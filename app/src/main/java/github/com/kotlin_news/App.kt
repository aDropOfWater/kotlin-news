package github.com.kotlin_news

import android.app.Application
import com.squareup.picasso.Picasso
import com.zzhoujay.richtext.RichText
import github.com.kotlin_news.util.DelegatesExt
import github.com.kotlin_news.util.setImag
import org.greenrobot.eventbus.EventBus

/**
 * Created by guoshuaijie on 2017/7/25.
 */
class App : Application(){

    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()
        //新闻列表默认每次加载的条数
        val newItemLoadNumber = 20
        val TRANSITION_ANIMATION_NEWS_PHOTOS = "transition_animation_news_photos"
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        EventBus.builder().throwSubscriberException(true)
        Picasso.with(this).setIndicatorsEnabled(true)
        RichText.initCacheDir(instance)
    }
}