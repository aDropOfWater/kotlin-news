package github.com.kotlin_news

import android.app.Application
import github.com.kotlin_news.util.DelegatesExt

/**
 * Created by guoshuaijie on 2017/7/25.
 */
class App : Application(){

    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}