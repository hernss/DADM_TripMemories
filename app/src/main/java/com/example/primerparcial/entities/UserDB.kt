package com.example.primerparcial.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "users")
class UserDB (
    id : Int,
    username : String,
    name : String,
    lastname : String,
    email : String,
    pass : String,
    phone : String,
    last_logged_in : Date,
    account_created_at : Date,
    is_logged_in : Int,
    email_code : Int,
    activated : Int
    ) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id : Int

    @ColumnInfo(name = "username")
    var username : String

    @ColumnInfo(name = "name")
    var name : String

    @ColumnInfo(name = "lastname")
    var lastname : String

    @ColumnInfo(name = "email")
    var email : String

    @ColumnInfo(name = "pass")
    var pass : String

    @ColumnInfo(name = "phone")
    var phone : String

    @ColumnInfo(name = "last_logged_in")
    var last_logged_in : Date

    @ColumnInfo(name = "account_created_at")
    var account_created_at : Date

    @ColumnInfo(name = "is_logged_in")
    var is_logged_in : Int

    @ColumnInfo(name = "email_code")
    var email_code : Int

    @ColumnInfo(name = "activated")
    var activated : Int

    init {
        this.id = id
        this.username = username
        this.name = name
        this.lastname = lastname
        this.email = email
        this.pass = pass
        this.phone = phone
        this.last_logged_in = last_logged_in
        this.account_created_at = account_created_at
        this.is_logged_in = is_logged_in
        this.email_code = email_code
        this.activated = activated
    }
}