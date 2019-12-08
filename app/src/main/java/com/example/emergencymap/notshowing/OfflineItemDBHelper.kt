package com.example.emergencymap.notshowing

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.emergencymap.ItemInfo
import com.example.emergencymap.R

class OfflineItemDBHelper(private val context : Context)
    : SQLiteOpenHelper(context, "SaveInfo", null, 1) {
    val address = context.resources.getString(R.string.BuildAddress)
    val place = context.resources.getString(R.string.DetailedPlace)
    val tel = context.resources.getString(R.string.ManagerTel)
    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL("CREATE TABLE saveinfo(ItemNo int(11) not null, Distinction int(11) not null, BuildAddress varchar(500), DetailedPlace varchar(500), ManagerTel varchar(50), PRIMARY KEY(ItemNo, Distinction));")
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        p0.execSQL("DROP TABLE IF EXISTS saveinfo")
        onCreate(p0)
    }

    fun doSomething(itemsSaving: List<ItemInfo>){
        delDB()
        val mHelper = OfflineItemDBHelper(context)
        var sqlDB : SQLiteDatabase = mHelper.writableDatabase
        onUpgrade(sqlDB, 1, 2)
        itemsSaving.forEach {  item ->
            Log.d("Test!", "넘버 : ${item.itemNo}, 계열번호 : ${item.itemDistinction}" +
                    ", 주소 : ${item.itemAttributes[address]}, 세부장소 : ${item.itemAttributes[place]}, 관리자 번호 : ${item.itemAttributes[tel]}")
            sqlDB.execSQL("INSERT INTO saveinfo VALUES('"
                    + item.itemNo+"','"
                    +item.itemDistinction+"','"
                    +item.itemAttributes[address]+"','"
                    +item.itemAttributes[place]+"','"
                    +item.itemAttributes[tel]+"');")

            Toast.makeText(context, "완", Toast.LENGTH_SHORT).show()
        }
    }

    fun delDB(){
        val mHelper = OfflineItemDBHelper(context)
        var sqlDB : SQLiteDatabase = mHelper.writableDatabase
        onUpgrade(sqlDB, 1, 2)
        sqlDB.execSQL("DELETE  FROM saveinfo")
    }


}