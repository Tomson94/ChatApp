package com.qdev.chatapp.ViewModel

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.qdev.chatapp.Model.Message
import com.qdev.chatapp.Utils.Utils
import java.util.*
import kotlin.collections.HashMap

class ChatWithViewModel(application: Application) : AndroidViewModel(application) {
    fun  getChatHeadUser(): String? {
        var user = Utils.getMyPrefference(getApplication(),"chatwith")
        return user
    }

fun  sendMessage(objMessage: Message, messageArea: EditText) {
    val database = FirebaseDatabase.getInstance()

    val myRef = database.getReference("Messages/"+ Utils.getMyPrefference(getApplication(),"chatwith")+ Utils.getMyPrefference(getApplication(),"name"))
    val myRef1 = database.getReference("Messages/"+ Utils.getMyPrefference(getApplication(),"name")+ Utils.getMyPrefference(getApplication(),"chatwith"))
    val map: MutableMap<String, String> = HashMap()
    map["message"] = objMessage.strMessage
    map["user"] = objMessage.user
    map["read"] = "0"
    myRef.push().setValue(map)
    myRef1.push().setValue(map)
    messageArea.setText("")
}
    fun getMessage(): MutableLiveData<List<Message>> {
        var update= MutableLiveData<List<Message>>()
        var addd= ArrayList<Message>()

        val database = FirebaseDatabase.getInstance()
        val myRef1 = database.getReference("Messages/"+ Utils.getMyPrefference(getApplication(),"name")+ Utils.getMyPrefference(getApplication(),"chatwith"))
        myRef1.addChildEventListener(object :ChildEventListener{
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                        var messages:HashMap<String,String> = p0.getValue() as HashMap<String, String>
                        var key = p0.key
                        var message = messages?.get("message")
                        var userName = messages?.get("user")
                val myRef1 = database.getReference("Messages/"+ Utils.getMyPrefference(getApplication(),"name")+ Utils.getMyPrefference(getApplication(),"chatwith"))
                if (key != null) {
                    val map: MutableMap<String, String> = HashMap()
                    map["message"] =message.toString()
                    map["user"] =userName.toString()
                    map["read"] = "1"
                    myRef1.child(key).setValue(map)
                }
                if (userName.equals( Utils.getMyPrefference(getApplication(),"name"))) {
                    addd.add(Message("You\n\n\t\t$message","1"))
                    update.value = addd


                } else {
                 addd.add(Message(Utils.getMyPrefference(getApplication(),"chatwith") + "\n\n\t\t" + message,"2"))
                    update.value = addd
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }



            override fun onChildRemoved(p0: DataSnapshot) {

            }


        })
        return update
    }

}