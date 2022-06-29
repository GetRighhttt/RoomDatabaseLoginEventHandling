package com.example.roomdatabaselogindemo.model.database

import androidx.lifecycle.LiveData
import androidx.room.*

/*
This interface will be used to define the methods that we need for our
Room database.
 */

// @Dao = Data access object
// what we are going to use throughout our App to access these methods
@Dao
interface DAO {

    /**
     * Methods used to insert, delete, and update the subscriber.
     * We suspend these to launch them later on as Coroutines.
     * And also to use these in the background.
     * onConflictStrategy will try to insert, and if there's an existing
     * row, it will replace it.
     *
     * We're returning a value for our DAO for validation purposes.
     * Essentially have to return something so we can know inform the user
     * when something has been updated, deleted, or inserted into the database.
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSubscriber(subscriber: Subscriber): Long

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber): Int

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber): Int

    // Used to delete all or a select item with @Query annotation
    // Verified at compile time
    @Query("DELETE FROM subscriber_table")
    suspend fun deleteAllRows(): Int

    // Returns all. No need to suspend, because it returns LiveData
    @Query("SELECT * FROM subscriber_table")
    fun getAllSubscribers(): LiveData<List<Subscriber>>
}