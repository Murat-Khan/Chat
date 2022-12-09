package com.murat.chat.ui.users.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.murat.chat.databinding.UserItemBinding
import com.murat.chat.model.User

class UserAdapter(private val onClick : (uid : String) -> Unit )
    :RecyclerView.Adapter<UserAdapter.UserHolder>() {
    private val userList :ArrayList<User> = arrayListOf()
    fun addUsers(newData: ArrayList<User>){
        userList.clear()
        userList.addAll(newData)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.onBind(userList[position])
    }

    override fun getItemCount(): Int = userList.size
    inner class UserHolder (private var binding : UserItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun onBind(user : User){
            itemView.setOnClickListener {
                onClick(user.uid.toString())
            }

            binding.userName.text = user.userName
            binding.userPhone.text = user.phone


        }
    }
}
