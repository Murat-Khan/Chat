package com.murat.chat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.murat.chat.databinding.UserItemBinding
import com.murat.chat.model.User

class UserAdapter(  private val userList :ArrayList<User>) :RecyclerView.Adapter<UserAdapter.UserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        return UserHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.onBind(userList[position])
    }

    override fun getItemCount(): Int = userList.size
    inner class UserHolder (private var binding : UserItemBinding):RecyclerView.ViewHolder(binding.root) {

        fun onBind(user : User){
            binding.userName.text = user.userName
            binding.userPhone.text = user.phone


        }
    }
}
