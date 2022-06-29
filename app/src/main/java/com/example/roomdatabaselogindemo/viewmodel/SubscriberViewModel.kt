package com.example.roomdatabaselogindemo.viewmodel

import android.provider.SyncStateContract.Helpers.update
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.roomdatabaselogindemo.model.database.Subscriber
import com.example.roomdatabaselogindemo.repository.SubscriberRepository
import kotlinx.coroutines.launch
import java.nio.file.Files.delete

/**
 * ViewModel class to incorporate business logic and save the configuration
 * state of the app when it changes.
 * In this scenario, because we have a repository, we will pass that as a
 * parameter to implement the methods needed from our DAO class.
 * Those methods were created and defined in our repository.
 */

class SubscriberViewModel(private val repository: SubscriberRepository): ViewModel() {

    // gain access to repositories properties and methods
    val subscribers = repository.subscribers

    // Two variables used to update and delete
    private var isUpdateOrDelete: Boolean = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber

    // MutableLiveData variables we will use for DataBinding
    val inputName = MutableLiveData<String?>("")
    val inputEmail = MutableLiveData<String?>("")
    val saveButton = MutableLiveData<String>("")
    val deleteButton = MutableLiveData<String>("")

    // Mutable LiveData backing property for the status message
    private val _statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
    get() = _statusMessage

    init {
        saveButton.value = "Save"
        deleteButton.value = "Clear All"
    }

    /**
     * Below we will show an example of the save or update function.
     *
     * We will also show how we can validate an email address using the Patters and
     * matcher class.
     * Validation is highly important in software development for testing and
     * to ensure the overall security of your applications.
     */
    fun saveOrUpdate() {

        if(inputName.value == null) {
            _statusMessage.value = Event("Please Enter Subscriber's Name:")
        } else if (inputEmail.value == null){
            _statusMessage.value = Event("Please Enter Subscriber's Email:")
            // Email validation example
        } else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            _statusMessage.value = Event("Email Invalid. Please " +
                    "Enter a Correct Email Address: ")
        } else {
            // if it does match update or delete
            if(isUpdateOrDelete) {
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!
                update(subscriberToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(Subscriber(0, name, email))
                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    // Method we are using to clear all the data if necessary
    fun clearOrDelete() {
        if(isUpdateOrDelete){
            delete(subscriberToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    /**
     * Here is where we show how our repository methods come into play.
     * We create our own ViewModel methods of Inserting, Deleting, Updating, and Deleting
     * All for our ViewModel, REFERENCING the methods we defined in our repository.
     * Ex:
     *  repository.insert(subscriber)
     *  We do this as coroutines using viewModelScope so when onCleared() gets called
     *  during the ViewModelLifeCycle, we won't have a coroutine leak anywhere.
     *
     *  We will also show how we can incorporate verifications for room database using
     *  if() statements.
     *
     *  These validations display messages when the required information is accurate or not.
     */

    fun insert(subscriber: Subscriber) =
        viewModelScope.launch {
            val newRowId = repository.insert(subscriber)
            if(newRowId > -1) {
                // updating value of mutable livedata with status message
                _statusMessage.value = Event(
                    "Subscriber Inserted Successfully: ID - ${newRowId}")
            } else {
                // updating value of mutable livedata with status message
                _statusMessage.value = Event("Error Occurred.")
            }
        }

    fun update(subscriber: Subscriber) =
        viewModelScope.launch {
            val noOfRows = repository.update(subscriber)
            if(noOfRows > 0) {
                inputName.value = null
                inputEmail.value = null
                isUpdateOrDelete = false
                subscriberToUpdateOrDelete = subscriber
                saveButton.value = "Save"
                deleteButton.value = "Clear All"
                _statusMessage.value = Event("$noOfRows Row Updated Successfully.")
            } else {
                _statusMessage.value = Event("Error occurred.")
            }

        }

    fun delete(subscriber: Subscriber) =
        viewModelScope.launch {
            val noOfRowsDeleted = repository.delete(subscriber)
            if(noOfRowsDeleted > 0) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            subscriberToUpdateOrDelete = subscriber
            saveButton.value = "Save"
            deleteButton.value = "Clear All"
            _statusMessage.value = Event("$noOfRowsDeleted Deleted Successfully.")
            } else {
                _statusMessage.value = Event("Error occurred.")
            }

        }

    fun clearAll() =
        viewModelScope.launch {
            val noItemsDeleted = repository.deleteAll()
            if(noItemsDeleted > 0) {
            _statusMessage.value = Event("$noItemsDeleted Subscribers Deleted Successfully.")
            }else {
                _statusMessage.value = Event("Error occurred.")
            }

        }

    /**
     * Method initiate to Update and Delete the list item when the user clicks on the items.
     */
    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveButton.value = "Update"
        deleteButton.value = "Delete"
    }



}