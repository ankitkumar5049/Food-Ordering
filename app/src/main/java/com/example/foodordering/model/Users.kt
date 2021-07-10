package com.example.foodordering.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

data class Users(
    val name:String,
    val mobileNumber:String,
    val email : String,
    val address : String,
    val password : String
)