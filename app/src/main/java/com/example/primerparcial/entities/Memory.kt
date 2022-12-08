package com.example.primerparcial.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
class Memory(
    var id : Int,
    var owner_id : Int,
    var country : String,
    var city : String,
    var description : String,
    var date : Date,
    var urlImage: String,
    var latitud : Double,
    var longitud : Double,
) : Parcelable{

    constructor (memory : MemoryDB) : this(
        memory.id,
        memory.owner_id,
        memory.country,
        memory.city,
        memory.description,
        memory.date,
        memory.urlImage,
        memory.latitud,
        memory.longitud
    ){
    }

    fun parseFromMemoryDB(memory : MemoryDB){
        this.id = memory.id
        this.owner_id = memory.owner_id
        this.country = memory.country
        this.city = memory.city
        this.description = memory.description
        this.date = memory.date
        this.urlImage = memory.urlImage
        this.latitud = memory.latitud
        this.longitud = memory.longitud
    }
}