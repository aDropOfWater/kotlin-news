package github.com.kotlin_news

import github.com.kotlin_news.data.newListItem

/**
 * Created by guoshuaijie on 2017/7/27.
 */
/**
 * 新闻列表数据生成时候的事件
 * 用于从数据库或者网络获取到数据时候，推送到view界面刷新
 */
data class NewListProduceEvent(var source: dataSources, val list: List<newListItem>, val channlId: String)

/**
 * 数据来源
 */
enum class dataSources {
    //本地数据
    DATABASE,
    //网络数据
    NETWORK
}