package github.com.kotlin_news

import android.util.Log
import com.google.gson.JsonParser

/**
 * Created by guoshuaijie on 2017/7/21.
 */
fun Any.log(msg: String?) {
    Log.e(this.javaClass.simpleName, msg)
}

fun String.obtainNewsStr( name: String): String {
    val jsonObject = JsonParser().parse(this).asJsonObject
    val array= jsonObject.getAsJsonArray(name)
    return array.toString()
}