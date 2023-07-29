package com.vasertoker.firebaseauth.relaTimeDatabase

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vasertoker.firebaseauth.R
import com.vasertoker.firebaseauth.databinding.ActivityRealBinding
import com.vasertoker.firebaseauth.models.UserData

class RealActivity : AppCompatActivity() {
    lateinit var binding: ActivityRealBinding

    lateinit var firebaseAuth: FirebaseAuth
    lateinit var firebaseDatabase: FirebaseDatabase
    lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser


        firebaseDatabase = Firebase.database
        reference = firebaseDatabase.getReference("users")
//
        val email = currentUser?.email!!
        val metadata = currentUser.metadata!!.toString()
        val phoneNumber = currentUser.phoneNumber
        val tenantId = currentUser.tenantId
        val photoUrl = currentUser.photoUrl.toString()
        val displayName = currentUser.displayName
        val uid = currentUser.uid

        val userData = UserData(email, metadata, phoneNumber, tenantId, photoUrl, displayName, uid)
       reference.child(uid).setValue(userData)


        reference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                children.forEach {
                    val value = it.getValue(UserData::class.java)
                    Log.i(TAG, "onDataChange: ${value?.displayName}")
                }
                Log.i(TAG, "onDataChange:  ")
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        Toast.makeText(this, "${currentUser?.email}", Toast.LENGTH_SHORT).show()


    }
}