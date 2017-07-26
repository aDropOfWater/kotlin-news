package github.com.kotlin_news.domain.commands

import android.support.v4.util.SimpleArrayMap
import github.com.kotlin_news.data.db.NewDb
import github.com.kotlin_news.data.server.NewServer
import github.com.kotlin_news.domain.datasource.NewDataSource
import github.com.kotlin_news.util.getTimeFromeNew
import github.com.kotlin_news.util.isNullOrEmpty
import github.com.kotlin_news.util.switchTimeStrToLong
import java.util.*

/**
 * Created by guoshuaijie on 2017/7/25.
 */
class NewsProvider(val sources: Map<dataSources, NewDataSource> = NewsProvider.SOURCES) {
    companion object {
        private val SOURCES by lazy {
            mapOf(dataSources.DATABASE to NewDb(),
                    dataSources.NETWORK to NewServer())
//            listOf(
//                    NewDb(),
//                    NewServer()
//            )
        }
    }

    fun requestNewList(type: String, channlId: String, startPage: Int) = requestToSources {
        val res = it.requestNewList(type, channlId, startPage)
        res?.apply {
            res.map {
                it.timeLong = it.ptime.switchTimeStrToLong()
                var timeFromeNew = Date().getTimeFromeNew(it.timeLong)
                if (timeFromeNew.isNullOrEmpty()) timeFromeNew = it.ptime.substring(5, 16)
                it.publishtime = timeFromeNew
            }
            var filterNot = res.filterNot {
                it.digest.isNullOrEmpty() && it.ads.isNullOrEmpty()
            }
            filterNot = filterNot.sortedByDescending { it.timeLong }
            filterNot.forEach {
                val instance = Calendar.getInstance(Locale.CHINA)
                instance.timeInMillis = it.timeLong
                //log("it.ptime:${it.ptime}--it.timeLong:${it.timeLong}--instance.time:${instance.time}")
            }
        }
        //if (!res.isNullOrEmpty()) res else null
    }

    //
    private fun <T : Any> requestToSources(f: (NewDataSource) -> T?): SimpleArrayMap<dataSources, T?> = sources.firstResult { f(it) }


    private fun <T, R : Any> Map<dataSources, T>.firstResult(predicate: (T) -> R?): SimpleArrayMap<dataSources, R?> {
        val mm = SimpleArrayMap<dataSources, R?>()
        for ((key, value) in this) {
            mm.put(key, predicate(value))
        }
        return mm
    }
}

/**
 * 数据来源
 */
enum class dataSources {
    //本地数据
    DATABASE,
    //网络数据
    NETWORK
}