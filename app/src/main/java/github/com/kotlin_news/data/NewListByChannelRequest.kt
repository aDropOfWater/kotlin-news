package github.com.kotlin_news.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import github.com.kotlin_news.obtainNewsStr
import java.net.URL

/**
 * Created by guoshuaijie on 2017/7/21.
 */
class NewListByChannelRequest (val type: String,val id: String, val startPage: String, val gson: Gson = Gson()){

    companion object {
        //新闻，视频
        val NETEAST_HOST = "http://c.m.163.com"

    }
    fun execute(): List<newListItem> {
        val url = "$NETEAST_HOST/nc/article/$type/$id/$startPage-20.html"
        val forecastJsonStr = URL(url).readText()
        val type = object : TypeToken<ArrayList<newListItem>>() {}.type
        val listItem =  gson.fromJson<List<newListItem>>(forecastJsonStr.obtainNewsStr(id),type)
        return listItem
    }
}