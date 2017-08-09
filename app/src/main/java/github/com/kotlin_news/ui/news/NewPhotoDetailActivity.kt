package github.com.kotlin_news.ui.news

import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import github.com.kotlin_news.R
import github.com.kotlin_news.data.photoset
import github.com.kotlin_news.domain.commands.RequestCommand
import github.com.kotlin_news.util.PinchImageView
import github.com.kotlin_news.util.setImag
import kotlinx.android.synthetic.main.activity_new_photo_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*


/**
 * Created by guoshuaijie on 2017/8/8.
 */
class NewPhotoDetailActivity : AppCompatActivity() {
    lateinit var viewCache: LinkedList<PinchImageView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_photo_detail)
        viewCache = LinkedList<PinchImageView>()
        doAsync {
            val requestNewDetail = RequestCommand.requestNewPhotosDetail(intent.getStringExtra("id"))
            uiThread {
                viewPager.adapter=photoPageAdapterad(requestNewDetail)
            }
        }
    }

    internal inner class photoPageAdapterad(var bean: List<photoset>) : PagerAdapter() {

        override fun getCount(): Int = bean.size

        override fun isViewFromObject(view: View?, `object`: Any?): Boolean = view == `object`

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val piv: PinchImageView
            if (viewCache.size > 0) {
                piv = viewCache.remove()
                piv.reset()
            } else {
                //piv = PinchImageView(App.instance)
                piv = layoutInflater.inflate(R.layout.item_photo, container, false) as PinchImageView
                //piv.scaleType = ImageView.ScaleType.FIT_END
            }
            piv.setImag(bean[position].imgsrc)
            container.addView(piv)
            return piv
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any?) {
            val imageView = `object` as PinchImageView
            container.removeView(imageView)
            viewCache.add(imageView)
        }

        override fun setPrimaryItem(container: ViewGroup?, position: Int, `object`: Any?) {
            val imageView = `object` as PinchImageView
            imageView.setImag(bean[position].imgsrc)
        }
    }

}