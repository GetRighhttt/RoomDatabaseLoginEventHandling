package com.example.roomdatabaselogindemo.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Here we will actually create our database and return a Singleton
 * of that database each time.
 * @Database marks our class as an actual database.
 * Extending RoomDatabase() allows the class to be apart of Room, and seen as
 * Room.
 */

// Must provide the list of entity classes, version number, and
// set exportSchema to false
@Database(entities = [Subscriber::class], version = 1, exportSchema = false )
abstract class DatabaseForSubscribers: RoomDatabase() {

    abstract val subscriberDao: DAO

    // Should only use one instance of a Room Database for the entire App
    // This is why we create a singleton with a companion object
    companion object {
        // @Volatile = immediately visible to other threads
        @Volatile
        private var INSTANCE: DatabaseForSubscribers? = null


        // returns an Instance of TaskDatabase
        // builds the database if it doesn't exist
        fun getInstance(context: Context): DatabaseForSubscribers {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseForSubscribers::class.java,
                        "subscriber_database"
                    ).fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}