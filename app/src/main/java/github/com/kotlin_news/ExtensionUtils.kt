package github.com.kotlin_news

import android.util.Log

/**
 * Created by guoshuaijie on 2017/7/21.
 */
fun Any.log(msg: String?) {
    Log.e(this.javaClass.simpleName, msg)
}