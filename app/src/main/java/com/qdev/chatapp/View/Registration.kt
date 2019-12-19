package com.qdev.chatapp.View

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.textclassifier.TextLinks
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.qdev.chatapp.Model.User
import com.qdev.chatapp.R
import com.qdev.chatapp.ViewModel.RegistrationViewModel
import kotlinx.android.synthetic.main.activity_login.edt_email
import kotlinx.android.synthetic.main.activity_login.edt_password
import kotlinx.android.synthetic.main.activity_login.login
import kotlinx.android.synthetic.main.activity_registration.*
import org.json.JSONObject

class Registration : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        var edt_name = this.edt_email
        var edt_password = this.edt_password
        var btn_register = this.login
        var go_to_login = this.gotolog
            var viewModel = ViewModelProviders.of(this).get(RegistrationViewModel::class.java!!)

        go_to_login.setOnClickListener {
            var intent = Intent(this, Login::class.java)
            startActivity(intent)
            finish()
        }

        btn_register.setOnClickListener{
            var progressDialogue= ProgressDialog(this)
            progressDialogue.setMessage("Loading...")
            progressDialogue.show()
            var name = edt_name.text.toString()
            var password = edt_password.text.toString()
            if(name!=null){
                if (name.length<2)
                {
                    edt_name.setError("Enter valisd name")
                    return@setOnClickListener
                }

            }else{
             edt_name.setError("Enter vslid name")
                return@setOnClickListener

            }
            if (password!=null){
                if (password.length<3){
                    edt_password.setError("Enter valid password")
                    return@setOnClickListener
                }
            }else{
                edt_password.setError("Enter valid password")
                return@setOnClickListener
            }

            var user = User(name,password)
                var status = viewModel.registerUser(user,progressDialogue)
        }

    }
}
