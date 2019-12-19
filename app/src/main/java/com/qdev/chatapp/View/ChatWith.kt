package com.qdev.chatapp.View

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.qdev.chatapp.Model.Message
import com.qdev.chatapp.R
import com.qdev.chatapp.R.layout.activity_chat_with
import com.qdev.chatapp.Utils.Utils
import com.qdev.chatapp.ViewModel.ChatWithViewModel
import kotlinx.android.synthetic.main.activity_chat_with.*
import kotlinx.android.synthetic.main.message_area.*


class ChatWith : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_chat_with)
        var  relativeLayout =this.layout2
        var linearLayout = this.layout1
        var edt_Message = this.messageArea
        var imgSendButton = this.sendButton
        var scroolview = this.scrollView
        scrollView.post {
            scrollView.fullScroll(View.FOCUS_DOWN)
        }
        var viewModel = ViewModelProviders.of(this).get(ChatWithViewModel::class.java)
        supportActionBar?.setTitle(viewModel.getChatHeadUser())
        imgSendButton.setOnClickListener{
            var message = messageArea.text.toString()
            var name  =Utils.getMyPrefference(applicationContext,"name")


            if (message==null){
                Toast.makeText(applicationContext ,"Can't send empty message", Toast.LENGTH_LONG).show()
            }else{
                if (name!=null){
                    var objMessage=Message(message,name)
                    viewModel.sendMessage(objMessage,messageArea)
                }

            }
        }

    viewModel.getMessage().observe(this, Observer { list->
        addMessageBox(list,linearLayout)

        })
    }


    private fun addMessageBox(list: List<Message>?, linearLayout: LinearLayout) {
        if (list != null) {
            linearLayout.removeAllViews()
            for(el in list){
                val textView = TextView(this@ChatWith)
                textView.setText(el.strMessage)
                textView.textSize = 15.0f


                val lp2 = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                lp2.weight = 1.0f

                if (el.user.equals( "1")) {
                    lp2.gravity = Gravity.RIGHT
                    textView.setBackgroundResource(R.drawable.bubble_in)
                } else {
                    lp2.gravity = Gravity.LEFT
                    textView.setBackgroundResource(R.drawable.bubble_out)
                }
                textView.layoutParams = lp2

                linearLayout.addView(textView)
                scrollView.post {
                    scrollView.fullScroll(View.FOCUS_DOWN)
                }
            }

        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        if (id==R.id.logout){
            Utils.setSharedPrefference(applicationContext,"status","0")
            var  intent = Intent(applicationContext,Login::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}
