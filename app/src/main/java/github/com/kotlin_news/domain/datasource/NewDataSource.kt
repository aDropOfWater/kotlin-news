package github.com.kotlin_news.domain.datasource

import github.com.kotlin_news.data.newDeatilBean
import github.com.kotlin_news.data.newListItem

/**
 * Created by guoshuaijie on 2017/7/25.
 */
interface NewDataSource {
    fun requestNewList(type: String, id: String, startPage: Int): List<newListItem>?

    fun requestNewDetail(id: String): newDeatilBean?
}