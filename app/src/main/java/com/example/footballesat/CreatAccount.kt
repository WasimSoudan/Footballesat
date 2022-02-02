package com.example.footballesat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_creat_account.*

class CreatAccount : AppCompatActivity() {
    private companion object{
        private const val TAG:String ="CreatAccount"
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creat_account)
        imag_back.setOnClickListener(){
            startActivity(Intent(this,MainActivity::class.java))
        }
        auth = Firebase.auth
        btn_Creat_Account.setOnClickListener(){
            var name:String=inputname.text.toString()
            var email:String=inputemail2.text.toString()
            var password:String=inputPassword2.text.toString()
            var re_password:String=input_rePassword.text.toString()
            chakeInfo(name,email,password,re_password)
        }
    }

    private fun chakeInfo(name: String, email: String, password: String, rePassword: String) {
        val pattern = Patterns.EMAIL_ADDRESS
        if (email.isEmpty()){
            inputemail2.setError("emial Shoud enter")
            inputemail2.requestFocus()
            return
        }else if (!pattern.matcher(email).matches()){
            inputemail2.setError("viled email should enter")
            inputemail2.requestFocus()
            return
        } else if (password.isEmpty()){
            inputPassword2.setError("Password Shoud enter")
            inputPassword2.requestFocus()
            return
        } else if (password.length<6){
            inputPassword2.setError("Password Shoud be more than 6 char")
            inputPassword2.requestFocus()
            return
        }else if(!password.equals(rePassword)) {
            inputPassword2.setError("not same")
            input_rePassword.setError("not same")
            input_rePassword.requestFocus()
            inputPassword2.requestFocus()
        }else {
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                }else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
        }
    }
    private fun updateUI(useer: FirebaseUser?) {
        if (useer==null){
            Log.w(TAG,"user is null ,not ging to navigate")
            return
        }
        startActivity(Intent(this,favorite::class.java).putExtra("id_user",useer.uid))
        finish()
    }
}