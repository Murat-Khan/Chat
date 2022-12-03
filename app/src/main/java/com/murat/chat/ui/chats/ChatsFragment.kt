package com.murat.chat.ui.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.murat.chat.adapter.UserAdapter
import com.murat.chat.databinding.FragmentChatsBinding
import com.murat.chat.model.User


class ChatsFragment : Fragment() {
   private lateinit var binding: FragmentChatsBinding
    private lateinit var db: FirebaseFirestore
    lateinit var userList  : ArrayList<User>
    private lateinit var adapter: UserAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatsBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        eventChanceListener()
    }

    private fun init() {
        db = FirebaseFirestore.getInstance()
        binding.recycler.layoutManager = LinearLayoutManager(requireActivity())
        binding.recycler.setHasFixedSize(true)
        userList= arrayListOf()
        adapter = UserAdapter(userList)
        binding.recycler.adapter = adapter
    }

    private fun eventChanceListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("Users").get().addOnSuccessListener {
            if (!it.isEmpty){
                for (data in it.documents){
                    val user : User? = data.toObject(User::class.java)
                    if (user != null) {
                        userList.add(user)
                    }
                }
                binding.recycler.adapter = UserAdapter(userList)
                adapter.notifyDataSetChanged()
            }
        }.addOnFailureListener {
                Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
            }

        }

        }



