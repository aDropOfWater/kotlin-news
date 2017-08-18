package github.com.kotlin_news.data

import java.io.Serializable

/**
 * 新闻频道model
 */
data class newChannel(val channelName: String, val channelId: String, var channelSelect: Boolean,val editAble: Boolean, var index: Int) : Serializable

/**
 * 新闻列表
 */
data class newListItem(val postid: String, val title: String, val digest: String, val imgsrc: String, val ptime: String, var ads: ArrayList<photoset>?) {
    var insertSuccess = false
    var publishtime: String = ""
    var timeLong: Long = 0
}

/**
 * 新闻列表中夹杂的图集
 */
data class photoset(val skipID: String, val title: String, val imgsrc: String)


/**
 * 新闻详情
 */
data class newDeatilBean(var map: MutableMap<String, Any?>) {
    val postid: String by map
    val sourceName: String by map
    val sourceIconUrl: String by map
    val title: String by map
    val body: String by map
    val ptime: String by map
}



