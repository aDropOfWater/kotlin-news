package github.com.kotlin_news.domain.commands

/**
 * Created by guoshuaijie on 2017/7/25.
 * type: String, channlId: String, startPage: Int
 */
class RequestNewListCommand(
        val type: String,
        val channlId: String,
        val startPage: Int,
        val newsProvider: NewsProvider = NewsProvider())
    : Command {
    override fun execute() {
        newsProvider.requestNewList(type, channlId, startPage)
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