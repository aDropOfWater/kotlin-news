package github.com.kotlin_news.data.server

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import github.com.kotlin_news.App
import github.com.kotlin_news.data.db.NewDb
import github.com.kotlin_news.data.newDeatilBean
import github.com.kotlin_news.data.newListItem
import github.com.kotlin_news.data.photoset
import github.com.kotlin_news.domain.datasource.NewDataSource
import github.com.kotlin_news.util.log
import github.com.kotlin_news.util.obtainNewsStr
import java.net.URL
import java.util.NoSuchElementException

/**
 * Created by guoshuaijie on 2017/7/25.
 */
class NewServer(val newDb: NewDb = NewDb()) : NewDataSource {
    override fun requestNewPhotosDetail(id: String): List<photoset>? {
        throw NoSuchElementException("图集详情数据请在本地获取") as Throwable
    }

    companion object {
        //新闻，视频
        private val NETEAST_HOST = "http://c.m.163.com"
        val gson: Gson = Gson()
    }


    override fun requestNewDetail(id: String): newDeatilBean? {
        val url = "$NETEAST_HOST/nc/article/$id/full.html"
        val forecastJsonStr = URL(url).readText()
        log(forecastJsonStr)
        val jsonObject = JsonParser().parse(forecastJsonStr).asJsonObject
        val newPbject = jsonObject.getAsJsonObject(id)
        newPbject?.apply {
            val hashMap = HashMap<String, Any?>()
            hashMap.put("postid", id)
            hashMap.put("sourceName", newPbject.getAsJsonObject("sourceinfo")?.get("tname")?.asString ?: newPbject["source"].asString)
            hashMap.put("sourceIconUrl", newPbject.getAsJsonObject("sourceinfo")?.get("topic_icons")?.asString ?: "")
            hashMap.put("title", newPbject["title"].asString)
            hashMap.put("ptime", newPbject["ptime"].asString)
            var body = newPbject["body"].asString
            val asJsonArray = newPbject.getAsJsonArray("img")
            val size = asJsonArray.size()
            for (i in 0 until size) {
                val get = asJsonArray.get(i)
                if (get is JsonObject) {
                    val newChars = "<img src=\"${get["src"].asString}\" />"
                    body = body.replace(get["ref"].asString, if (i == 0) "" else newChars)
                }
            }
//            newPbject.getAsJsonArray("img").forEach {
//                if (it is JsonObject) {
//                    val newChars = "<img src=\"${it["src"].asString}\" />"
//                    body = body.replace(it["ref"].asString, newChars)
//                }
//            }
            hashMap.put("body", body)
            val newDeatilBean = newDeatilBean(hashMap)
            newDb.saveNewDetail(newDeatilBean)
            return newDeatilBean
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