package github.com.kotlin_news.data

/**
 * Created by guoshuaijie on 2017/7/21.
 */
/**
 * 新闻列表
 */
data class newListItem(val postid: String, val title: String, val digest: String, val imgsrc: String, val ptime: String, var ads: ArrayList<photoset>?){
    var publishtime:String=""
    var timeLong: Long = 0
}

/**
 * 新闻列表中夹杂的图集
 */
data class photoset(val skipID: String,val title: String, val imgsrc: String)

