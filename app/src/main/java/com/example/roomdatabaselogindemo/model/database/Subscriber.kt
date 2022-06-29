package com.example.roomdatabaselogindemo.model.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This will be our Table we are going to use for our Room Datbase.
 */

//@Entity creates the actual table and creates a table name
@Entity(tableName = "subscriber_table")
data class Subscriber(

    // Our id is going to be our primary key
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "Id")
    var id: Int,

    // @ColumnInfo creates columns
    @ColumnInfo(name = "Name")
    var name: String,

    @ColumnInfo(name = "Email")
    var email: String
)
