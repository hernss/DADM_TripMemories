package com.example.primerparcial.database

import androidx.room.*
import com.example.primerparcial.entities.MemoryDB

@Dao
interface memoryDao {

    @Query("SELECT * FROM memories WHERE owner_id = :user_id ORDER BY date DESC")
    fun loadAllMemoriesFromUser(user_id : Int): MutableList<MemoryDB?>?

    @Query("SELECT * FROM memories WHERE id = :id LIMIT 1")
    fun loadMemoryFromId(id : Int): MemoryDB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemory(memoryDB:MemoryDB?)

    @Update
    fun updateMemory(memoryDB:MemoryDB?)

    @Delete
    fun delete(memoryDB:MemoryDB?)
}