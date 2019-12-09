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
    val latitude = context.getString(R.string.Latitude)
    val longitude = context.getString(R.string.Longitude)
    override fun onCreate(query: SQLiteDatabase) {
        query.execSQL("CREATE TABLE IF NOT EXISTS saveinfo(ItemNo int(11) not null, " +
                "Latitude Double(36, 11), " +
                "Longitude Double(36, 11), " +
                "Distinction int(11) not null, " +
                "BuildAddress varchar(500), " +
                "DetailedPlace varchar(500), " +
                "ManagerTel varchar(50), " +
                "PRIMARY KEY(ItemNo, Distinction));")
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        //p0.execSQL("DROP TABLE IF EXISTS saveinfo")
        onCreate(p0)
    }

    fun refreshOfflineItems(itemsSaving: List<ItemInfo>){
        delDB()
        val sqlDB : SQLiteDatabase = this.writableDatabase
        onUpgrade(sqlDB, 1, 2)
        itemsSaving.forEach {  item ->
            Log.d("INSERT item", "넘버 : ${item.itemNo}, 계열번호 : ${item.itemDistinction}" +
                    ", 주소 : ${item.itemAttributes[address]}, 세부장소 : ${item.itemAttributes[place]}, 관리자 번호 : ${item.itemAttributes[tel]}")
            sqlDB.execSQL("INSERT INTO $OFFLINE_ITEMS_DATABASE_NAME " +
                    "(ItemNo, Latitude, Longitude, Distinction, BuildAddress, DetailedPlace, ManagerTel) " +
                    "VALUES('"
                    + item.itemNo + "','"
                    + item.itemLatitude + "','"
                    + item.itemLongitude + "','"
                    + item.itemDistinction + "','"
                    //+ item.itemAttributes[address]!!.replace("\'", "\'\'")!!.replace("\"", "\"\"")+"','"
                    //+ item.itemAttributes[place]!!.replace("\'", "\'\'")!!.replace("\"", "\"\"")+"','"
                    + item.itemAttributes[address]!!.replace("\'", "").replace("\"", "")
                    + item.itemAttributes[place]!!.replace("\'", "").replace("\"", "")
                    + item.itemAttributes[tel]+"');")

            Toast.makeText(context, "저장되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    fun loadOfflineItems() : List<ItemInfo>{
        val itemsOfflineSaved = mutableListOf<ItemInfo>()

        val sqlDB = this.writableDatabase
        onUpgrade(sqlDB, 1, 2)

        val cursor = sqlDB.rawQuery("SELECT * FROM $OFFLINE_ITEMS_DATABASE_NAME", null)
        while(cursor.moveToNext()){
            itemsOfflineSaved.add(ItemInfo(
                cursor.getInt(0),           //itemNo
                cursor.getDouble(1),        //itemLatitude
                cursor.getDouble(2),        //itemLongitude
                cursor.getInt(3),           //itemDistinction
                cursor.getString(4),        //itemBuildAddress
                cursor.getString(5),        //itemDetailedPlace
                cursor.getString(6)         //itemManagerTel
            ))
        }

        return itemsOfflineSaved
    }

    private fun delDB(){
        val mHelper = OfflineItemDBHelper(context)
        var sqlDB : SQLiteDatabase = mHelper.writableDatabase
        onUpgrade(sqlDB, 1, 2)
        sqlDB.execSQL("DELETE  FROM $OFFLINE_ITEMS_DATABASE_NAME")
    }
}