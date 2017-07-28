package github.com.kotlin_news.ui.news

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.kotlin_news.App
import github.com.kotlin_news.R
import kotlinx.android.synthetic.main.fra_main_new.*


/**
 * Created by guoshuaijie on 2017/7/21.
 */
class MainNewFragment : Fragment() {

    companion object {
        var listOfTitle: Array<String> = App.instance.resources.getStringArray(R.array.news_channel_name_static)
        lateinit var fragmentList: List<NewListFragment>
    }


    var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fra_main_new, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val first = listOfTitle[0]
        fragmentList = listOfTitle.map { NewListFragment.newInstance(it, it == first) }

        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

        containerPager.adapter = mSectionsPagerAdapter

        slidingTabs.setupWithViewPager(containerPager)

        slidingTabs.tabMode = TabLayout.MODE_FIXED

        containerPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                fragmentList[position].firstRefresh()
            }
        })
        activity.toolbar.title="新闻"
    }


    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): NewListFragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return listOfTitle.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return listOfTitle[position]
        }
    }
    
}