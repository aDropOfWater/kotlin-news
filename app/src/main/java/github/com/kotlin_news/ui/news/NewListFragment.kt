package github.com.kotlin_news.ui.news


import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.kotlin_news.App
import github.com.kotlin_news.NewListProduceEvent
import github.com.kotlin_news.R
import github.com.kotlin_news.dataSources
import github.com.kotlin_news.domain.commands.RequestCommand
import github.com.kotlin_news.ui.news.adapter.NewListAdapter
import github.com.kotlin_news.util.ctx
import github.com.kotlin_news.util.log
import github.com.kotlin_news.util.toast
import kotlinx.android.synthetic.main.fragment_new_list.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread

class  NewListFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_new_list, container, false)
        return rootView
    }

    val newListAdapter = NewListAdapter { v, (postid, _, digest, imgsrc) ->
        if(digest.isNullOrEmpty()){
            val intent = Intent(activity,NewPhotoDetailActivity::class.java)
            intent.putExtra("id" , postid)
            startActivity(intent)
            return@NewListAdapter
        }
        val intent = Intent(activity,NewDetailActivity::class.java)
        intent.putExtra("id" , postid)
        intent.putExtra("url" , imgsrc)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val options = ActivityOptions.makeSceneTransitionAnimation(activity, v, App.TRANSITION_ANIMATION_NEWS_PHOTOS)
            startActivity(intent, options.toBundle())
        } else {
            val options = ActivityOptionsCompat.makeScaleUpAnimation(v, v.width / 2, v.height / 2, 0, 0)
            ActivityCompat.startActivity(activity, intent, options.toBundle())
        }



    }
    lateinit var linearLayoutManager: LinearLayoutManager
    var lastVisibleItem = -1
    var startPage = 0
    var type = "list"
    var id = ""
    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white)
        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light, android.R.color.holo_orange_light,
                android.R.color.holo_green_light)
        refreshLayout.setProgressViewOffset(false, 0, TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources
                        .displayMetrics).toInt())
        linearLayoutManager = LinearLayoutManager(view?.ctx)
        newRecyView.layoutManager = linearLayoutManager
        newRecyView.adapter = newListAdapter
        refreshLayout.setOnRefreshListener {
            if (!refreshLayout.isRefreshing) refreshLayout.isRefreshing = true
            getData()
        }
        newRecyView.itemAnimator = DefaultItemAnimator()
        //第一个展示的fragment   默认加载数据
        if (arguments.getBoolean(IS_FIRST_ITEM, false)) firstRefresh()
        newRecyView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 == newListAdapter.itemCount) {
                    getData()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: NewListProduceEvent) {
        if (event.channlId != id) return
        when (event.source) {
            dataSources.DATABASE -> {
                log("从本地获取到:${event.list.size}条数据,当前时间：${System.currentTimeMillis()}")
                newListAdapter.addAll(event.list)
            }
            dataSources.NETWORK -> {
                toast("刷新${event.list.size}条数据")
                log("从网络获取到:${event.list.size}条数据,当前时间：${System.currentTimeMillis()}")
                if (refreshLayout.isRefreshing) {//刷新状态
                    newListAdapter.addAll(event.list, 0)
                    newRecyView.scrollToPosition(0)
                } else {//加载更多时候
                    newListAdapter.addAll(event.list)
                }
            }
        }
    }

    fun getData() {
        log("开始获取：${arguments.getString(CHANNEL_NAME)}  的数据,startPage=$startPage-----refreshLayout.isRefreshing=${refreshLayout.isRefreshing}")
        doAsync {
            if (convertFromNameToTypeAndId(arguments.getString(CHANNEL_NAME))) {
                RequestCommand.requestNewList(type, id, if (refreshLayout.isRefreshing) 0 else startPage)
            } else {
                activity.toast("参数初始化错误")
            }
            uiThread {
                refreshLayout.isRefreshing = false
                startPage += App.newItemLoadNumber
            }
        }
    }

    private fun convertFromNameToTypeAndId(channl: String): Boolean {
        when (channl) {
            "头条" -> {
                type = "headline";id = "T1348647909107"
            }
            "科技" -> {
                id = "T1348649580692"
            }
            "财经" -> {
                id = "T1348648756099"
            }
            "军事" -> {
                id = "T1348648141035"
            }
            "体育" -> {
                id = "T1348649079062"
            }
            else -> return false
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    companion object {
        private val CHANNEL_NAME = "channel_name"
        private val IS_FIRST_ITEM = "IS_FIRST_ITEM"
        fun newInstance(channel: String, first: Boolean = false): NewListFragment {
            val fragment = NewListFragment()
            val args = Bundle()
            args.putString(CHANNEL_NAME, channel)
            args.putBoolean(IS_FIRST_ITEM, first)
            fragment.arguments = args
            return fragment
        }
    }

    fun firstRefresh() {
        if (newListAdapter.itemCount == 0) {
            refreshLayout.isRefreshing = true
            getData()
        }
    }
}