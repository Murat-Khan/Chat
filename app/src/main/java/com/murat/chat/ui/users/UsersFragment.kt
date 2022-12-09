package com.murat.chat.ui.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.murat.chat.ui.users.adapter.UserAdapter
import com.murat.chat.R
import com.murat.chat.databinding.FragmentUsersBinding
import com.murat.chat.model.User



class UsersFragment : Fragment() {
   private lateinit var binding: FragmentUsersBinding
    private lateinit var db: FirebaseFirestore
    lateinit var userList  : ArrayList<User>
    private lateinit var adapter: UserAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = UserAdapter{id ->
         findNavController().navigate(R.id.chatFragment, bundleOf("uid" to id))
        }
        init()
        eventChanceListener()

    }

    private fun init() {
        db = FirebaseFirestore.getInstance()
        binding.recycler.layoutManager = LinearLayoutManager(requireActivity())
        binding.recycler.setHasFixedSize(true)
        userList= arrayListOf()

        binding.recycler.adapter = adapter
    }

    private fun eventChanceListener() {
        db.collection("Users").get().addOnSuccessListener {
            if (!it.isEmpty){
                for (data in it.documents){
                    val user : User? = data.toObject(User::class.java)
                    if (user != null && user.uid != FirebaseAuth.getInstance().currentUser?.uid) {
                        userList.add(user)
                    }
                }
              adapter.addUsers(userList)
            }
        }.addOnFailureListener {
                Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
            }

        }

        }



