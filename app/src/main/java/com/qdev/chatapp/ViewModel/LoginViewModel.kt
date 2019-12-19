package com.qdev.chatapp.ViewModel

import android.app.Application
import android.app.ProgressDialog
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.AndroidViewModel
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.qdev.chatapp.Model.User
import com.qdev.chatapp.Utils.Utils
import com.qdev.chatapp.View.Contacts
import org.json.JSONObject

class LoginViewModel(application: Application) : AndroidViewModel(application) {

fun  checkUserLogin(user: User, progressDialogue: ProgressDialog){

    var request = StringRequest(Request.Method.GET,Utils.Util.URL, Response.Listener { response ->
        if (response.equals("null")){
            Toast.makeText(getApplication(),"No registered user found",Toast.LENGTH_LONG).show()
       progressDialogue.dismiss()
        }else{
            var jsonObject = JSONObject(response)
            if (!jsonObject.has(user.name)){
                Toast.makeText(getApplication(),"No registered user found",Toast.LENGTH_LONG).show()
          progressDialogue.dismiss()
            }else{
                if(jsonObject.getJSONObject(user.name).get("password").equals(user.password)){
                    Toast.makeText(getApplication(),"success",Toast.LENGTH_LONG).show()
                    Utils.LoggedInUser.name=user.name
                    Utils.LoggedInUser.password = user.password
                    Utils.LoggedInUser.status =1
                    Utils.setSharedPrefference(getApplication(),"name",user.name)
                    Utils.setSharedPrefference(getApplication(),"status","1")

                    var intent = Intent(getApplication(),Contacts::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(getApplication(), intent, null)
               progressDialogue.dismiss()
                }else{
                    Toast.makeText(getApplication(),"Incorrect Password",Toast.LENGTH_LONG).show()
                progressDialogue.dismiss()
                }
            }
        }

    }, Response.ErrorListener { error: VolleyError? ->

    })
    var  requestQueue:RequestQueue= Volley.newRequestQueue(getApplication())
    requestQueue.add(request)

}
}