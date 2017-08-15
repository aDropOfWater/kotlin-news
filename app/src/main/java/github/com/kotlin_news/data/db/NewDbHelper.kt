package github.com.kotlin_news.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import github.com.kotlin_news.App
import github.com.kotlin_news.R
import org.jetbrains.anko.collections.forEachByIndex
import org.jetbrains.anko.collections.forEachWithIndex
import org.jetbrains.anko.db.*

/**
 * Created by guoshuaijie on 2017/7/25.
 */
class NewDbHelper(ctx: Context = App.instance) : ManagedSQLiteOpenHelper(ctx,
        NewDbHelper.DB_NAME, null, NewDbHelper.DB_VERSION) {
    companion object {
        val DB_NAME = "KotlinNews.db"
        val DB_VERSION = 2
        val instance by lazy { NewDbHelper() }
        //val tableNames = listOf("T1348647909107", "T1348649580692", "T1348648756099", "T1348648141035", "T1348649079062")
    }


    override fun onCreate(db: SQLiteDatabase) {
        /**
         * 新闻频道列表数据库
         */
        db.createTable(newChannelsTable.NAME, true,
                newChannelsTable.ID to INTEGER + PRIMARY_KEY,
                newChannelsTable.channelName to TEXT + UNIQUE,
                newChannelsTable.channelId to TEXT,
                newChannelsTable.channelSelect to TEXT,
                newChannelsTable.editAble to TEXT,
                newChannelsTable.index to INTEGER)
        //初始化频道列表，才能根据列表创建下面的新闻频道数据库
        val notEditChannelNames = App.instance.resources.getStringArray(R.array.news_channel_name_static)
        val notEditChannelIds = App.instance.resources.getStringArray(R.array.news_channel_id_static)
        if (notEditChannelNames.size == notEditChannelIds.size) {
            notEditChannelNames.forEachWithIndex { position, value ->
                db.insert(newChannelsTable.NAME,
                        newChannelsTable.channelName to value,
                        newChannelsTable.editAble to "0",
                        newChannelsTable.channelSelect to "1",
                        newChannelsTable.channelId to notEditChannelIds[position],
                        newChannelsTable.index to position)
            }
        }
        val EditChannelNames = App.instance.resources.getStringArray(R.array.news_channel_name)
        val EditChannelIds = App.instance.resources.getStringArray(R.array.news_channel_id)
        if (EditChannelNames.size == EditChannelIds.size) {
            EditChannelNames.forEachWithIndex { position, value ->
                db.insert(newChannelsTable.NAME,
                        newChannelsTable.channelName to value,
                        newChannelsTable.editAble to "1",
                        newChannelsTable.channelSelect to "0",
                        newChannelsTable.channelId to EditChannelIds[position],
                        newChannelsTable.index to notEditChannelNames.size + position)
            }
        }
        val tableNames = notEditChannelIds + EditChannelIds
        /**
         * 每个新闻频道建立对应的一张表
         */
        tableNames.forEach {
            db.createTable(it, true,
                    newListTable.ID to INTEGER + PRIMARY_KEY,
                    newListTable.postid to TEXT + UNIQUE + NOT_NULL,
                    newListTable.digest to TEXT,
                    newListTable.imgsrc to TEXT,
                    newListTable.ptime to TEXT,
                    newListTable.title to TEXT)
        }

        /**
         * 图集数据库
         */
        db.createTable(newListPhotoSetTable.NAME, true,
                newListPhotoSetTable.ID to INTEGER + PRIMARY_KEY,
                newListPhotoSetTable.skipID to TEXT + UNIQUE,
                newListPhotoSetTable.postid to TEXT,
                newListPhotoSetTable.imgsrc to TEXT,
                newListPhotoSetTable.title to TEXT)

        /**
         * 新闻详情数据库
         */
        db.createTable(newDetailTable.NAME, true,
                newDetailTable.ID to INTEGER + PRIMARY_KEY,
                newDetailTable.postid to TEXT + UNIQUE,
                newDetailTable.body to TEXT,
                newDetailTable.ptime to TEXT,
                newDetailTable.sourceIconUrl to TEXT,
                newDetailTable.sourceName to TEXT,
                newDetailTable.title to TEXT)

        /**
         * 报错信息数据库
         */
        db.createTable(errorTable.NAME, true,
                errorTable.ID to INTEGER + PRIMARY_KEY,
                errorTable.msg to TEXT,
                errorTable.meta to TEXT,
                errorTable.remark to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onCreate(db)
    }
}