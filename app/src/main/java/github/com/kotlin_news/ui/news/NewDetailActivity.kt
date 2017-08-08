package github.com.kotlin_news.ui.news

import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
import github.com.kotlin_news.App
import github.com.kotlin_news.R
import github.com.kotlin_news.domain.commands.RequestCommand
import github.com.kotlin_news.util.fromHtml
import github.com.kotlin_news.util.setImag
import kotlinx.android.synthetic.main.activity_new_detail.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

/**
 * Created by guoshuaijie on 2017/8/2.
 */
class NewDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_detail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        doAsync {
            val requestNewDetail = RequestCommand.requestNewDetail(intent.getStringExtra("id"))
            uiThread {
                newTitle.text=requestNewDetail.title
                newSource.text=requestNewDetail.sourceName
                newTime.text = requestNewDetail.ptime
                newContent.fromHtml(requestNewDetail.body)

                toolbarLayout.title = requestNewDetail.title
            }
        }
        val stringExtra = intent.getStringExtra("url")
        news_detail_photo_iv.setImag(stringExtra)
    }
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> false
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition()
        } else {
            finish()
        }
    }
}