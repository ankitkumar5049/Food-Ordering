package com.example.foodordering.activity

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context?) : SQLiteOpenHelper(context, "LoginSignUp.db", null, 1) {
    override fun onCreate(MyDB: SQLiteDatabase) {
        MyDB.execSQL("CREATE TABLE users(username TEXT PRIMARY KEY, email TEXT,mobile TEXT, address TEXT,password TEXT)")
    }

    override fun onUpgrade(MyDB: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        MyDB.execSQL("drop table if exists users")
    }

    fun insertData(
        username: String?,
        email: String?,
        mobile: String?,
        address: String?,
        password: String?
    ): Boolean {
        val MyDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("username", username)
        contentValues.put("email", email)
        contentValues.put("mobile", mobile)
        contentValues.put("address", address)
        contentValues.put("password", password)
        val results = MyDB.insert("users", null, contentValues)
        return results != -1L
    }

    fun checkRegisteredUser(mobile: String, email: String): Boolean {
        val myDb = this.writableDatabase
        val cursor = myDb.rawQuery(
            "Select * from users where username = ? or email = ?",
            arrayOf(mobile, email)
        )
        return cursor.count > 0
    }



    fun checkUserMobilePassword(mobile: String, password: String): Boolean {
        val myDb = this.writableDatabase
        val cursor = myDb.rawQuery(
            "Select * from users where mobile = ? and password = ?",
            arrayOf(mobile, password)
        )
        return cursor.count > 0
    }

    fun forgotUserPassword(mobile: String, email: String): Boolean {
        val myDb = this.writableDatabase
        val cursor = myDb.rawQuery(
            "Select * from users where mobile = ? and email = ?",
            arrayOf(mobile, email)
        )
        return cursor.count > 0
    }

    companion object {
        const val Dbname = "LoginSignUp.db"
    }
}