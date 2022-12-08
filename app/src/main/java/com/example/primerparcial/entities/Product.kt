package com.example.primerparcial.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Product(
    var partNumber : String,
    var brand : String,
    var type : String,
    var description : String,
    var urlImage : String,
    var price : String) : Parcelable{


}