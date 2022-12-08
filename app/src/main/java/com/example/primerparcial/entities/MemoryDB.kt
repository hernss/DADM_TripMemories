package com.example.primerparcial.entities

import androidx.room.*
import java.util.*

@Entity(tableName = "memories")
class MemoryDB(
    id : Int,
    owner_id : Int,
    country : String,
    city : String,
    description : String,
    date : Date,
    urlImage: String,
    latitud : Double,
    longitud : Double
    ) {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id : Int

    @ColumnInfo(name="owner_id")
    var owner_id : Int

    @ColumnInfo(name = "country")
    var country : String

    @ColumnInfo(name = "city")
    var city : String

    @ColumnInfo(name = "description")
    var description : String

    @ColumnInfo(name = "date")
    var date : Date

    @ColumnInfo(name = "urlImage")
    var urlImage  : String

    @ColumnInfo(name = "latitud")
    var latitud : Double

    @ColumnInfo(name = "longitud")
    var longitud : Double

    init {
        this.id = id
        this.owner_id = owner_id
        this.country = country
        this.city = city
        this.description = description
        this.date=date
        this.urlImage = urlImage
        this.latitud = latitud
        this.longitud = longitud
    }

}
