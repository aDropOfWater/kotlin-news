package github.com.kotlin_news.domain.datasource

import github.com.kotlin_news.data.newChannel
import github.com.kotlin_news.data.newDeatilBean
import github.com.kotlin_news.data.newListItem
import github.com.kotlin_news.data.photoset

/**
 * Created by guoshuaijie on 2017/7/25.
 */
interface NewDataSource {
    fun requestNewList(type: String, id: String, startPage: Int): List<newListItem>?

    fun requestNewDetail(id: String): newDeatilBean?

    fun requestNewPhotosDetail(id: String): List<photoset>?

    fun requestNewChannelList(isSelect: Boolean = false): List<newChannel>

    fun saveChannelList(channels: List<newChannel>): Boolean
}