package github.com.kotlin_news.domain.commands

import github.com.kotlin_news.NewListProduceEvent
import github.com.kotlin_news.data.db.NewDb
import github.com.kotlin_news.data.server.NewServer
import github.com.kotlin_news.dataSources
import github.com.kotlin_news.domain.datasource.NewDataSource
import github.com.kotlin_news.util.*
import org.jetbrains.anko.db.NULL
import java.util.*

/**
 * Created by guoshuaijie on 2017/7/25.
 */
class NewsProvider(val sources: List<NewDataSource> = NewsProvider.SOURCES) {
    companion object {

        private val SOURCES by lazy {
            listOf(NewDb(), NewServer())
        }
    }

    private fun requestToMergeSources(f: (NewDataSource) -> Unit): Unit {
        sources.mergeResult { f(it) }
    }

    private fun <T : Any> Iterable<T>.mergeResult(predicate: (T) -> Unit): Unit {
        for (element in this) {
            predicate(element)
        }
    }

    fun requestNewList(type: String, channlId: String, startPage: Int) = requestToMergeSources {
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
            post(NewListProduceEvent(if (it is NewDb) dataSources.DATABASE else dataSources.NETWORK, filterNot, channlId))
        }
    }

    private fun <T : Any> requestToFirstSources(f: (NewDataSource) -> T?): T = sources.firstResult { f(it) }

    private fun <T, R : Any> Iterable<T>.firstResult(predicate: (T) -> R?): R {
        for (element in this) {
            val result = predicate(element)
            if (result != null) return result
        }
        throw NoSuchElementException("No element matching predicate was found.")
    }

    fun requestNewDetail(id: String) = requestToFirstSources {
        it.requestNewDetail(id)
//        res?.apply {
//            res.map {
//                it.timeLong = it.ptime.switchTimeStrToLong()
//                var timeFromeNew = Date().getTimeFromeNew(it.timeLong)
//                if (timeFromeNew.isNullOrEmpty()) timeFromeNew = it.ptime.substring(5, 16)
//                it.publishtime = timeFromeNew
//            }
//            var filterNot = res.filterNot {
//                it.digest.isNullOrEmpty() && it.ads.isNullOrEmpty()
//            }
//            filterNot = filterNot.sortedByDescending { it.timeLong }
//            filterNot.forEach {
//                val instance = Calendar.getInstance(Locale.CHINA)
//                instance.timeInMillis = it.timeLong
//                //log("it.ptime:${it.ptime}--it.timeLong:${it.timeLong}--instance.time:${instance.time}")
//            }
//            post(NewListProduceEvent(if (it is NewDb) dataSources.DATABASE else dataSources.NETWORK, filterNot, channlId))
//        }
    }


}