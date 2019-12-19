package com.qdev.chatapp.ViewModel

import android.app.Application
import android.app.ProgressDialog
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.internal.InternalTokenProvider
import com.qdev.chatapp.Model.User
import com.qdev.chatapp.Utils.Utils
import com.qdev.chatapp.View.Login
import org.json.JSONObject

class RegistrationViewModel(application: Application) : AndroidViewModel(application){

    fun registerUser(user: User,progressDialogue:ProgressDialog):String{
        var  flags:String=""

        val request = StringRequest(Request.Method.GET,Utils.Util.URL, Response.Listener<String> { response ->

            if (response .equals("null")) {
                val database = FirebaseDatabase.getInstance()
                val myRef = database.getReference("Users")
                myRef.child(user.name).child("password").setValue(user.password)
                flags ="registration successful"
                Toast.makeText(getApplication(), "registration successful", Toast.LENGTH_LONG).show()
                var  intent = Intent(getApplication(),Login::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(getApplication(),intent,null)
                progressDialogue.dismiss()

            }else
            {
                var obj = JSONObject(response)
                if (!obj.has(user.name)){
                    val database = FirebaseDatabase.getInstance()
                    val myRef = database.getReference("Users")
                    myRef.child(user.name).child("password").setValue(user.password)
                    flags ="registration successful"
                    var  intent = Intent(getApplication(),Login::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(getApplication(),intent,null)
                    Toast.makeText(getApplication(), "registration successful", Toast.LENGTH_LONG).show()
                    progressDialogue.dismiss()
                }else{
                    Toast.makeText(getApplication(), "User name already excists", Toast.LENGTH_LONG).show()
                    flags ="User name already excists"
                    progressDialogue.dismiss()

                }
            }

        }, Response.ErrorListener {errorVolley: VolleyError? ->
            flags ="Some network error occured,Please try again"
            progressDialogue.dismiss()
        })
        var requestQueue: RequestQueue =Volley.newRequestQueue(getApplication())
        requestQueue.add(request)

        return flags
    }



}