package com.qdev.chatapp.View

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.qdev.chatapp.Model.User
import com.qdev.chatapp.R
import com.qdev.chatapp.Service.NewMessageService
import com.qdev.chatapp.Utils.Utils
import com.qdev.chatapp.ViewModel.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var edt_name = this.edt_email
        var edt_password = this.edt_password
        var btn_login  = this.login
        var go_to_registration = this.gotoreg
        var viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)


        var intent = Intent(applicationContext,NewMessageService::class.java)
        if (android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        startForegroundService(intent)
        else
            startService(intent)

        if(Utils.getMyPrefference(applicationContext,"status").equals("1")){
            var intent = Intent(this, Contacts::class.java)
            startActivity(intent)
            finish()
        }

        go_to_registration.setOnClickListener {
            var intent = Intent(this, Registration::class.java)
            startActivity(intent)
            finish()
        }


        btn_login.setOnClickListener{
            var progressDialogue =ProgressDialog(this)
            progressDialogue.setMessage("Logging  in please wait...")
            progressDialogue.show()
            var name = edt_name.text.toString()
            var password  = edt_password.text.toString()
            if (name!=null){
                if (name.length<2){
                    edt_name.setError("Enter a valid name")
                    return@setOnClickListener
                }
            }else{
                edt_name.setError("Enter a valid name")
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

            viewModel.checkUserLogin(user,progressDialogue)

        }
    }



}
