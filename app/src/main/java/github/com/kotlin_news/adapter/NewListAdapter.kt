package github.com.kotlin_news.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.kotlin_news.R
import github.com.kotlin_news.util.ctx
import github.com.kotlin_news.data.newListItem
import github.com.kotlin_news.util.setImag
import kotlinx.android.synthetic.main.item_new.view.*

/**
 * Created by guoshuaijie on 2017/7/21.
 * 新闻列表适配器
 */
class NewListAdapter(val newList: List<newListItem>, val itemClick: (newListItem) -> Unit) : RecyclerView.Adapter<NewListAdapter.ViewHolder>() {

    override fun getItemCount(): Int {
        return newList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindNewItem(newList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_new, parent, false)
        return ViewHolder(view,itemClick)
    }


    class ViewHolder(view: View, val itemClick: (newListItem) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindNewItem(newListItem: newListItem) {
            with(itemView){
                newImage.setImag(newListItem.imgsrc)
                newTitle.text=newListItem.title
                newTime.text=newListItem.publishtime
                itemView.setOnClickListener { itemClick(newListItem) }
            }
        }
    }
}