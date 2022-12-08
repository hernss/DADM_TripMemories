package com.example.primerparcial.database

import androidx.room.*
import com.example.primerparcial.entities.UserDB

@Dao
interface userDao {

    @Query("SELECT count(*) FROM users")
    fun getUserCount(): Int

    @Query("SELECT count(*) FROM users WHERE username = :username AND pass = :pass")
    fun checkUserAndPass(username : String, pass: String): Int

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    fun loadUserByUsername(username : String): UserDB?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    fun loadUserById(id : Int): UserDB?

    @Query("SELECT count(*) FROM users where username = :username")
    fun checkUsernameAvailable(username : String): Int

    @Query("UPDATE users SET is_logged_in = 0")
    fun logoutAllUsers()

    @Query("SELECT * FROM users WHERE is_logged_in == 1 LIMIT 1")
    fun getLogguedUser() : UserDB?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPerson(userDB:UserDB?)

    @Update
    fun updatePerson(userDB:UserDB?)

    @Delete
    fun delete(userDB:UserDB?)
}