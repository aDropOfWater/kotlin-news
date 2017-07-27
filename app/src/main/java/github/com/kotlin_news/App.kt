package github.com.kotlin_news

import android.app.Application
import github.com.kotlin_news.util.DelegatesExt
import org.greenrobot.eventbus.EventBus

/**
 * Created by guoshuaijie on 2017/7/25.
 */
class App : Application(){

    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()
        //新闻列表默认每次加载的条数
        val newItemLoadNumber = 20
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        EventBus.builder().throwSubscriberException(true)
    }
}