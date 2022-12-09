package com.murat.chat.ui.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.murat.chat.R
import com.murat.chat.model.Message


class ChatAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val data: ArrayList<Message> = arrayListOf()

    fun addMessages(newData: ArrayList<Message>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        return if (item.senderId == FirebaseAuth.getInstance().currentUser?.uid) {
            CURRENT_USER
        } else OTHER_USER


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == CURRENT_USER) {
            MyMessageHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_right, parent, false)
            )
        } else {
            OtherMessageHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_left, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = data[position]
        when(holder){
            is MyMessageHolder -> holder.onBind(message)
            is OtherMessageHolder -> holder.onBind(message)
        }
    }

    override fun getItemCount(): Int = data.size

    companion object {
        private const val CURRENT_USER = 1
        private const val OTHER_USER = 0
    }


    inner class MyMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var tvMassage: TextView? = null
        fun onBind(message: Message) {
            tvMassage?.text = message.message

        }

        init {
            tvMassage = itemView.findViewById(R.id.right_message)
        }


    }

    inner class OtherMessageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var tvMassage: TextView? = null
        fun onBind(message: Message) {
            tvMassage?.text = message.message

        }

        init {
            tvMassage = itemView.findViewById(R.id.left_message)
        }
    }

}