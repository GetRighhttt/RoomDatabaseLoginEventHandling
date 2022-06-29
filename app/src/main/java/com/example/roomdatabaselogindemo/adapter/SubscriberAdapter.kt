package com.example.roomdatabaselogindemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabaselogindemo.R
import com.example.roomdatabaselogindemo.databinding.ListItemBinding
import com.example.roomdatabaselogindemo.model.database.Subscriber

/**
 * Setting up our Adapter class.
 * Passing in our list of Subscribers as a parameter and also our clickListener.
 */
class SubscriberAdapter(private val clickListener: (Subscriber) -> Unit)
    : RecyclerView.Adapter<SubscriberAdapter.SubscriberViewHolder>() {

    private val subscribers = ArrayList<Subscriber>()

    /**
     * Separate class that we use to create our ViewHolder.
     * Must pass in our DataBinding variable in our constructor.
     * This classes main purpose is to bind values to each list item.
     */
    class SubscriberViewHolder(private val binding: ListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // method we call in our adapter class to bind views to their positions
        // And our onClickListener
        fun bind(subscriber: Subscriber, clickListener: (Subscriber) -> Unit) {
            binding.apply {
                nameTv.text = subscriber.name
                emailTv.text = subscriber.email
                // Use the nested layout for the list_item
                listItemLayoutText.setOnClickListener {
                    clickListener(subscriber)
                }
            }
        }
        // cardview item we will use for animation in our BindViewHolder Method
        val linearListItem = binding.linearListItem

        companion object {
            fun inflateFrom(parent: ViewGroup): SubscriberViewHolder {
                // get an instance of layout inflater
                val layoutInflater = LayoutInflater.from(parent.context)
                // need data binding object of list_item layout
                val binding: ListItemBinding = DataBindingUtil.inflate(
                    layoutInflater, R.layout.list_item, parent, false
                )
                return SubscriberViewHolder(binding)
            }
        }
    }

    /**
     * This is typically where we inflate our list_item with the ViewHolder we created
     * below.
     * We also have to create another instance of our DataBinding variable here to inflate
     * that layout list_item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    : SubscriberViewHolder = SubscriberViewHolder.inflateFrom(parent)

    /**
     * Here is where we can attach animations to our items as they are.
     * Used to display data on the list item.
     * Position represents the count value of each list_item.
     * We will get relevant subscribers object from the subscribers list using the position
     * and pass it to the bind function of the RecyclerView class.
     */
    override fun onBindViewHolder(holder: SubscriberViewHolder,
                                  position: Int) {
        holder.bind(subscribers[position], clickListener)
        holder.linearListItem.startAnimation(AnimationUtils
            .loadAnimation(holder.itemView.context, R.anim.visibility_animation))
    }

    /**
     * Typically used just to return the size of the entire list.
     * RecyclerView library will create separate rows based on the count.
     */
    override fun getItemCount(): Int {
        return subscribers.size
    }

    fun setList(subscriber: List<Subscriber>) {
        subscribers.clear()
        subscribers.addAll(subscriber)
    }

}


