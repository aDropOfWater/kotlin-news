package github.com.kotlin_news.data.db

/**
 * Created by guoshuaijie on 2017/7/25.
 */
/**
 * 新闻列表
 */
object newListTable{
//    val NAME = "newList"
    val ID = "_id"

    val postid = "postid"
    val title = "title"
    val digest = "digest"
    val imgsrc = "imgsrc"
    val ptime = "ptime"
}
object newListPhotoSetTable{
    val NAME = "photoset"
    val ID = "_id"

    val postid = "postid"
    val skipID = "skipID"
    val title = "title"
    val imgsrc = "imgsrc"
}

object errorTable{
    val NAME = "error"
    val ID = "_id"

    val msg = "msg"
    //元数据
    val meta = "meta"
    //备注
    val remark = "remark"
}