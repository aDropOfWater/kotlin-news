package github.com.kotlin_news.data.db


/**
 * 新闻频道
 */
object newChannelsTable{
    val ID = "_id"
    val NAME = "newChannelsTable"
    val channelName = "channelName"
    val channelId = "channelId"
    //该频道是否已经选择显示， 1标识显示，0标识不显示
    val channelSelect = "channelSelect"
    //是否可以编辑，1标识可以，0标识不可以
    val editAble = "editAble"
    val index = "channelIndex"
}
/**
 * 新闻列表
 */
object newListTable{
    val ID = "_id"
    val postid = "postid"
    val title = "title"
    val digest = "digest"
    val imgsrc = "imgsrc"
    val ptime = "ptime"
}

/**
 * 新闻列表中的图集
 */
object newListPhotoSetTable{
    val NAME = "photoset"
    val ID = "_id"
    val postid = "postid"
    val skipID = "skipID"
    val title = "title"
    val imgsrc = "imgsrc"
}
/**
 * 新闻详情
 */
object newDetailTable{
    val NAME = "newDetail"
    val ID = "_id"
    val postid = "postid"
    val sourceName = "sourceName"
    val sourceIconUrl = "sourceIconUrl"
    val title = "title"
    val ptime = "ptime"
    val body = "body"
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