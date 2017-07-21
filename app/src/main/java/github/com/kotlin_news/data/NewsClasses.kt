package github.com.kotlin_news.data

/**
 * Created by guoshuaijie on 2017/7/21.
 */
data class newListItem(val postid: String, val title: String, val digest: String, val imgsrc: String, val ptime: String, val map: LinkedHashMap<String,String>)