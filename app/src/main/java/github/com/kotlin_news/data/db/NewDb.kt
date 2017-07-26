package github.com.kotlin_news.data.db

import android.text.TextUtils
import github.com.kotlin_news.App
import github.com.kotlin_news.data.newListItem
import github.com.kotlin_news.data.photoset
import github.com.kotlin_news.domain.datasource.NewDataSource
import github.com.kotlin_news.util.isNullOrEmpty
import github.com.kotlin_news.util.log
import github.com.kotlin_news.util.parseList
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import java.lang.Exception
import java.util.ArrayList

/**
 * Created by guoshuaijie on 2017/7/25.
 */
class NewDb(val newdbHelper: NewDbHelper = NewDbHelper()) : NewDataSource {
    override fun requestNewList(type: String, id: String, startPage: Int): List<newListItem>? {
        log("开始从本地数据库获取数据..")
        var newList = ArrayList<newListItem>()
        newdbHelper.use {
            select(id).whereSimple("(_id >= ?) and (_id < ?)", startPage.toString(), (startPage + 22).toString())
                    .parseList {
                        val item = newListItem(it[newListTable.postid].toString(),
                                it[newListTable.title].toString(),
                                it[newListTable.digest].toString(),
                                it[newListTable.imgsrc].toString(),
                                it[newListTable.ptime].toString(), null)
                        newList.add(item)
                    }
            newList.forEach {
                if(it.digest.isNullOrEmpty()){
                    var photoList = ArrayList<photoset>()
                    select(newListPhotoSetTable.NAME).whereSimple("${newListPhotoSetTable.postid} = ?",it.postid)
                            .parseList{
                                val item = photoset(it[newListPhotoSetTable.title].toString(),
                                        it[newListPhotoSetTable.imgsrc].toString())
                                photoList.add(item)
                            }
                    it.ads=photoList
                }
            }
        }
        if(!newList.isNullOrEmpty())log("本地获取数据成功")else log("本地获取数据失败")
        return newList
    }

    fun saveNewList(result: List<newListItem>, channlId: String) = newdbHelper.use {
        log("固化数据到本地数据库..")
        try {
            beginTransaction()
            result.forEach {
               val ins = insert(channlId,
                        newListTable.title to it.title,
                        newListTable.ptime to it.ptime,
                        newListTable.digest to it.digest,
                        newListTable.imgsrc to it.imgsrc,
                        newListTable.postid to it.postid)
                if (TextUtils.isEmpty(it.digest)) {
                    val outIt = it
                    it.ads?.forEach {
                        insert(newListPhotoSetTable.NAME,
                                newListPhotoSetTable.postid to outIt.postid,
                                newListPhotoSetTable.title to it.title,
                                newListPhotoSetTable.imgsrc to it.imgsrc)
                    }
                }
                log("数据插入结果:$ins")
            }
            setTransactionSuccessful()
            log("数据固化成功")
        } catch (e: Exception) {
            App.instance.toast("数据固化出错")
            insert(errorTable.NAME,
                    errorTable.msg to "数据固化出错",
                    errorTable.meta to result.toString())
        }finally {
            endTransaction()
        }
    }

}