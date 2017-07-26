package github.com.kotlin_news.domain.commands

import github.com.kotlin_news.data.newListItem

/**
 * Created by guoshuaijie on 2017/7/25.
 * type: String, channlId: String, startPage: Int
 */
class RequestNewListCommand(
        val type: String,
        val channlId: String,
        val startPage: Int,
        val newsProvider: NewsProvider = NewsProvider(),
        val returnData: (List<newListItem>, dataSources) -> Unit)
    : Command {
    override fun execute() {
        val map = newsProvider.requestNewList(type, channlId, startPage)
        for(i in 0..map.size()){
            val valueAt = map.valueAt(i)
            val keyAt = map.keyAt(i)
            if(valueAt!=null){
                returnData(valueAt, keyAt)
            }
        }
    }
}
//class RequestNewListCommand(
//        val type: String,
//        val channlId: String,
//        val startPage: Int,
//        val newsProvider: NewsProvider = NewsProvider())
//    : Command<List<newListItem>> {
//    override fun execute(): List<newListItem> = newsProvider.requestNewList(type, channlId, startPage)
//}