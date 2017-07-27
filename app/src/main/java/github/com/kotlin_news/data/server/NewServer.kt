package github.com.kotlin_news.data.server

import github.com.kotlin_news.data.db.NewDb
import github.com.kotlin_news.data.newListItem
import github.com.kotlin_news.domain.datasource.NewDataSource
import github.com.kotlin_news.util.log

/**
 * Created by guoshuaijie on 2017/7/25.
 */
class NewServer(val newDb: NewDb = NewDb()): NewDataSource {
    override fun requestNewList(type: String, channlId: String, startPage: Int): List<newListItem>? {
        val start = System.currentTimeMillis()
        val result = NewListByChannelRequest(type, channlId, startPage).execute()
        log("网络查询耗时：${System.currentTimeMillis()-start}")
        val newList = newDb.saveNewList(result, channlId)
        log("数据固化耗时：${System.currentTimeMillis()-start}")
        return newList
    }
}