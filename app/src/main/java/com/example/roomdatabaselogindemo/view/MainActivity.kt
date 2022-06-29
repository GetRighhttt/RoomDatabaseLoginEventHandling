package com.example.roomdatabaselogindemo.view

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdatabaselogindemo.R
import com.example.roomdatabaselogindemo.databinding.ActivityMainBinding
import com.example.roomdatabaselogindemo.factory.SubscriberViewModelFactory
import com.example.roomdatabaselogindemo.model.database.DatabaseForSubscribers
import com.example.roomdatabaselogindemo.model.database.Subscriber
import com.example.roomdatabaselogindemo.repository.SubscriberRepository
import com.example.roomdatabaselogindemo.adapter.SubscriberAdapter
import com.example.roomdatabaselogindemo.viewmodel.SubscriberViewModel

class MainActivity : AppCompatActivity() {

    // creating our reference variables
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: SubscriberViewModel
    private lateinit var adapter: SubscriberAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // get an instance of our Database so we can use our Factory class
        val dao = DatabaseForSubscribers.getInstance(application).subscriberDao
        val repository = SubscriberRepository(dao)

        /**
         * Now we can use the repository instance to create our ViewModelFactory instance.
         * And then get an instance of our ViewModel.
         */

        val factory = SubscriberViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SubscriberViewModel::class.java]

        binding.apply {
            // Now we assigning our DataBinding XML variables
            viewModelXML = viewModel
            lifecycleOwner = this@MainActivity
        }
        displayRecyclerView() // display the RecyclerView

        /**
         * Code to observe LiveData from the Event class about the status of the Event,
         * and display the status message each time the events(Insert, Update, Delete) get
         * updated.
         *
         * Essentially will notify the user when something is saved to the database.
         *
         * getContentIfNotHandled() is a method that is defined in our Event class
         * that is essentially boilerplate code. This method was not made private so we
         * can directly access it from anywhere in our code.
         */
        viewModel.message.observe(this, Observer { it ->
            it?.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
   }

    /**
     * Method below is used to observe the LiveData of the subscribers list,
     * receive the changes, and set the list that we can display for our
     * RecyclerView.
     *
     * We are setting the list from the setList method we created in our adapter
     * class.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun displaySubscribers() {
        viewModel.subscribers.observe(this@MainActivity, Observer {
            // Set the List
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    /**
     * Method below will be used to display the RecyclerView from our Subscribers
     * LiveData method that we have created above using DataBinding.
     *
     * We set the LayoutManager to Linear. and construct our adapter with our clickListener.
     */
    private fun displayRecyclerView() {
        binding.apply {
            rvSubscriber.layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = SubscriberAdapter(
                clickListener = { selectedItem: Subscriber -> listItemClicked(selectedItem) })
            rvSubscriber.adapter = adapter
        }

        displaySubscribers() // LiveData observer method
    }

    /**
     * OnCLickListener method for the onclick.
     */

    private fun listItemClicked(subscriber: Subscriber) {
        viewModel.initUpdateAndDelete(subscriber)
    }

}

