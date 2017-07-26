package github.com.kotlin_news.domain.commands

/**
 * Created by guoshuaijie on 2017/7/25.
 */
interface Command<out T> {
    fun execute(): T
}