package com.murat.chat.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.murat.chat.databinding.FragmentChatBinding
import com.murat.chat.model.Message


class ChatFragment : Fragment() {
    private lateinit var binding : FragmentChatBinding
    private var receiverId = ""
    private lateinit var ref :CollectionReference
    private var messages= arrayListOf<Message>()
    private var adapter : ChatAdapter = ChatAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding = FragmentChatBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ref = FirebaseFirestore.getInstance().collection("Messages")
        receiverId = arguments?.getString("uid").toString()
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        binding.btnSend.setOnClickListener {
            sendMessage(uid)
        }

        binding.rvMessage.adapter = adapter

        ref.addSnapshotListener { value, error ->
            if (value!= null){
                messages.clear()
                for (item in value.documents){
                    if ( (item.data?.get("receiverId").toString() == receiverId  &&   item.data?.get("senderId").toString() == uid) || (item.data?.get("receiverId").toString() == uid && item.data?.get("senderId").toString() == receiverId)){
                        val message = Message(
                            item.data?.get("senderId").toString(),
                            item.data?.get("receiverId").toString(),
                            item.data?.get("message").toString()
                        )
                        messages.add(message)
                    }

                    adapter.addMessages(messages)
                    Log.d("messages_11", messages.toString())
                    binding.rvMessage.scrollToPosition(messages.size - 1)



                    //val message = item.toObject(Message::class.java)
                }
            }

        }

    }

    private fun sendMessage(uid: String?) {

        val data = Message(
            senderId = uid.toString(),
            receiverId = receiverId,
            binding.etMessage.text.toString()
        )
        ref.document().set(data).addOnCompleteListener {
            if (it.isSuccessful) {

            }
        }
        clearMessages()
    }

    private fun clearMessages() {
        binding.etMessage.setText("")
    }
}