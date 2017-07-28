package github.com.kotlin_news.data.server


import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import github.com.kotlin_news.App
import github.com.kotlin_news.data.newListItem
import github.com.kotlin_news.util.log
import github.com.kotlin_news.util.obtainNewsStr
import java.net.URL

/**
 * Created by guoshuaijie on 2017/7/21.
 */
class NewListByChannelRequest(val type: String, val channlId: String, val startPage: Int, val gson: Gson = Gson()) {

    companion object {
        //新闻，视频
        private val NETEAST_HOST = "http://c.m.163.com"

    }

    fun execute(): List<newListItem> {
        val url = "$NETEAST_HOST/nc/article/$type/$channlId/$startPage-${App.newItemLoadNumber}.html"
        val forecastJsonStr = URL(url).readText()
        log("从网络获取到的数据...")
        val type = object : TypeToken<List<newListItem>>() {}.type
        val listItem = gson.fromJson<List<newListItem>>(forecastJsonStr.obtainNewsStr(channlId), type)
        log("从网络获取到${listItem.size}条数据")
//        listItem.map {
//            it.timeLong = it.ptime.switchTimeStrToLong()
//            var timeFromeNew = Date().getTimeFromeNew(it.timeLong)
//            if (timeFromeNew.isNullOrEmpty()) timeFromeNew = it.ptime.substring(5, 16)
//            it.publishtime = timeFromeNew
//        }
//        var filterNot = listItem.filterNot {
//            it.digest.isNullOrEmpty() && it.ads.isNullOrEmpty()
//        }
//        filterNot = filterNot.sortedByDescending { it.timeLong }
//        filterNot.forEach {
//            val instance = Calendar.getInstance(Locale.CHINA)
//            instance.timeInMillis = it.timeLong
//            log("it.ptime:${it.ptime}--it.timeLong:${it.timeLong}--instance.time:${instance.time}")
//        }
//        NewDb().saveNewList(filterNot,channlId)
        return listItem
    }
}