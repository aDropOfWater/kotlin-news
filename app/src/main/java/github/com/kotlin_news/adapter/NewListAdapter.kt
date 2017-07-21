package github.com.kotlin_news.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import github.com.kotlin_news.data.newListItem

/**
 * Created by guoshuaijie on 2017/7/21.
 * 新闻列表适配器
 */
class NewListAdapter(val newList: ArrayList<newListItem>, val itemClick: (newListItem) -> Unit): RecyclerView.Adapter<NewListAdapter.ViewHolder>() {
    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    }
}