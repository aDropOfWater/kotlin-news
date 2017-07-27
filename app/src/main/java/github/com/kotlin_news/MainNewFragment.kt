package github.com.kotlin_news

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.kotlin_news.adapter.NewListAdapter
import github.com.kotlin_news.domain.commands.RequestNewListCommand
import github.com.kotlin_news.util.ctx
import github.com.kotlin_news.util.getTimeFromeNew
import github.com.kotlin_news.util.log
import github.com.kotlin_news.util.toast
import kotlinx.android.synthetic.main.fra_main_new.*
import kotlinx.android.synthetic.main.fragment_main2.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.util.*


/**
 * Created by guoshuaijie on 2017/7/21.
 */
class MainNewFragment : Fragment() {

    companion object {
        var listOfTitle: Array<String> =App.instance.resources.getStringArray(R.array.news_channel_name_static)
        lateinit var fragmentList: List<PlaceholderFragment>
    }


    var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fra_main_new, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val first = listOfTitle[0]
        fragmentList = listOfTitle.map { PlaceholderFragment.newInstance(it, it == first) }

        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

        containerPager.adapter = mSectionsPagerAdapter

        slidingTabs.setupWithViewPager(containerPager)

        slidingTabs.tabMode = TabLayout.MODE_FIXED

        containerPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                fragmentList[position].getData()
            }
        })
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): PlaceholderFragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return listOfTitle.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return listOfTitle[position]
        }
    }


    class PlaceholderFragment : Fragment() {

        override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
            val rootView = inflater!!.inflate(R.layout.fragment_main2, container, false)
            return rootView
        }

        val newListAdapter = NewListAdapter {
            val timeFromeNew = Date().getTimeFromeNew(it.timeLong)
            log("timeFromeNew:$timeFromeNew-----System:${System.currentTimeMillis()}")
            log("it.ptime:${it.ptime}--it.timeLong:${it.timeLong}--it.publishtime:${it.publishtime}")
        }

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
            newRecyView.layoutManager = LinearLayoutManager(view?.ctx)
            newRecyView.adapter = newListAdapter
            refreshLayout.setOnRefreshListener { getData() }
            newRecyView.itemAnimator = DefaultItemAnimator()
            //第一个展示的fragment   默认加载数据
            if(arguments.getBoolean(IS_FIRST_ITEM,false))getData()
        }

        @Subscribe(threadMode = ThreadMode.MAIN)
        fun onMessageEvent(event: NewListProduceEvent) {
            if(event.channlId!=id)return
            when (event.source) {
                dataSources.DATABASE -> {
                    log("从本地获取到:${event.list.size}条数据,当前时间：${System.currentTimeMillis()}")
                    newListAdapter.addAll(event.list)
                }
                dataSources.NETWORK -> {
                    toast("刷新${event.list.size}条数据")
                    log("从网络获取到:${event.list.size}条数据,当前时间：${System.currentTimeMillis()}")
                    newListAdapter.addAll(event.list, 0)
                    newRecyView.scrollToPosition(0)
                }
            }
            if(refreshLayout.isRefreshing){
                refreshLayout.isRefreshing=false
            }else{
                startPage+=App.newItemLoadNumber
            }
        }

        fun getData() {
            log("开始获取：${arguments.getString(CHANNEL_NAME)}的数据")
            if(!refreshLayout.isRefreshing)refreshLayout.isRefreshing=true
            doAsync {
                if (convertFromNameToTypeAndId(arguments.getString(CHANNEL_NAME))) {
                    RequestNewListCommand(type, id,if(refreshLayout.isRefreshing) 0 else startPage).execute()
                } else {
                    activity.toast("参数初始化错误")
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
            fun newInstance(channel: String,first: Boolean = false): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putString(CHANNEL_NAME, channel)
                args.putBoolean(IS_FIRST_ITEM, first)
                fragment.arguments = args
                return fragment
            }
        }
    }
}