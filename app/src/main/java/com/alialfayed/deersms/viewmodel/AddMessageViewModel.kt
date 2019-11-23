package com.alialfayed.deersms.viewmodel

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.alialfayed.deersms.R
import com.alialfayed.deersms.repository.FirebaseHandler
import com.alialfayed.deersms.view.activity.*
import kotlinx.android.synthetic.main.activity_add_message.*
import kotlinx.android.synthetic.main.activity_current_sim.*
import java.util.*


/**
 * Class do :
 * Created by ( Eng Ali)
 */
class AddMessageViewModel : ViewModel() {

    lateinit var addMessageActivity: AddMessageActivity
    lateinit var firebaseHandler: FirebaseHandler
    private  var calendarAlarm: Calendar  = Calendar.getInstance()
    var checkerContactorGroups: Boolean = true
    private val resultTemplate = 0
    private lateinit var checkerButtonTime: RadioGroup
    private lateinit var radioGroup_SendVia: RadioGroup
    var checkerButtonSend: Boolean = true
    var isSendNow: Boolean = true
    var phoneGroup = ArrayList<String>()
    val MSG_ID = "SMS"



    fun setAddMessage(activity: Activity) {
        this.addMessageActivity = activity as AddMessageActivity
        this.firebaseHandler = FirebaseHandler(activity, this)

    }



    fun viewModeSendMessage(
        personName: String, personNumber: String, SMSMessage: String,
        SMSDate: String, SMSTime: String, SMSStatus: String, SMSSendVia: String,
        SMSDelivered: String, smsCalender: Long
    ) {
        firebaseHandler.scheduleMessageNoPlanRepository(
            personName, personNumber, SMSMessage, SMSDate,
            SMSTime, SMSStatus, SMSSendVia, SMSDelivered, smsCalender  )
    }

    fun addNowAddMessage(date: String, time: String) {
        val phoneText = addMessageActivity.edtNumber_AddMessage.text.toString().trim()
        val messageText = addMessageActivity.edtMessage_AddMessage.text.toString().trim()
        val nameText = addMessageActivity.edtname_AddMessage.text.toString().trim()

        if (phoneText.isEmpty()) {
            addMessageActivity.edtNumber_AddMessage.error = "Phone Number mustn't be empty"
            addMessageActivity.edtNumber_AddMessage.requestFocus()
            return
        } else if (nameText.isEmpty()) {
            addMessageActivity.edtname_AddMessage.error = "Name mustn't be empty!"
            addMessageActivity.edtname_AddMessage.requestFocus()
            return
        } else if (messageText.isEmpty()) {
            addMessageActivity.edtMessage_AddMessage.error = "Message mustn't be empty!"
            addMessageActivity.edtMessage_AddMessage.requestFocus()
            return
        } else {
            viewModeSendMessage(nameText,phoneText, messageText, date, time,"Completed","SMS","Sent", calendarAlarm.timeInMillis )
        }

    }

    fun addScheduleAddMessage() {
        val phoneText = addMessageActivity.edtNumber_AddMessage.text.toString().trim()
        val messageText = addMessageActivity.edtMessage_AddMessage.text.toString().trim()
        val nameText = addMessageActivity.edtname_AddMessage.text.toString().trim()
        val date = addMessageActivity.editDate_AddMessage.text.toString().trim()
        val time = addMessageActivity.editTime_AddMessage.text.toString().trim()

        if (phoneText.isEmpty()) {
            addMessageActivity.edtNumber_AddMessage.error = "Phone Number mustn't be empty"
            addMessageActivity.edtNumber_AddMessage.requestFocus()
            return
        } else if (nameText.isEmpty()) {
            addMessageActivity.edtname_AddMessage.error = "Name mustn't be empty!"
            addMessageActivity.edtname_AddMessage.requestFocus()
            return
        } else if (messageText.isEmpty()) {
            addMessageActivity.edtMessage_AddMessage.error = "Message mustn't be empty!"
            addMessageActivity.edtMessage_AddMessage.requestFocus()
            return
        } else if (date.isEmpty()) {
            Toast.makeText(addMessageActivity, "Enter a valid Date!", Toast.LENGTH_SHORT).show()
            return
        } else if (time.isEmpty()) {
            Toast.makeText(addMessageActivity, "Enter a valid Time!", Toast.LENGTH_SHORT).show()
            return
        } else {
            viewModeSendMessage(  nameText, phoneText,  messageText,
                date,  time,"Pending",  "SMS", "Pending Sent",
                calendarAlarm.timeInMillis
            )
        }

    }

    // Group
    fun addGroupNowAddMessage(number: String , date: String , time: String) {
        val phoneText = addMessageActivity.edtGroup_AddMessage.text.toString().trim()
        val messageText = addMessageActivity.edtMessage_AddMessage.text.toString().trim()
        val nameText = addMessageActivity.edtname_AddMessage.text.toString().trim()

        if (phoneText.isEmpty()) {
            addMessageActivity.edtNumber_AddMessage.error = "Phone Number mustn't be empty"
            addMessageActivity.edtNumber_AddMessage.requestFocus()
            return
        } else if (nameText.isEmpty()) {
            addMessageActivity.edtname_AddMessage.error = "Name mustn't be empty!"
            addMessageActivity.edtname_AddMessage.requestFocus()
            return
        } else if (messageText.isEmpty()) {
            addMessageActivity.edtMessage_AddMessage.error = "Message mustn't be empty!"
            addMessageActivity.edtMessage_AddMessage.requestFocus()
            return
        } else {
            viewModeSendMessage(nameText,number, messageText, date, time,"Completed","SMS","Sent", calendarAlarm.timeInMillis )
        }

    }

    fun addGroupScheduleAddMessage(number: String) {
        val phoneText = addMessageActivity.edtGroup_AddMessage.text.toString().trim()
        val messageText = addMessageActivity.edtMessage_AddMessage.text.toString().trim()
        val nameText = addMessageActivity.edtname_AddMessage.text.toString().trim()
        val date = addMessageActivity.editDate_AddMessage.text.toString().trim()
        val time = addMessageActivity.editTime_AddMessage.text.toString().trim()

        if (phoneText.isEmpty()) {
            addMessageActivity.edtNumber_AddMessage.error = "Phone Number mustn't be empty"
            addMessageActivity.edtNumber_AddMessage.requestFocus()
            return
        } else if (nameText.isEmpty()) {
            addMessageActivity.edtname_AddMessage.error = "Name mustn't be empty!"
            addMessageActivity.edtname_AddMessage.requestFocus()
            return
        } else if (messageText.isEmpty()) {
            addMessageActivity.edtMessage_AddMessage.error = "Message mustn't be empty!"
            addMessageActivity.edtMessage_AddMessage.requestFocus()
            return
        } else if (date.isEmpty()) {
            Toast.makeText(addMessageActivity, "Enter a valid Date!", Toast.LENGTH_SHORT).show()
            return
        } else if (time.isEmpty()) {
            Toast.makeText(addMessageActivity, "Enter a valid Time!", Toast.LENGTH_SHORT).show()
            return
        } else {
            viewModeSendMessage(  nameText, number,  messageText,
                date,  time,"Pending",  "SMS", "Pending Sent",
                calendarAlarm.timeInMillis
            )
        }

    }

    fun setSMSAlarm(
        SMSId: String, personName: String, personNumber: String, SMSMessage: String,
        SMSDate: String, SMSTime: String, SMSStatus: String, SMSSendVia: String,
        UserID: String, smsCalender: Long, SMSDelivered: String ) {

        addMessageActivity.setSMSAlarm(
            SMSId, personName,   personNumber, SMSMessage, SMSDate,  SMSTime, SMSStatus,
            SMSSendVia,  UserID, smsCalender, SMSDelivered )
    }


    fun setInitComponentaddMessageActivity() {
        checkerButtonTime = addMessageActivity.checkerButtonTime_AddMessage
        checkerButtonTime.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.radioBtnNow_AddMessage -> {
                    isSendNow = true
                }
                R.id.radioBtnCustom_AddMessage -> {
                    isSendNow = false
                }
            }
        }

        radioGroup_SendVia = addMessageActivity.checkerButtonVia_AddMessage
        radioGroup_SendVia.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.radioBtnWhatsApp_AddMessage -> {
                    checkerButtonSend = false
                    addMessageActivity.checkerButtonTime_AddMessage.setVisibility(View.GONE)
                    addMessageActivity.textView9.setVisibility(View.GONE)
                }
                R.id.radioBtnSMS_AddMessage -> {
                    checkerButtonSend = true
                    addMessageActivity.checkerButtonTime_AddMessage.setVisibility(View.VISIBLE)
                    addMessageActivity.textView9.setVisibility(View.VISIBLE)
                    addMessageActivity.linearLayout_AddMessage.setVisibility(View.GONE)

                }
            }
        }

        if (addMessageActivity.intent.getStringExtra("phoneContact") != null || addMessageActivity.intent.getStringExtra("nameContact" ) != null ) {
            val textName: String = addMessageActivity.intent.getStringExtra("nameContact")
            addMessageActivity.edtname_AddMessage.setText(textName)
            val textPhone: String = addMessageActivity.intent.getStringExtra("phoneContact")
            addMessageActivity.edtNumber_AddMessage.setText(textPhone)
            if (addMessageActivity.intent.getStringExtra("Template") == null) {
                val textTemplate: String =
                    addMessageActivity.getText(R.string.type_your_message).toString()
                addMessageActivity.edtMessage_AddMessage.setHint(textTemplate)
            } else {
                val textTemplate: String = addMessageActivity.intent.getStringExtra("Template")
                addMessageActivity.edtMessage_AddMessage.setText(textTemplate)
            }
        }
        if (addMessageActivity.intent.getStringExtra("Template") != null) {
            val textName: String = addMessageActivity.intent.getStringExtra("Template")
            addMessageActivity.edtMessage_AddMessage.setText(textName)
            if (addMessageActivity.intent.getStringExtra("TPhone") != null && addMessageActivity.intent.getStringExtra("TName") != null
            ) {
                val textName: String = addMessageActivity.intent.getStringExtra("TName")
                addMessageActivity.edtname_AddMessage.setText(textName)
                val textPhone: String = addMessageActivity.intent.getStringExtra("TPhone")
                addMessageActivity.edtNumber_AddMessage.setText(textPhone)
            } else if (addMessageActivity.intent.getStringExtra("TPhone") != null) {
                val textPhone: String = addMessageActivity.intent.getStringExtra("TPhone")
                addMessageActivity            } else if (addMessageActivity.intent.getStringExtra("TName") != null) {
                var textName: String = addMessageActivity.intent.getStringExtra("TName")
                addMessageActivity.edtname_AddMessage.setText(textName)
            } else {
                val textName: String = addMessageActivity.getText(R.string.name).toString()
                addMessageActivity.edtname_AddMessage.setHint(textName)
                val textPhone: String = addMessageActivity.getText(R.string.choose_number).toString()
                addMessageActivity.edtNumber_AddMessage.setHint(textPhone)
            }
        }
        if (addMessageActivity.intent.getStringArrayListExtra("phoneGroup") != null || addMessageActivity.intent.getStringExtra("nameGroup") != null) {
            addMessageActivity.edtNumber_AddMessage.setVisibility(View.GONE)
            addMessageActivity.edtGroup_AddMessage.setVisibility(View.VISIBLE)
            phoneGroup = addMessageActivity.intent.getStringArrayListExtra("phoneGroup")
            val nameGroup = addMessageActivity.intent.getStringExtra("nameGroup")
            addMessageActivity.edtname_AddMessage.setText(nameGroup)
            addMessageActivity.edtGroup_AddMessage.setText(phoneGroup.toString())
            addMessageActivity.edtname_AddMessage.setEnabled(false)
            addMessageActivity.groupChecker = true
        }

    }

    fun getContacts() {
        val intent = Intent(addMessageActivity,ContactsSMSActivity::class.java)
        intent.putExtra("SMSMode","goBack")
        addMessageActivity.startActivity(intent)
        addMessageActivity.finish()
    }

    fun getGroups() {
        val intent = Intent(addMessageActivity,GroupContactsActivity::class.java)
        intent.putExtra("SMSMode","goBack")
        addMessageActivity.startActivity(intent)
        addMessageActivity.finish()
    }

    fun resetActivity() {
//    addMessageActivity.edtNumber_AddMessage.setText("")
//    addMessageActivity.edtMessage_AddMessage.setText("")
//    addMessageActivity.txtDate_AddMessage.setText("")
//    addMessageActivity.txtTime_AddMessage.setText("")
//    addMessageActivity.checkerButtonTime_AddMessage.setVisibility(View.VISIBLE)
//    addMessageActivity.linearLayout_AddMessage.setVisibility(View.GONE)
        addMessageActivity.startActivity(Intent(addMessageActivity, AddMessageActivity::class.java))


    }

    fun template() {
        val phone = addMessageActivity.edtNumber_AddMessage.text.toString()
        val name = addMessageActivity.edtname_AddMessage.text.toString()
        val intent = Intent(addMessageActivity, TemplatesActivity::class.java)
        intent.putExtra("teName", name)
        intent.putExtra("tePhone", phone)
        intent.putExtra("CurrentSIM","CurrentSIM")
        addMessageActivity.startActivityForResult(intent, resultTemplate)
        addMessageActivity.finish()

    }

    fun checker() {
        if (checkerContactorGroups){
            addMessageActivity.imageBtnContacts_AddMessage.setVisibility(View.VISIBLE)
            addMessageActivity.imageBtnGroups_AddMessage.setVisibility(View.VISIBLE)
            checkerContactorGroups = false
        }else{
            addMessageActivity.imageBtnContacts_AddMessage.setVisibility(View.GONE)
            addMessageActivity.imageBtnGroups_AddMessage.setVisibility(View.GONE)
            checkerContactorGroups = true
        }
    }

    fun checkPermission(permission: String): Boolean {
        val check: Int = ContextCompat.checkSelfPermission(addMessageActivity, permission)
        return (check == PackageManager.PERMISSION_GRANTED)
    }

    fun finishNowSend() {
        val phone = addMessageActivity.edtNumber_AddMessage.text.toString().trim()
        val message = addMessageActivity.edtMessage_AddMessage.text.toString().trim()
        val name = addMessageActivity.edtname_AddMessage.text.toString().trim()
        if (phone.isEmpty() || message.isEmpty() || name.isEmpty()) {
            Toast.makeText(addMessageActivity, "Please Check Your Fields ", Toast.LENGTH_SHORT).show()
        } else {
            addMessageActivity.startActivity(Intent(addMessageActivity, HomeActivity::class.java))
        }
    }

    fun finishScheduleSend() {
        val phone = addMessageActivity.edtNumber_AddMessage.text.toString().trim()
        val message = addMessageActivity.edtMessage_AddMessage.text.toString().trim()
        val name = addMessageActivity.edtname_AddMessage.text.toString().trim()
        val date = addMessageActivity.editDate_AddMessage.text.toString().trim()
        val time = addMessageActivity.editTime_AddMessage.text.toString().trim()
        if (phone.isEmpty() || message.isEmpty() || name.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(addMessageActivity, "Please Check Your Fields ", Toast.LENGTH_SHORT).show()
        } else {
            addMessageActivity.startActivity(Intent(addMessageActivity, HomeActivity::class.java))
        }
    }

    fun finishGroupNowSend() {
        val phone = addMessageActivity.edtGroup_AddMessage.text.toString().trim()
        val message = addMessageActivity.edtMessage_AddMessage.text.toString().trim()
        val name = addMessageActivity.edtname_AddMessage.text.toString().trim()
        if (phone.isEmpty() || message.isEmpty() || name.isEmpty()) {
            Toast.makeText(addMessageActivity, "Please Check Your Fields ", Toast.LENGTH_SHORT).show()
        } else {
            addMessageActivity.startActivity(Intent(addMessageActivity, HomeActivity::class.java))
        }
    }

    fun finishSGroupcheduleSend() {
        val phone = addMessageActivity.edtGroup_AddMessage.text.toString().trim()
        val message = addMessageActivity.edtMessage_AddMessage.text.toString().trim()
        val name = addMessageActivity.edtname_AddMessage.text.toString().trim()
        val date = addMessageActivity.editDate_AddMessage.text.toString().trim()
        val time = addMessageActivity.editTime_AddMessage.text.toString().trim()
        if (phone.isEmpty() || message.isEmpty() || name.isEmpty() || date.isEmpty() || time.isEmpty()) {
            Toast.makeText(addMessageActivity, "Please Check Your Fields ", Toast.LENGTH_SHORT).show()
        } else {
            addMessageActivity.startActivity(Intent(addMessageActivity, HomeActivity::class.java))
        }
    }
}