package github.com.kotlin_news.ui.news

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.kotlin_news.R
import github.com.kotlin_news.data.newChannel
import github.com.kotlin_news.domain.commands.RequestCommand
import github.com.kotlin_news.util.log
import kotlinx.android.synthetic.main.fra_main_new.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


/**
 * Created by guoshuaijie on 2017/7/21.
 */
class MainNewFragment : Fragment() {

    lateinit var oldFragmentList: List<NewListFragment>
    var listOfChannel: List<newChannel> by Delegates.observable(RequestCommand.requestNewChannelList(true)) { _, old, new ->
        if (old != new) {
            val newFragmentList = ArrayList<NewListFragment>()
            new.forEach {
                val fragment = if (it in old) {
                    oldFragmentList[old.indexOf(it)]
                } else {
                    NewListFragment.newInstance(it)
                }
                newFragmentList.add(fragment)
            }
            log("old oldFragmentList=$oldFragmentList")
            oldFragmentList = newFragmentList
            log("new oldFragmentList=$oldFragmentList")
            mSectionsPagerAdapter?.notifyDataSetChanged()
        }
    }


    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fra_main_new, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        log("获取到的频道列表$listOfChannel")
        oldFragmentList = listOfChannel.map { NewListFragment.newInstance(it) }

        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

        containerPager.adapter = mSectionsPagerAdapter

        slidingTabs.setupWithViewPager(containerPager)

        slidingTabs.tabMode = TabLayout.MODE_SCROLLABLE

        containerPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                oldFragmentList[position].firstRefresh()
            }
        })
        activity.toolbar.title = "新闻"

        ivAddChannel.setOnClickListener { startActivity(Intent(activity, NewChannelActivity::class.java)) }
    }

    override fun onResume() {
        super.onResume()
        listOfChannel = RequestCommand.requestNewChannelList(true)
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): NewListFragment = oldFragmentList[position]

        override fun getCount(): Int = listOfChannel.size

        override fun getPageTitle(position: Int): CharSequence? =
                listOfChannel[position].channelName
    }

}