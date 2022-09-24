package com.example.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btnSignUp
import kotlinx.android.synthetic.main.activity_login.edt_email
import kotlinx.android.synthetic.main.activity_login.edt_password
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUp : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val name = edt_name.text.toString()
            val email = edt_email.text.toString()
            val password = edt_password.text.toString()

            signUp(name, email, password)
        }
    }

    private fun signUp(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                    if (task.isSuccessful) {
                        addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                        // code for jumping to home
                        val intent = Intent(this@SignUp, MainActivity::class.java)
                        finish()
                        startActivity(intent)
                    } else {
                        Toast.makeText(baseContext, "Authentication failed ${task.exception}", Toast.LENGTH_SHORT)
                            .show()
                        Log.d("Firebase", task.exception.toString())
                    }
            }
//        keytool -list -v \-alias androiddebugkey -keystore %USERPROFILE%\.android\debug.keystore
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").child(uid).setValue(User(name, email, uid))
    }
}