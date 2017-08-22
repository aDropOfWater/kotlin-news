package github.com.kotlin_news.ui.news

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import github.com.kotlin_news.R
import github.com.kotlin_news.data.newChannel
import github.com.kotlin_news.domain.commands.RequestCommand
import github.com.kotlin_news.ui.anim.ChannelItemAnim
import github.com.kotlin_news.ui.news.adapter.NewChannelAdapter
import github.com.kotlin_news.util.log
import kotlinx.android.synthetic.main.activity_new_channel.*
import org.jetbrains.anko.contentView
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


/**
 * Created by guoshuaijie on 2017/8/15.
 * 新闻频道管理界面
 */
class NewChannelActivity : AppCompatActivity() {

    val itemCilck = { p: Int, v: View, c: newChannel ->
        log("position=$p,name=${c.channelName}")
        val location = IntArray(2)
        val pp = ImageView(NewChannelActivity@ this)
        //1.将点击到的view复制出来一个POP
        v.getLocationInWindow(location)
        pp.setImageBitmap(view2bitmap(v))
        window.contentView = pp
        window.showAtLocation(contentView, Gravity.NO_GRAVITY, location[0], location[1])
        //2.隐藏点击到的view
        //3.获取pop将要飞过去的位置
        var index = 0
        if (c.channelSelect) {
            val old = showAdapter.remove(p)
            notShowAdapter.add(old, 0)
            index= 0
        } else {
            val old = notShowAdapter.remove(p)
            showAdapter.add(old, -1)
            index = showAdapter.itemCount-1
        }
        doAsync {
            Thread.sleep(100)
            uiThread {
                val addView: View?
                if (c.channelSelect) {
                    addView = notShowLayoutManager.findViewByPosition(0)
                } else {
                    val childSize = showLayoutManager.childCount
                    addView = showLayoutManager.findViewByPosition(childSize - 1)
                }
                if (addView == null) {
                    window.dismiss()
                    return@uiThread
                }
                val toLocation = IntArray(2)
                addView.getLocationInWindow(toLocation)
                //pop开始平移过去指定位置
                val animation = ValueAnimator.ofFloat(0f, 1f)
                animation.duration = 400
                val xTranslate = toLocation[0] - location[0]
                val yTranslate = toLocation[1] - location[1]
                animation.addUpdateListener {
                    val animatedValue = it.animatedValue as Float
                    val xx = location[0] + xTranslate * animatedValue
                    val yy = location[1] + yTranslate * animatedValue
                    log("起始位置x=${location[0]},y=${location[1]},终点位置x=${toLocation[0]},y=${toLocation[1]},平移x=${xx.toInt()},y=${yy.toInt()}")
                    window.update(xx.toInt(), yy.toInt(), -1, -1)
                    when (animatedValue) {
                        1f -> {
                            //4.改变newChannel的数据状态
                            //找到from  top 2  bottom的view
//                            var view = (if (c.channelSelect) notShowLayoutManager else showLayoutManager).findViewByPosition(index)
                            var view:View
                            if (c.channelSelect) {
                                 view = notShowLayoutManager.findViewByPosition(index)
                            } else {
                                 view = showLayoutManager.findViewByPosition(index)
                            }
                            log("view.hashCode()=${view.hashCode()}")
                            ViewCompat.setAlpha(view, 1f)
                            window.dismiss()
                            c.channelSelect = !c.channelSelect
                        }
                    }
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

    val window: PopupWindow by lazy {
        val window = PopupWindow()
        window.width = ViewGroup.LayoutParams.WRAP_CONTENT
        window.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.setBackgroundDrawable(ColorDrawable())
        window
    }

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
        val anmi = ChannelItemAnim()
        rlMyChannel.itemAnimator = anmi
        val helper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, 0) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPosition = viewHolder.adapterPosition//得到拖动ViewHolder的position
                val toPosition = target.adapterPosition//得到目标ViewHolder的position
//                if (fromPosition < toPosition) {
//                    //分别把中间所有的item的位置重新交换
//                    for (i in fromPosition..toPosition - 1) {
//                        Collections.swap(showAdapter.newList, i, i + 1)
//                    }
//                } else {
//                    for (i in fromPosition downTo toPosition + 1) {
//                        Collections.swap(showAdapter.newList, i, i - 1)
//                    }
//                }
                Collections.swap(showAdapter.newList, fromPosition, toPosition)
                showAdapter.notifyItemMoved(fromPosition, toPosition)
                showAdapter.notifyItemRangeChanged(0, showAdapter.itemCount)
                return true
            }

            override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        })
        helper.attachToRecyclerView(rlMyChannel)


        rlMoreChannel.adapter = notShowAdapter
        notShowLayoutManager = GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false)
        rlMoreChannel.layoutManager = notShowLayoutManager
        val anma = ChannelItemAnim()
        rlMoreChannel.itemAnimator = anma


    }


}