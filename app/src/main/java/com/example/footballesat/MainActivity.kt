package com.example.footballesat

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_creat_account.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private companion object{
        private const val TAG ="MainActivity"
        private const val RC_GOOGEL_SIGn_IN=4926
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        PB1.visibility=View.GONE
        btn_creatAccoutn.setOnClickListener(){
            startActivity(Intent(this,CreatAccount::class.java))
        }
        // ...
        // Initialize Firebase Auth
        auth = Firebase.auth
        // Configure Google Sign In
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.My_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        btnSignin.setOnClickListener (){
            val sigInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(sigInIntent, RC_GOOGEL_SIGn_IN)
        }
        btn_log.setOnClickListener(){
            var email:String=inputemail.text.toString()
            var password:String=inputPassword.text.toString()
            checkemail(email,password)
        }
        forgetPassword.setOnClickListener() {
            var e:String =inputemail.text.toString()
            resetpassword(e)
        }

    }

    private fun resetpassword(e: String) {
        val pattern = Patterns.EMAIL_ADDRESS
        if (e.isEmpty()){
            inputemail.setError("emial Shoud enter")
            inputemail.requestFocus()
            return
        }
        if (!pattern.matcher(e).matches()) {
            inputemail.setError("viled email should enter")
            inputemail.requestFocus()
            return
        }
        PB1.visibility=View.VISIBLE
        auth.sendPasswordResetEmail(e).addOnCompleteListener(this){ task->
            PB1.visibility=View.GONE
            if (task.isSuccessful){
                Toast.makeText(this,"chake your email ",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"try agen same thing wrong ",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun checkemail(email: String, password: String) {
        val pattern = Patterns.EMAIL_ADDRESS
        if (email.isEmpty()){
            inputemail.setError("emial Shoud enter")
            inputemail.requestFocus()
            return
        }else if (!pattern.matcher(email).matches()){
            inputemail.setError("viled email should enter")
            inputemail.requestFocus()
            return
        } else if (password.isEmpty()) {
            inputPassword.setError("Password Shoud enter")
            inputPassword.requestFocus()
            return
        }else {
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){ task->
                if (task.isSuccessful){
                        startActivity(Intent(this, favorite::class.java).putExtra("id_user",auth.uid))
                }else {
                    Toast.makeText(this,"email or password is not corrent ",Toast.LENGTH_SHORT).show()

            }
        }
    }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser!=null){
           // Toast.makeText(this,currentUser.uid,Toast.LENGTH_SHORT).show()
            startActivity(Intent(this,HomePag::class.java).putExtra("id_user",currentUser.uid))
            finish()
        }else {
            updateUI(currentUser)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGEL_SIGn_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
    private fun updateUI(useer: FirebaseUser?) {
        if (useer==null){
            Log.w(TAG,"user is null ,not ging to navigate")
            return
        }
        //Toast.makeText(this,useer.uid,Toast.LENGTH_SHORT).show()
        startActivity(Intent(this,favorite::class.java).putExtra("id_user",useer.uid))
        finish()
    }
    }


