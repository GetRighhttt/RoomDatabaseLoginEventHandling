package com.example.roomdatabaselogindemo.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.roomdatabaselogindemo.repository.SubscriberRepository
import com.example.roomdatabaselogindemo.viewmodel.SubscriberViewModel
import java.lang.IllegalArgumentException

/**
 * Our ViewModel class sends the repository as a parameter, so we must
 * create a Factory class.
 * In this class, we also must send the repository as a parameter in our
 * constructor.
 */
class SubscriberViewModelFactory(private val repository: SubscriberRepository)
    : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SubscriberViewModel::class.java)) {
            return SubscriberViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class.")
    }
}