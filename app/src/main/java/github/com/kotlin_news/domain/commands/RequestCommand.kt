package github.com.kotlin_news.domain.commands

/**
 * Created by guoshuaijie on 2017/7/25.
 * type: String, channlId: String, startPage: Int
 */
class RequestCommand {
    companion object {
        private val newsProvider: NewsProvider = NewsProvider()
        fun requestNewList(type: String,
                           channlId: String,
                           startPage: Int) {
            newsProvider.requestNewList(type, channlId, startPage)
        }

        fun requestNewDetail(newId: String) = newsProvider.requestNewDetail(newId)

        fun requestNewPhotosDetail(newId: String) = newsProvider.requestNewPhotosDetail(newId)

        fun requestNewChannelList(isSelect: Boolean = false) = newsProvider.requestNewChannelList(isSelect)

    }
}
//class RequestCommand(
//        val type: String,
//        val channlId: String,
//        val startPage: Int,
//        val newsProvider: NewsProvider = NewsProvider())
//    : Command<List<newListItem>> {
//    override fun execute(): List<newListItem> = newsProvider.requestNewList(type, channlId, startPage)
//}