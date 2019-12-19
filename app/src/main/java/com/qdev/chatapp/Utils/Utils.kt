package com.qdev.chatapp.Utils

import android.content.Context
import android.content.SharedPreferences
import android.view.Display

class Utils {
    object Util {
       var URL ="https://chatappwithchatlibrary.firebaseio.com/Users.json"
    }
    object LoggedInUser{
        var name:String = ""
        var password:String = ""
        var status:Int =0
        var chatWith:String = ""

    }
    companion object{
        fun  setSharedPrefference( context: Context,Key:String, value:String){
            var sharedPreferences = context.getSharedPreferences("MyPref",Context.MODE_PRIVATE)
            var editor:SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString(Key,value)
            editor.commit()

        }
        fun getMyPrefference(context: Context,Key: String): String? {
            var sharedPreferences = context.getSharedPreferences("MyPref",Context.MODE_PRIVATE)
            var value = sharedPreferences.getString(Key,"NoData")
            return value
        }

    }
}