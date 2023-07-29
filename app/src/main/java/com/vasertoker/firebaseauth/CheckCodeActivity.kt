package com.vasertoker.firebaseauth

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.vasertoker.firebaseauth.databinding.ActivityCheckCodeBinding
import java.util.concurrent.TimeUnit

class CheckCodeActivity : AppCompatActivity() {
    lateinit var binding: ActivityCheckCodeBinding
    lateinit var auth: FirebaseAuth
    private var RC_SIGN_IN = 1
    lateinit var storedVerificationId: String
    lateinit var phone: String
    lateinit var code: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        phone = intent.getStringExtra("phone").toString()
         storedVerificationId = intent.getStringExtra("id")!!


        val subSequence = phone.subSequence(0, 9)
        binding.tvTextAuth.text = "Bir martalik kod $subSequence-**-**\nraqamiga yuborildi"

        binding.tvSmsCode.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                verifyCode()
                val view = currentFocus
                if (view != null) {
                    val imm: InputMethodManager =
                        getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
            }
            true
        }

        binding.tvSmsCode.addTextChangedListener {
            if (it.toString().length == 7) {
                verifyCode()
            }
        }

        binding.btnRefresh.setOnClickListener {
            if (phone != null) {
                resendCode(phone)
            }
        }

    }

    private fun resendCode(toString: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(toString) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {

                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyCode() {
         code = binding.tvSmsCode.unmaskedText
//        Toast.makeText(this, "$code", Toast.LENGTH_SHORT).show()


        if (code.length == 6){
            Toast.makeText(this, "ichiga kirdi", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "verifyCode: $storedVerificationId  va $code")
            val credential = PhoneAuthProvider.getCredential(storedVerificationId, code)
            signInWithPhoneAuthCredential(credential)

        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")

                    val user = task.result?.user?.phoneNumber

                    Log.i(ContentValues.TAG, "signInWithPhoneAuthCredential: $user ")
                    val intent = Intent(this, HomeActivity::class.java).apply {
                        putExtra("number", phone)
                    }

                    startActivity(intent)
                    finish()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
                }
            }
    }

}