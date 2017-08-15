package github.com.kotlin_news.util

import android.content.Context
import android.graphics.Color
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonParser
import com.squareup.picasso.Picasso
import com.zzhoujay.richtext.CacheType
import com.zzhoujay.richtext.ImageHolder
import com.zzhoujay.richtext.RichText
import github.com.kotlin_news.R
import kotlinx.android.synthetic.main.activity_new_detail.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by guoshuaijie on 2017/7/21.
 */
val View.ctx: Context
    get() = context

fun ImageView.setImag(iconUrl: String) {
    if (iconUrl.isNullOrEmpty()) setImageResource(R.mipmap.ic_image_loading) else Picasso.with(ctx).load(iconUrl)
            .error(R.mipmap.ic_empty_picture)
            .placeholder(R.mipmap.ic_image_loading)
            .fit().into(this)
}

fun TextView.fromHtml(body: String) {
    RichText.fromHtml(body)
            .scaleType(ImageHolder.ScaleType.FIT_CENTER) // 图片缩放方式
            .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT) // 图片占位区域的宽高
            .placeHolder(R.mipmap.ic_image_loading) // 设置加载中显示的占位图
            .error(R.mipmap.ic_empty_picture) // 设置加载失败的错误图
            .cache(CacheType.ALL) // 缓存类型，默认为Cache.ALL（缓存图片和图片大小信息和文本样式信息）
            .into(this)

}


fun Fragment.toast(message: CharSequence) = Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()


fun Any.log(msg: String?) {
    Log.e(this.javaClass.simpleName, msg)
}

/**
 * 获取截取掉新闻列表ID字符串
 */
fun String.obtainNewsStr(name: String): String {
    val jsonObject = JsonParser().parse(this).asJsonObject
    val array = jsonObject.getAsJsonArray(name)
    return array.toString()
}

val oneHour = 1000 * 60 * 60
/**
 * 获取过去某个时间点距离现在的分钟数，只计算一个小时以内的
 * 窜过来的时间格式yyyy-MM-dd hh:mm:ss      2017-07-21 10:57:08
 */
fun Date.getTimeFromeNew(oldTime: Long): String {
    val timeSpace = time - oldTime
    return when (timeSpace) {
        in 0..60000 -> "${timeSpace / 1000}秒钟前" //一分钟之内
        in 60000..oneHour -> "${timeSpace / 60000}分钟前"//一个小时之内
        else -> ""
    }
}

fun String.switchTimeStrToLong(format: String = "yyyy-MM-dd HH:mm:ss"): Long {
    val sdf = SimpleDateFormat(format, Locale.getDefault())
    var d: Date = sdf.parse(this)
    return d.time
}

fun <T : Any> List<T>?.isNullOrEmpty(): Boolean = this == null || this.isEmpty()

fun Boolean.switch2String(): String = if (this) "1" else "0"

fun String.switch2Boolean(): Boolean = "1" == this




