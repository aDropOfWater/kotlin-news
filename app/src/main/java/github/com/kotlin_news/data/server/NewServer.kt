package github.com.kotlin_news.data.server

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import github.com.kotlin_news.App
import github.com.kotlin_news.data.db.NewDb
import github.com.kotlin_news.data.newDeatilBean
import github.com.kotlin_news.data.newListItem
import github.com.kotlin_news.domain.datasource.NewDataSource
import github.com.kotlin_news.util.log
import github.com.kotlin_news.util.obtainNewsStr
import java.net.URL

/**
 * Created by guoshuaijie on 2017/7/25.
 */
class NewServer(val newDb: NewDb = NewDb()) : NewDataSource {
    companion object {
        //新闻，视频
        private val NETEAST_HOST = "http://c.m.163.com"
        val gson: Gson = Gson()
    }


    override fun requestNewDetail(postId: String): newDeatilBean? {
        val url = "$NETEAST_HOST/nc/article/$postId/full.html"
        val forecastJsonStr = URL(url).readText()
        log(forecastJsonStr)
        val jsonObject = JsonParser().parse(forecastJsonStr).asJsonObject
        val newPbject = jsonObject.getAsJsonObject(postId)
        newPbject?.apply {
            val hashMap = HashMap<String, Any?>()
            hashMap.put("postid", postId)
            hashMap.put("sourceName", newPbject.getAsJsonObject("sourceinfo")?.get("tname")?.asString?:newPbject["source"].asString)
            hashMap.put("sourceIconUrl", newPbject.getAsJsonObject("sourceinfo")?.get("topic_icons")?.asString?:"")
            hashMap.put("title", newPbject["title"].asString)
            hashMap.put("ptime", newPbject["ptime"].asString)
            var body = newPbject["body"].asString
            newPbject.getAsJsonArray("img").forEach {
                if (it is JsonObject) {
                    val newChars = "<img src=\"${it["src"].asString}\" />"
                    body = body.replace(it["ref"].asString, newChars)
                }
            }
            hashMap.put("body", body)
            return newDeatilBean(hashMap)
        }
        return null
    }

    override fun requestNewList(type: String, channlId: String, startPage: Int): List<newListItem>? {
        val start = System.currentTimeMillis()
        val url = "$NETEAST_HOST/nc/article/$type/$channlId/$startPage-${App.newItemLoadNumber}.html"
        val forecastJsonStr = URL(url).readText()
        log("从网络获取到的数据...")
        val type = object : TypeToken<List<newListItem>>() {}.type
        val result = gson.fromJson<List<newListItem>>(forecastJsonStr.obtainNewsStr(channlId), type)
        log("从网络获取到${result.size}条数据")
        log("网络查询耗时：${System.currentTimeMillis() - start}")
        val newList = newDb.saveNewList(result, channlId)
        log("数据固化耗时：${System.currentTimeMillis() - start}")
        log("数据库存储时候过滤掉${result.size - newList.size}条本地存在的数据，还剩下${newList.size}条数据")
        return newList
    }
}