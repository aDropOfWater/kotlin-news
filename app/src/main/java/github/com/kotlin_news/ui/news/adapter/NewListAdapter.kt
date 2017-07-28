package github.com.kotlin_news.ui.news.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.kotlin_news.R
import github.com.kotlin_news.data.newListItem
import github.com.kotlin_news.util.*
import kotlinx.android.synthetic.main.item_new_image_text.view.*
import kotlinx.android.synthetic.main.item_new_image.view.*

/**
 * Created by guoshuaijie on 2017/7/21.
 * 新闻列表适配器
 */
class NewListAdapter(val itemClick: (newListItem) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var newList = ArrayList<newListItem>()
    /**
     * 图文item
     */
    val TYPE_IMAGE_TEXT = 0
    /**
     * 图片item
     */
    val TYPE_IMAGE = 1

    /**
     * 底部的加载更多
     */
    val TYPE_FOOTER = 2

    fun addAll(elements: List<newListItem>, location: Int = newList.size) {
        val filter = elements.filter { !newList.contains(it) }
        val addAll = newList.addAll(location, filter)
        log("数据添加${if (addAll) "成功" else "失败"}  现在有${newList.size}条数据")
        notifyItemRangeInserted(location, filter.size)
    }

    override fun getItemViewType(position: Int) = when (position) {
        in 0 until  newList.size -> if (newList[position].ads.isNullOrEmpty()) TYPE_IMAGE_TEXT else TYPE_IMAGE
        newList.size -> TYPE_FOOTER
        else -> TYPE_FOOTER
    }


    override fun getItemCount() = if (!newList.isNullOrEmpty()) newList.size +1 else 0

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageTextViewHolder -> holder.bindNewItem(newList[position])
            is ImageViewHolder -> holder.bindNewItem(newList[position])
            is FooterViewHolder -> holder
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            TYPE_IMAGE_TEXT -> {
                val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_new_image_text, parent, false)
                return ImageTextViewHolder(view, itemClick)
            }
            TYPE_IMAGE -> {
                val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_new_image, parent, false)
                return ImageViewHolder(view, itemClick)
            }
            TYPE_FOOTER -> {
                val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_footer, parent, false)
                return FooterViewHolder(view, itemClick)
            }
            else -> {
                val view = LayoutInflater.from(parent.ctx).inflate(R.layout.item_new_image_text, parent, false)
                return ImageTextViewHolder(view, itemClick)
            }
        }
    }


    class ImageTextViewHolder(view: View, val itemClick: (newListItem) -> Unit) : RecyclerView.ViewHolder(view) {
        fun bindNewItem(newListItem: newListItem) {
            with(itemView) {
                newImage.setImag(newListItem.imgsrc)
                newTitle.text = newListItem.title
                newTime.text = newListItem.publishtime
                itemView.setOnClickListener { itemClick(newListItem) }
            }
        }
    }

    class ImageViewHolder(view: View, val itemClick: (newListItem) -> Unit) : RecyclerView.ViewHolder(view) {
        val PhotoThreeHeight = JavaUtil.dip2px(90)
        val PhotoTwoHeight = JavaUtil.dip2px(120)
        val PhotoOneHeight = JavaUtil.dip2px(150)

        fun bindNewItem(newListItem: newListItem) {
            with(itemView) {
                newListItem.ads?.let {
                    val layoutParams = llImageGroup.layoutParams
                    when (it.size) {
                        0 -> {
                            itemView.visibility = View.GONE
                        }
                        1 -> {
                            itemView.visibility = View.VISIBLE
                            layoutParams.height = PhotoOneHeight
                            newImageLeft.setImag(it[0].imgsrc)
                        }
                        2 -> {
                            itemView.visibility = View.VISIBLE
                            layoutParams.height = PhotoTwoHeight
                            newImageLeft.setImag(it[0].imgsrc)
                            newImageMiddle.setImag(it[1].imgsrc)
                        }
                        else -> {
                            itemView.visibility = View.VISIBLE
                            layoutParams.height = PhotoThreeHeight
                            newImageLeft.setImag(it[0].imgsrc)
                            newImageMiddle.setImag(it[1].imgsrc)
                            newImageRight.setImag(it[2].imgsrc)
                        }
                    }
                    llImageGroup.layoutParams = layoutParams
                    newImagTitle.text = newListItem.title
                    newImagTime.text = newListItem.publishtime
                }
            }
        }
    }

    class FooterViewHolder(view: View, val itemClick: (newListItem) -> Unit) : RecyclerView.ViewHolder(view)
}