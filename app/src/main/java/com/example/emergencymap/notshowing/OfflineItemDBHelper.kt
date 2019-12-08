package com.example.emergencymap.notshowing

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.emergencymap.ItemInfo
import com.example.emergencymap.R

const val OFFLINE_ITEMS_DATABASE_NAME = "SaveInfo"

class OfflineItemDBHelper(private val context : Context)
    : SQLiteOpenHelper(context, OFFLINE_ITEMS_DATABASE_NAME, null, 1) {
    val address = context.resources.getString(R.string.BuildAddress)
    val place = context.resources.getString(R.string.DetailedPlace)
    val tel = context.resources.getString(R.string.ManagerTel)
    override fun onCreate(query: SQLiteDatabase) {
        query.execSQL("CREATE TABLE saveinfo(ItemNo int(11) not null, Distinction int(11) not null, BuildAddress varchar(500), DetailedPlace varchar(500), ManagerTel varchar(50), PRIMARY KEY(ItemNo, Distinction));")
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        p0.execSQL("DROP TABLE IF EXISTS saveinfo")
        onCreate(p0)
    }

    fun refreshOfflineItems(itemsSaving: List<ItemInfo>){
        delDB()
        val mHelper = OfflineItemDBHelper(context)
        var sqlDB : SQLiteDatabase = mHelper.writableDatabase
        onUpgrade(sqlDB, 1, 2)
        itemsSaving.forEach {  item ->
            Log.d("INSERT item", "넘버 : ${item.itemNo}, 계열번호 : ${item.itemDistinction}" +
                    ", 주소 : ${item.itemAttributes[address]}, 세부장소 : ${item.itemAttributes[place]}, 관리자 번호 : ${item.itemAttributes[tel]}")
            sqlDB.execSQL("INSERT INTO $OFFLINE_ITEMS_DATABASE_NAME VALUES('"
                    + item.itemNo+"','"
                    +item.itemDistinction+"','"
                    +item.itemAttributes[address]+"','"
                    +item.itemAttributes[place]+"','"
                    +item.itemAttributes[tel]+"');")

            Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun delDB(){
        val mHelper = OfflineItemDBHelper(context)
        var sqlDB : SQLiteDatabase = mHelper.writableDatabase
        onUpgrade(sqlDB, 1, 2)
        sqlDB.execSQL("DELETE  FROM $OFFLINE_ITEMS_DATABASE_NAME")
    }


}