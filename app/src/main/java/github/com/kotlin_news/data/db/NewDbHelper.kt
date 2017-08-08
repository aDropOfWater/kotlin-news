package github.com.kotlin_news.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import github.com.kotlin_news.App
import org.jetbrains.anko.db.*

/**
 * Created by guoshuaijie on 2017/7/25.
 */
class NewDbHelper(ctx: Context = App.instance) : ManagedSQLiteOpenHelper(ctx,
        NewDbHelper.DB_NAME, null, NewDbHelper.DB_VERSION) {
    companion object {
        val DB_NAME = "KotlinNews.db"
        val DB_VERSION = 1
        val instance by lazy { NewDbHelper() }
        val tableNames = listOf("T1348647909107","T1348649580692","T1348648756099","T1348648141035","T1348649079062")
    }


    override fun onCreate(db: SQLiteDatabase) {

        /**
         * 每个新闻频道建立对应的一张表
         */
        tableNames.forEach {
            db.createTable(it, true,
                    newListTable.ID to INTEGER + PRIMARY_KEY,
                    newListTable.postid to TEXT + UNIQUE,
                    newListTable.digest to TEXT,
                    newListTable.imgsrc to TEXT,
                    newListTable.ptime to TEXT,
                    newListTable.title to TEXT)
        }

        db.createTable(newListPhotoSetTable.NAME, true,
                newListPhotoSetTable.ID to INTEGER + PRIMARY_KEY,
                newListPhotoSetTable.skipID to TEXT + UNIQUE,
                newListPhotoSetTable.postid to TEXT,
                newListPhotoSetTable.imgsrc to TEXT,
                newListPhotoSetTable.title to TEXT)

        db.createTable(newDetailTable.NAME,true,
                newDetailTable.ID to INTEGER + PRIMARY_KEY,
                newDetailTable.postid to TEXT + UNIQUE,
                newDetailTable.body to TEXT,
                newDetailTable.ptime to TEXT,
                newDetailTable.sourceIconUrl to TEXT,
                newDetailTable.sourceName to TEXT,
                newDetailTable.title to TEXT)

        db.createTable(errorTable.NAME,true,
                errorTable.ID to INTEGER + PRIMARY_KEY,
                errorTable.msg to TEXT,
                errorTable.meta to TEXT,
                errorTable.remark to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }
}