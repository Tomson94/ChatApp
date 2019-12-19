package com.qdev.chatapp.Service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.text.Html

import com.google.firebase.database.*
import com.qdev.chatapp.R
import com.qdev.chatapp.Utils.Utils
import com.qdev.chatapp.View.Contacts


class NewMessageService : Service() {


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        notify(applicationContext,"You may have new messages")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onCreate() {
        super.onCreate()
        val thread = Thread(Runnable {
            checkNewMessages()
        })
        thread.run()

    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented")
    }

    private fun checkNewMessages() {
        var strmessages:String=""
            val database = FirebaseDatabase.getInstance()
            val myRef1 = database.getReference("Messages")
            myRef1.addChildEventListener(object : ChildEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }
                override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                    strmessages =""
                    for (el in p0.children) {
                        var key = p0.key
                        var messages: HashMap<String, String> = el.getValue() as HashMap<String, String>
                        var message = messages?.get("message")
                        var userName = messages?.get("user")
                        var isread =messages.get("read")
                        var name = Utils.getMyPrefference(getApplication(),"name")+userName
                        if (userName.equals(Utils.getMyPrefference(getApplication(), "name"))) {

                        } else {
                            if(key.equals(name)) {
                                if (message != null && isread.equals("0")) {

                                    strmessages = strmessages + "\n" + userName + ":" + message
                                    notify(applicationContext, strmessages)
                                }
                            }
                        }
                    }
                }
                override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                    strmessages =""
                    for (el in p0.children) {
                        var key = p0.key
                        var messages: HashMap<String, String> = el.getValue() as HashMap<String, String>
                        var message = messages?.get("message")
                        var userName = messages?.get("user")
                        var read = messages?.get("read")
                        var name = Utils.getMyPrefference(getApplication(),"name")+userName
                        if (userName.equals(Utils.getMyPrefference(getApplication(), "name"))) {
                        } else {
                            if (key.equals(name)) {
                                if (message != null && read.equals("0")) {
                                    strmessages = strmessages + "\n" + userName + ":" + message
                                    notify(applicationContext, strmessages)
                                }
                            }
                        }
                    }
                }
                override fun onChildRemoved(p0: DataSnapshot) {
                }
            })
    }

    companion object {
        fun notify(context: Context,mesage:String){
            val resultIntent = Intent(context, Contacts::class.java)
            val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(resultIntent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            if (android.os.Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                val notifyID = 1
                val CHANNEL_ID = "my_channel_01"
                val name: CharSequence =
                    context.getString(R.string.default_notification_channel_id)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
                val mNotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                mNotificationManager.createNotificationChannel(mChannel)
                val notification: Notification = Notification.Builder(context)
                    .setContentTitle("ChatApp")
                    .setSubText(Html.fromHtml("<p>${mesage}</p>", Html.FROM_HTML_MODE_COMPACT))
                    .setSmallIcon(R.drawable.icon)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(resultPendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .build()
                mNotificationManager.notify(notifyID , notification)
            }else{
                val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                val notification: Notification = Notification.Builder(context)
                    .setContentTitle("ChatApp")
                    .setContentText(mesage)
                    .setContentIntent(resultPendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.drawable.icon)
                    .build()
                mNotificationManager.notify(1 , notification)
            }
        }
    }

}

