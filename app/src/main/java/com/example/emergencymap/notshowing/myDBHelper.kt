package com.example.emergencymap.notshowing

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class myDBHelper(context : Context) : SQLiteOpenHelper(context, "SaveInfo", null, 1) {
    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL("CREATE TABLE saveinfo(ItemNo int(11) PRIMARY KEY, Distinction int(11) PRIMARY KEY, BuildAddress varchar(500), DetailedPlace varchar(500), ManagerTel varchar(50));")
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {
        p0.execSQL("DROP TABLE IF EXISTS saveinfo")
        onCreate(p0)
    }

}