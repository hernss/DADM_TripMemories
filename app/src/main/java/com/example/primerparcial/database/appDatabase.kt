package com.example.primerparcial.database

import android.content.Context
import androidx.room.*
import com.example.primerparcial.entities.UserDB
import com.example.primerparcial.entities.MemoryDB

@Database(entities = [UserDB::class, MemoryDB::class], version=1, exportSchema=false)
@TypeConverters(Converters::class)
public  abstract class appDatabase : RoomDatabase() {

    abstract fun userDao(): userDao
    abstract fun memoryDao(): memoryDao

    companion object {
        var INSTANCE: appDatabase? = null

        fun getAppDataBase(context: Context): appDatabase? {
            if (INSTANCE == null) {
                synchronized(appDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        appDatabase::class.java,
                        "memoriesDB"
                    ).allowMainThreadQueries().build() // No es lo mas recomendable que se ejecute en el mainthread
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}