package github.com.kotlin_news.ui.news

import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import github.com.kotlin_news.R
import github.com.kotlin_news.data.newChannel
import github.com.kotlin_news.domain.commands.RequestCommand
import github.com.kotlin_news.ui.news.adapter.NewChannelAdapter
import kotlinx.android.synthetic.main.activity_new_channel.*
import org.jetbrains.anko.contentView
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.animation.Animation
import android.widget.ImageView
import java.util.ArrayList
import android.view.animation.TranslateAnimation
import github.com.kotlin_news.util.log
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread


/**
 * Created by guoshuaijie on 2017/8/15.
 * 新闻频道管理界面
 */
class NewChannelActivity : AppCompatActivity() {

    val itemCilck = { p: Int, v: View, c: newChannel ->
        //1.将点击到的view复制出来一个POP
        val location = IntArray(2)
        v.getLocationInWindow(location)
        val window = PopupWindow()
        val pp = ImageView(NewChannelActivity@ this)
        pp.setImageBitmap(view2bitmap(v))
        window.contentView = pp
        window.width = ViewGroup.LayoutParams.WRAP_CONTENT
        window.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.setBackgroundDrawable(ColorDrawable())
        window.showAtLocation(contentView, Gravity.NO_GRAVITY, location[0], location[1])
        //2.隐藏点击到的view
        val old = notShowAdapter.remove(p)
        //3.获取pop将要飞过去的位置
        showAdapter.add(old)
        doAsync {
            Thread.sleep(200)
            uiThread {
                val addView = showLayoutManager.findViewByPosition(showLayoutManager.childCount - 1)
                val toLocation = IntArray(2)
                addView.getLocationInWindow(toLocation)
                //pop开始平移过去指定位置
                val animation = ValueAnimator.ofFloat(0f, 1f)
                animation.duration = 1500
                //animation.fillAfter = true//设置为true，动画转化结束后被应用
                val xTranslate = toLocation[0] - location[0]
                val yTranslate = toLocation[1] - location[1]
                animation.addUpdateListener { animation ->
                    val animatedValue = animation.animatedValue as Float
                    val xx = location[0] + xTranslate * animatedValue
                    val yy = location[1] + yTranslate * animatedValue
                    log("起始位置x=${location[0]},y=${location[1]},终点位置x=${toLocation[0]},y=${toLocation[1]},平移x=${xx.toInt()},y=${yy.toInt()}")
                    window.update(xx.toInt(), yy.toInt(), -1, -1)
                }
                animation.start()
            }
        }
        Unit
    }

    fun view2bitmap(view: View): Bitmap {
        val width = view.measuredWidth
        val height = view.measuredHeight
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        view.draw(Canvas(bitmap))
        return bitmap
    }

    lateinit var showAdapter: NewChannelAdapter
    lateinit var showLayoutManager: GridLayoutManager
    lateinit var notShowAdapter: NewChannelAdapter
    lateinit var notShowLayoutManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_channel)
        //展示在界面上的新闻tab
        val showOnTab = RequestCommand.requestNewChannelList(true)
        //没有展示在新闻页
        val notShowOnTab = RequestCommand.requestNewChannelList(false)


        showAdapter = NewChannelAdapter(ArrayList(showOnTab), itemCilck)
        notShowAdapter = NewChannelAdapter(ArrayList(notShowOnTab), itemCilck)

        rlMyChannel.adapter = showAdapter
        showLayoutManager = GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        rlMyChannel.layoutManager = showLayoutManager
        rlMyChannel.itemAnimator = DefaultItemAnimator()


        rlMoreChannel.adapter = notShowAdapter
        notShowLayoutManager = GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        rlMoreChannel.layoutManager = notShowLayoutManager
        rlMoreChannel.itemAnimator = DefaultItemAnimator()


    }


}