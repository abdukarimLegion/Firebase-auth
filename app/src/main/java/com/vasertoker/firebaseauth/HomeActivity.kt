package com.vasertoker.firebaseauth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vasertoker.firebaseauth.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding : ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val stringExtra = intent.getStringExtra("number")
        binding.tvNumber.text = stringExtra
    }
}