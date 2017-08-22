package github.com.kotlin_news.ui.news.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.kotlin_news.R
import github.com.kotlin_news.data.newChannel
import github.com.kotlin_news.util.ctx
import kotlinx.android.synthetic.main.item_news_channel.view.*

/**
 * Created by guoshuaijie on 2017/8/17.
 */
class NewChannelAdapter(val newList: ArrayList<newChannel>,
                        val itemClick: (Int, View, newChannel) -> Unit
) : RecyclerView.Adapter<NewChannelAdapter.channelViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): channelViewHolder {
        val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_news_channel, parent, false)
        return channelViewHolder(view, itemClick)
    }

    fun remove(position: Int): newChannel {
        val old = newList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(0, itemCount)
        return old
    }

    fun add(channel: newChannel, index: Int) {
        if (index < 0) {
            newList.add(channel)
            notifyItemInserted(itemCount)
        } else {
            newList.add(index, channel)
            notifyItemInserted(index)
        }
        notifyItemRangeChanged(0, itemCount)
    }

    override fun onBindViewHolder(holder: channelViewHolder, position: Int) {
        holder.bindNewItem(newList[position], position)
    }

    //var newList = ArrayList<newChannel>()
    override fun getItemCount(): Int = newList.size


    class channelViewHolder(view: View, val itemClick: (Int, View, newChannel) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindNewItem(channel: newChannel, position: Int) {
            itemView.setOnClickListener { v -> itemClick(position, v, channel) }
            with(itemView) {
                newsChannelTv.text = channel.channelName
                newsChannelTv.setTextColor(if (channel.editAble) ContextCompat.getColor(ctx, R.color.gray_deep) else ContextCompat.getColor(ctx, R.color.gray))
            }
        }
    }
}