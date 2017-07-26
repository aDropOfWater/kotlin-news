package github.com.kotlin_news.data.server

import github.com.kotlin_news.data.db.NewDb
import github.com.kotlin_news.data.newListItem
import github.com.kotlin_news.domain.datasource.NewDataSource

/**
 * Created by guoshuaijie on 2017/7/25.
 */
class NewServer(val newDb: NewDb = NewDb()): NewDataSource {
    override fun requestNewList(type: String, channlId: String, startPage: Int): List<newListItem>? {
        val result = NewListByChannelRequest(type, channlId, startPage).execute()
        newDb.saveNewList(result,channlId)
        return result
    }
}