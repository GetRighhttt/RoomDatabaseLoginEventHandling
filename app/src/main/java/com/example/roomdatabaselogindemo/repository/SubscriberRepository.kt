package com.example.roomdatabaselogindemo.repository

import com.example.roomdatabaselogindemo.model.database.DAO
import com.example.roomdatabaselogindemo.model.database.Subscriber

/**
 * The purpose of the repository class is to act like a mediator between
 * various data sources: API, Databases, Network, etc.
 * It's necessary for MVVM architectural design.
 * To Provide a clean API for ViewModels to easily get and send data.
 * Typically defines the methods from the database we need to use.
 */

// Adding Dao as a constructor because we are going to call their functions
class SubscriberRepository(private val dao: DAO) {

    /**
     * Here we have to get all the subscribers in order for us to define methods
     * needed for our ViewModel business logic.
     *
     * The repository usually doesn't includes Logic. Just define methods.
     *
     * We return a value here so we can use it for validation of events,
     * and letting the user know when the database has been updated.
     * We will see it more in the ViewModel.
      */
    val subscribers = dao.getAllSubscribers()

    // defining method to insert into our database
    suspend fun insert(subscriber: Subscriber): Long {
        return dao.addSubscriber(subscriber)
    }

    // defining a method to update to our database
    suspend fun update(subscriber: Subscriber): Int {
        return dao.updateSubscriber(subscriber)
    }

    // defining a method to delete from our database
    suspend fun delete(subscriber: Subscriber): Int {
        return dao.deleteSubscriber(subscriber)
    }

    // defining a method to delete all subscriber data
    suspend fun deleteAll(): Int {
        return dao.deleteAllRows()
    }

}