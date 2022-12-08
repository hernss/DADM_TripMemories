package com.example.primerparcial.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class User (
    var id : Int,
    var username : String,
    var name : String,
    var lastname : String,
    var email : String,
    var pass : String,
    var phone : String,
    var last_logged_in : Date,
    var account_created_at : Date,
    var is_logged_in : Int,
    var email_code : Int,
    var activated : Int
    ) : Parcelable{

    constructor(user_db: UserDB) : this(
            user_db.id,
            user_db.username,
            user_db.name,
            user_db.lastname,
            user_db.email,
            user_db.pass,
            user_db.phone,
            user_db.last_logged_in,
            user_db.account_created_at,
            user_db.is_logged_in,
            user_db.email_code,
            user_db.activated) {

    }

        fun parseFromUserDB(user : UserDB){
            this.id = user.id
            this.username = user.username
            this.name = user.name
            this.lastname = user.lastname
            this.email = user.email
            this.pass = user.pass
            this.phone = user.phone
            this.last_logged_in = user.last_logged_in
            this.account_created_at = user.account_created_at
            this.is_logged_in = user.is_logged_in
            this.email_code = user.email_code
            this.activated = user.activated
        }
}