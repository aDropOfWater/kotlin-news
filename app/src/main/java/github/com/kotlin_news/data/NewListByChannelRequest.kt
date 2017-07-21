package github.com.kotlin_news.data

import github.com.kotlin_news.log
import java.net.URL

/**
 * Created by guoshuaijie on 2017/7/21.
 */
class NewListByChannelRequest (val type: String,val id: String, val startPage: String){

    companion object {
        //新闻，视频
        val NETEAST_HOST = "http://c.m.163.com"

    }
    fun execute() {
        val url = "$NETEAST_HOST/nc/article/$type/$id/$startPage-20.html"
        val forecastJsonStr = URL(url).readText()
        log("demo从网络获取到的数据：$forecastJsonStr")
//        return gson.fromJson(forecastJsonStr, ForecastResult::class.java)
    }
}