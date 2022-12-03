package com.murat.chat.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.murat.chat.R
import com.murat.chat.databinding.FragmentAuthBinding
import com.murat.chat.showToast
import java.util.concurrent.TimeUnit


class AuthFragment : Fragment() {
 private lateinit var binding: FragmentAuthBinding
    private lateinit var auth : FirebaseAuth
    private var verId = ""
    private var phone = ""
  private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

      override fun onCodeSent(verificationId: String, p1: PhoneAuthProvider.ForceResendingToken) {
          super.onCodeSent(verificationId, p1)
          verId = verificationId
          binding.acceptContainer.isVisible = true
          binding.authContainer.isVisible = false
      }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    saveUserData()
                } else {
                    showToast(task.exception?.message.toString())


                }
            }
    }

    private fun saveUserData() {
        val uid = auth.currentUser?.uid
        if (uid!= null){
            val ref = FirebaseFirestore.getInstance().collection("Users").document(uid)
            val userData = hashMapOf<String,String>()
            userData["uid"] = uid
            userData["phone"] = phone
            userData["userName"] = binding.inAccept.userName.text.toString()

            ref.set(userData).addOnCompleteListener {
                if (it.isSuccessful) {
                    findNavController().navigateUp()
                } else {
                    it.exception?.message?.let { it1 -> showToast(it1) }
                }
            }


        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAuthBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.inAuth.btnGetCode.setOnClickListener {
            if (binding.inAuth.editPhone.text.toString().isEmpty()){
binding.inAuth.editPhone.error = "Введитете номер"
            }else{
                sendVerificationCode()
            }



        }

        binding.inAccept.btnContinue.setOnClickListener {
            val credential = PhoneAuthProvider.getCredential(verId, binding.inAccept.editSMS.text.toString())
            signInWithPhoneAuthCredential(credential)
        }


    }

    private fun sendVerificationCode() {
        phone = binding.inAuth.editPhone.text.toString()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity())                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }


}