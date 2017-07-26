package github.com.kotlin_news

import android.os.Bundle
import android.support.annotation.UiThread
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.kotlin_news.adapter.NewListAdapter
import github.com.kotlin_news.data.newListItem
import github.com.kotlin_news.domain.commands.RequestNewListCommand
import github.com.kotlin_news.domain.commands.dataSources
import github.com.kotlin_news.util.ctx
import github.com.kotlin_news.util.getTimeFromeNew
import github.com.kotlin_news.util.log
import kotlinx.android.synthetic.main.fra_main_new.*
import kotlinx.android.synthetic.main.fragment_main2.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.util.*
import android.util.TypedValue



/**
 * Created by guoshuaijie on 2017/7/21.
 */
class MainNewFragment : Fragment() {


    companion object {
        val listOfTitle: Array<String> by lazy { App.instance.resources.getStringArray(R.array.news_channel_name_static) }
    }


    var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fra_main_new, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

        containerPager.adapter = mSectionsPagerAdapter

        slidingTabs.setupWithViewPager(containerPager)

        slidingTabs.tabMode = TabLayout.MODE_FIXED
        // val listOfTitle = view.ctx.resources.getStringArray(R.array.news_channel_name)
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): PlaceholderFragment {
            return PlaceholderFragment.newInstance(listOfTitle[position])
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

        /**
         * 刷新新闻条目时候根据数据源的不同，数据展示的位置也不同
         */
        val refurbish = { data: List<newListItem>, sources: dataSources ->
            activity.runOnUiThread {
                when (sources) {
                    dataSources.DATABASE -> {
                        log("从本地获取到:${data.size}条数据")
                        newListAdapter.addAll(data)
                    }
                    dataSources.NETWORK -> {
                        log("从网络获取到:${data.size}条数据")
                        newListAdapter.addAll(data, 0)
                    }
                }
            }
        }

        val newListAdapter = NewListAdapter {
            val timeFromeNew = Date().getTimeFromeNew(it.timeLong)
            log("timeFromeNew:$timeFromeNew-----System:${System.currentTimeMillis()}")
            log("it.ptime:${it.ptime}--it.timeLong:${it.timeLong}--it.publishtime:${it.publishtime}")
        }

        var startPage = 0
        var type = "list"
        var id = ""
        //var newLists=ArrayList<newListItem>()
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
            getData()
        }

        fun getData() {
            if(!refreshLayout.isRefreshing)refreshLayout.isRefreshing=true
            doAsync {
                if (convertFromNameToTypeAndId(arguments.getString(CHANNEL_NAME))) {
                    RequestNewListCommand(type, id, startPage, returnData = refurbish).execute()
                } else {
                    activity.toast("参数初始化错误")
                }
                uiThread {
                    if(refreshLayout.isRefreshing)refreshLayout.isRefreshing=false
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


        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val CHANNEL_NAME = "channel_name"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(channel: String): PlaceholderFragment {
                val fragment = PlaceholderFragment()
                val args = Bundle()
                args.putString(CHANNEL_NAME, channel)
                fragment.arguments = args
                return fragment
            }
        }
    }
}