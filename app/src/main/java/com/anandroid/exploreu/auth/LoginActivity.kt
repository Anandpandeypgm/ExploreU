package com.anandroid.exploreu.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.anandroid.exploreu.MainActivity
import com.anandroid.exploreu.R
import com.anandroid.exploreu.databinding.ActivityLoginBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val auth= FirebaseAuth.getInstance()
    private var verificationId: String? = null

    private lateinit var dialog : AlertDialog



    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


    binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = AlertDialog.Builder(this).setView(R.layout.loading_layout)
            .setCancelable(false).create()

        binding.sendOtp.setOnClickListener {
            if (binding.userNumber.text!!.isEmpty()){
                binding.userNumber.error = "Please enter your number"
            }else{
                sendOtp(binding.userNumber.text.toString())
            }
        }


        binding.verifyOtp.setOnClickListener {
            if (binding.userOtp.text!!.isEmpty()){
                binding.userOtp.error = "Please enter your OTP"
            }else{
                verifyOtp(binding.userOtp.text.toString())
            }
        }

    }

    private fun verifyOtp(otp: String) {
//        binding.sendOtp.showLoadingButton()
        dialog.show()
        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)

        signInWithPhoneAuthCredential(credential)

    }

    private fun sendOtp(number: String) {
   //     binding.sendOtp.showLoadingButton()
        dialog.show()

      val  callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        //       binding.sendOtp.showNormalButton()

                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@LoginActivity.verificationId =verificationId
                dialog.dismiss()

   //             binding.sendOtp.showNormalButton()
                binding.numberLayout.visibility = GONE
                binding.otpLayout.visibility =VISIBLE
            }
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
   //             binding.sendOtp.showNormalButton()
                if (task.isSuccessful) {
                    
                    checkUserExist(binding.userNumber.text.toString())
                    
              //    startActivity(Intent(this,MainActivity::class.java))
                //    finish()
                } else {
                    dialog.dismiss()
                    Toast.makeText(this,task.exception!!.message,Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun checkUserExist(number: String) {
        FirebaseDatabase.getInstance().getReference("users").child(number).
        addValueEventListener(object:ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                dialog.dismiss()
                Toast.makeText(this@LoginActivity,p0.message,Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()){
                    dialog.dismiss()
                    startActivity(Intent(this@LoginActivity,MainActivity::class.java))
                    finish()
                } else{
                    startActivity(Intent(this@LoginActivity,RegisterActivity::class.java))
                    finish()
                }
            }
        })

    }
}