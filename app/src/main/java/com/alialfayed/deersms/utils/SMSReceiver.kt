package com.alialfayed.deersms.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.telephony.SmsManager
import com.alialfayed.deersms.model.MessageFirebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class SMSReceiver : BroadcastReceiver() {

    private lateinit var sentIntent: PendingIntent
    private lateinit var deliveredIntent: PendingIntent
    private lateinit var smsSentReceiver: BroadcastReceiver
    private lateinit var smsDeliveredReceiver: BroadcastReceiver

    private lateinit var smsId: String
    private lateinit var smsPersonName: String
    private lateinit var smsPersonNumber: String
    private lateinit var smsMessage: String
    private lateinit var smsDate: String
    private lateinit var smsTime: String
    private lateinit var smsStatus: String
    private lateinit var smsSendVia: String
    private lateinit var userID: String
    private  var smsCalender: Long = 0
    private lateinit var smsDelivered:String

    lateinit var databaseReferenceMsg: DatabaseReference

    override fun onReceive(context: Context?, intent: Intent?) {

        databaseReferenceMsg = FirebaseDatabase.getInstance().getReference("Messages")
        sentIntent = PendingIntent.getBroadcast(context, 0, Intent("Message sent"), 0)
        deliveredIntent = PendingIntent.getBroadcast(context, 0, Intent("message delivered"), 0)

        smsId = intent?.extras?.getString("SmsId")!!
        smsPersonName = intent.extras?.getString("SmsPersonName")!!
        smsPersonNumber = intent.extras?.getString("SmsPersonNumber")!!
        smsMessage = intent.extras?.getString("SmsMessage")!!
        smsDate = intent.extras?.getString("SmsDate")!!
        smsTime = intent.extras?.getString("SmsTime")!!
        smsStatus = intent.extras?.getString("SmsStatus")!!
        smsSendVia = intent.extras?.getString("SmsSendVia")!!
        userID = intent.extras?.getString("UserID")!!
        smsCalender = intent.extras!!.getLong("SmsCalender")
        smsDelivered = intent.extras?.getString("SmsDelivered")!!

        val smsManager: SmsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(smsPersonNumber, null, smsMessage, sentIntent, deliveredIntent)

        smsSentReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, arg1: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        // update msg on fireBase
                        updateMsg("Completed" ,"Sent")
                        // show notification
                        showNotification(context, "Message sent")
                    }
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                        // update msg on fireBase
                        updateMsg("GENERIC FAILURE" , "GENERIC FAILURE")
                        // show notification
                        showNotification(context, " GENERIC FAILURE")
                    }
                    SmsManager.RESULT_ERROR_NO_SERVICE -> {
                        // update msg on fireBase
                        updateMsg("NO SERVICE" , "NO SERVICE")
                        // show notification
                        showNotification(context, " NO SERVICE")
                    }
                    SmsManager.RESULT_ERROR_NULL_PDU -> {
                        // update msg on fireBase
                        updateMsg("NULL PDU" , "NO SERVICE")
                        // show notification
                        showNotification(context, " NULL PDU")
                    }
                    SmsManager.RESULT_ERROR_RADIO_OFF -> {
                        // update msg on fireBase
                        updateMsg("RADIO OFF" , "RADIO OFF")
                        // show notification
                        showNotification(context, " RADIO OFF")
                    }
                }
            }
        }

        smsDeliveredReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, arg1: Intent) {
                when (resultCode){
                    Activity.RESULT_OK -> {
                        // update msg on fireBase
                        updateMsg("Completed" , "Delivered" )
                        // show notification
                        showNotification(context, "Message Delivered")
                    }
                    Activity.RESULT_CANCELED -> {
                        // update msg on fireBase
                        updateMsg("Not","Not Delivered")
                        // show notification
                        showNotification(context, "Message not delivered")
                    }
                }
            }
        }

        context?.applicationContext?.registerReceiver(smsSentReceiver, IntentFilter("Message sent"))
        context?.applicationContext?.registerReceiver(smsDeliveredReceiver, IntentFilter("message delivered"))

    }

    private fun updateMsg(Status: String, Delivered :String){

        val message = MessageFirebase(smsId, smsPersonName, smsPersonNumber, smsMessage, smsDate, smsTime,
            Status, smsSendVia, userID, smsCalender , Delivered)
        // update msg on fireBase
        databaseReferenceMsg.child(smsId).setValue(message)
    }

    private fun showNotification(context: Context?, message: String) {
        val notificationHelper = NotificationHelper(context!!, smsId,smsPersonName, smsPersonNumber, smsMessage, smsDate, smsTime, smsStatus, smsSendVia, userID, message)
        val notification = notificationHelper.getChannelNotification()
        notificationHelper.getManager().notify(smsId.hashCode(), notification.build())
    }



}
