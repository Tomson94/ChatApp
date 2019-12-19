package com.qdev.chatapp.ViewModel

import android.app.Application
import android.app.ProgressDialog
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.qdev.chatapp.Utils.Utils
import org.json.JSONObject
import kotlin.collections.ArrayList

class ContactsViewModel(application: Application) : AndroidViewModel(application) {
    var users=MutableLiveData<List<String>>()
    var usernames= ArrayList<String>()

    fun getAllContacts(pd: ProgressDialog) {
        var request =StringRequest(Request.Method.GET,Utils.Util.URL, Response.Listener<String> {response ->

            if (response.equals("null")){
                Toast.makeText(getApplication(),"No contacts found",Toast.LENGTH_LONG).show()
            }
            else{

                var jsonObj = JSONObject(response)
                val i: Iterator<*> = jsonObj.keys()
                var key = ""
                while (i.hasNext()) {
                    key = i.next().toString()
                    if (key != Utils.getMyPrefference(getApplication(),"name")) {
                        usernames.add(key)
                    }
                }
                users.value=usernames
                pd.dismiss()

            }

        }, Response.ErrorListener { error: VolleyError? ->

        })
        var requestQueue:RequestQueue =Volley.newRequestQueue(getApplication())
        requestQueue.add(request)
    }
    fun getData():MutableLiveData<List<String>>{
      return users
    }

}