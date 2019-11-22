package com.alialfayed.deersms.utils

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.telephony.SmsManager
import android.widget.Toast
import com.alialfayed.deersms.model.MessageFirebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class WhatsAppReceiver : BroadcastReceiver() {
    private lateinit var sentPendingIntent: PendingIntent
    private lateinit var deliveredPendingIntent: PendingIntent
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

        context!!.startService(Intent(context.applicationContext, WhatsappAccessibilityService::class.java))

        databaseReferenceMsg = FirebaseDatabase.getInstance().getReference("Messages")

        sentPendingIntent = PendingIntent.getBroadcast(context, 0, Intent("Message sent"), 0)
        deliveredPendingIntent =
            PendingIntent.getBroadcast(context, 0, Intent("message delivered"), 0)

        smsId = intent?.extras?.getString("WhatsAppId")!!
        smsPersonName = intent.extras?.getString("PersonName")!!
        smsPersonNumber = intent.extras?.getString("PersonNumber")!!
        smsMessage = intent.extras?.getString("WhatsAppAppMessage")!!
        smsDate = intent.extras?.getString("WhatsAppDate")!!
        smsTime = intent.extras?.getString("WhatsAppTime")!!
        smsStatus = intent.extras?.getString("WhatsAppStatus")!!
        smsSendVia = intent.extras?.getString("WhatsAppSendVia")!!
        userID = intent.extras?.getString("UserID")!!
        smsCalender = intent.extras!!.getLong("calendar")
        smsDelivered = intent.extras?.getString("WhatsAppDelivered")!!



        WhatsappAccessibilityService.sActive = true
        WhatsappAccessibilityService.sPhone = smsPersonNumber
        WhatsappAccessibilityService.sContact = smsPersonName
        WhatsappAccessibilityService.sMsg = smsMessage
        val intent =  Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + smsPersonNumber.replace("+","") + "&text=" + WhatsappAccessibilityService.sMsg))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        updateMsg("Completed" ,"Sent")

        smsSentReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, arg1: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        updateMsg("Completed" ,"Sent")
                        showNotification(context, "message sent")
                    }
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                        updateMsg("GENERIC FAILURE" , "GENERIC FAILURE" )
                        showNotification(context, " GENERIC FAILURE")
                    }
                    SmsManager.RESULT_ERROR_NO_SERVICE -> {
                        updateMsg("NO SERVICE" , "NO SERVICE")
                        showNotification(context, " NO SERVICE")
                    }
                    SmsManager.RESULT_ERROR_NULL_PDU -> {
                        updateMsg("NULL PDU" , "NO SERVICE")
                        showNotification(context, " NULL PDU")
                    }
                    SmsManager.RESULT_ERROR_RADIO_OFF -> {
                        updateMsg("RADIO OFF" , "RADIO OFF")
                        showNotification(context, " RADIO OFF")
                    }
                }
            }
        }

        smsDeliveredReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, arg1: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        Toast.makeText(context, "message delivered", Toast.LENGTH_SHORT).show()
                        updateMsg("Completed" , "Delivered" )
                        showNotification(context, "message delivered")
                    }
                    Activity.RESULT_CANCELED -> {
                        Toast.makeText(context, "message not delivered", Toast.LENGTH_SHORT).show()
                        updateMsg("Not","Not Delivered")
                        showNotification(context, "message not delivered")
                    }
                }
            }
        }

        context?.applicationContext?.registerReceiver(smsSentReceiver, IntentFilter("message sent"))
        context?.applicationContext?.registerReceiver(
            smsDeliveredReceiver,
            IntentFilter("message delivered")
        )

    }
    private fun updateMsg(messageStatus: String , messageDelivered :String) {

        val message = MessageFirebase(
            smsId, smsPersonName, smsPersonNumber, smsMessage, smsDate, smsTime,
            messageStatus, smsSendVia, userID, smsCalender , messageDelivered
        )
        // update msg on fireBase
        databaseReferenceMsg.child(smsId).setValue(message)
    }

    private fun showNotification(context: Context?, msg: String) {
        val notificationHelper = NotificationHelper(
            context!!,smsId,smsPersonName,smsPersonNumber,smsMessage,smsDate,smsTime,smsStatus,smsSendVia,userID, msg )
        val nb = notificationHelper.getChannelNotification()
        notificationHelper.getManager().notify(smsId.hashCode(), nb.build())
    }



}