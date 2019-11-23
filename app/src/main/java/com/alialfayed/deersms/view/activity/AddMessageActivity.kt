package com.alialfayed.deersms.view.activity

import android.app.*
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.speech.RecognizerIntent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.alialfayed.deersms.R
import com.alialfayed.deersms.utils.SMSReceiver
import com.alialfayed.deersms.viewmodel.AddMessageViewModel
import kotlinx.android.synthetic.main.activity_add_message.*
import kotlinx.android.synthetic.main.activity_current_sim.*
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class AddMessageActivity : AppCompatActivity() {

    private lateinit var addMessageViewModel: AddMessageViewModel

    private lateinit var calendarAlarm: Calendar
    private lateinit var currentDate: String
    private lateinit var currentTime: String

    private var calender: DatePickerDialog.OnDateSetListener? = null
    private var timePickerDialog: TimePickerDialog? = null
    private var date: Date? = null
    private var myDateCheck: Date? = null
    private var mHour = 0
    private var mMin = 0
    private var hours1 = 0
    private var min1 = 0
    private var year1 = 0
    private var month1 = 0
    private var dayOfMonth1 = 0

    var groupChecker: Boolean = false
    var isSendNow: Boolean = true

    private val REQ_CODE = 100

    private val requestSendSMS = 2
    private val requestReadState = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_message)

        addMessageViewModel = ViewModelProviders.of(this).get(AddMessageViewModel::class.java)
        addMessageViewModel.setAddMessage(this)
        addMessageViewModel.setInitComponentaddMessageActivity()

        calender()
        timesender()
        calendarAlarm = Calendar.getInstance()
        currentDate = DateFormat.getDateInstance().format(calendarAlarm.time)
        currentTime = DateFormat.getTimeInstance().format(calendarAlarm.time)



        /**
         * button do send Message
         */
        imageBtnSend_AddMessage.setOnClickListener {

            if (addMessageViewModel.checkPermission(android.Manifest.permission.READ_CONTACTS)
                && addMessageViewModel.checkPermission(android.Manifest.permission.READ_PHONE_STATE)
                && addMessageViewModel.checkPermission(android.Manifest.permission.READ_PHONE_STATE)
            ) {
                if (!addMessageViewModel.checkerButtonSend) {
//                  if checkerButtonSend = false
//                  WhatsApp



                }else{
//                    if checkerButtonSend = false
//                      WhatsApp
                    if (!groupChecker) {
                        /**
                         * One Contact
                         */
                        if (isSendNow) {
                            addMessageViewModel.addNowAddMessage(currentDate, currentTime)
                            addMessageViewModel.finishNowSend()
                            Toast.makeText(this, "Done SMS Now ", Toast.LENGTH_SHORT).show()
                        } else {
                            addMessageViewModel.addScheduleAddMessage()
                            addMessageViewModel.finishScheduleSend()
                            Toast.makeText(this, "Done SMS Schedule ", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        /**
                         * Group Contacts
                         */
                        if (isSendNow) {
                            for (i in addMessageViewModel.phoneGroup!!.indices) {
                                if (addMessageViewModel.phoneGroup.size >= 0) {
                                    addMessageViewModel.addGroupNowAddMessage(addMessageViewModel.phoneGroup[i],currentDate, currentTime)
                                }
                            }
                            addMessageViewModel.finishGroupNowSend()
                            Toast.makeText(this, "Done SMS Group Now ", Toast.LENGTH_SHORT).show()
                        }else{
                            for (i in addMessageViewModel.phoneGroup!!.indices) {
                                if (addMessageViewModel.phoneGroup.size >= 0) {
                                    addMessageViewModel.addGroupScheduleAddMessage(addMessageViewModel.phoneGroup[i])
                                }
                            }
                            addMessageViewModel.finishSGroupcheduleSend()
                            Toast.makeText(this, "Done SMS Group Schedule ", Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }
        }
        /**
         * editText Reset Activity
         */
        imageBtnReset.setOnClickListener {
            addMessageViewModel.resetActivity()
        }
        /**
         * button check group or contacts
         */
        imageBtnChecker_AddMessage.setOnClickListener {
            addMessageViewModel.checker()
        }
        /**
         * Speaker
         */
        imageBtnSpeaker_AddMessage.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please Speak Now")
            try {
                startActivityForResult(intent, REQ_CODE)
            } catch (a: ActivityNotFoundException) {
                Toast.makeText(
                    applicationContext,
                    "Sorry your device not supported",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        /**
         * button get template Message
         */
        imageBtnAttach_AddMessage.setOnClickListener {
            addMessageViewModel.template()
        }
        /**
         * button get contacts
         */
        imageBtnContacts_AddMessage.setOnClickListener {
            addMessageViewModel.getContacts()
            addMessageViewModel.checker()
            edtNumber_AddMessage.setVisibility(View.VISIBLE)
            edtNumber_AddMessage.setText("")
            edtname_AddMessage.setText("")
            edtname_AddMessage.setEnabled(true)
            edtGroup_AddMessage.setVisibility(View.GONE)
            groupChecker = false

        }
        /**
         * button get Groups
         */
        imageBtnGroups_AddMessage.setOnClickListener {
            addMessageViewModel.getGroups()
            addMessageViewModel.checker()
            edtNumber_AddMessage.setVisibility(View.GONE)
            edtGroup_AddMessage.setVisibility(View.VISIBLE)
            edtGroup_AddMessage.setText("")
            edtname_AddMessage.setText("")


        }

    }



    fun setSMSAlarm(
        smsId: String, smsPersonName: String, smsPersonNumber: String, smsMessage: String,
        smsDate: String, SmsTime: String, SmsStatus: String, smsSendVia: String, UserID: String,
        smsCalender: Long, SmsDelivered: String
    ) {

        val alarmManager: AlarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this@AddMessageActivity, SMSReceiver::class.java)
        intent.putExtra("SmsId", smsId)
        intent.putExtra("SmsPersonName", smsPersonName)
        intent.putExtra("SmsPersonNumber", smsPersonNumber)
        intent.putExtra("SmsMessage", smsMessage)
        intent.putExtra("SmsDate", smsDate)
        intent.putExtra("SmsTime", SmsTime)
        intent.putExtra("SmsStatus", SmsStatus)
        intent.putExtra("SmsSendVia", smsSendVia)
        intent.putExtra("UserID", UserID)
        intent.putExtra("SmsCalender", smsCalender)
        intent.putExtra("SmsDelivered", SmsDelivered)

        val pendingIntent: PendingIntent =PendingIntent.getBroadcast(this@AddMessageActivity, smsId.hashCode(), intent, 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact( AlarmManager.RTC_WAKEUP, calendarAlarm.timeInMillis, pendingIntent )
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendarAlarm.timeInMillis, pendingIntent)
        }

    }


    /**
     * Check Radio Button time
     */
    private fun timesender() {
        radioBtnNow_AddMessage.setOnClickListener {
            Toast.makeText(this, "Message Send Now", Toast.LENGTH_SHORT).show()
            isSendNow = true
        }
        radioBtnCustom_AddMessage.setOnClickListener {
            checkerButtonTime_AddMessage.setVisibility(View.GONE)
            linearLayout_AddMessage.setVisibility(View.VISIBLE)
            isSendNow = false
            Toast.makeText(this, "Message Send Custom Time", Toast.LENGTH_SHORT).show()
        }
        editDate_AddMessage.setOnClickListener {
            selectDate()
        }
        editTime_AddMessage.setOnClickListener {
            selectTime()
        }


    }

    private fun selectDate() {

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val dialog = DatePickerDialog(this@AddMessageActivity, calender, year, month, day)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog.show()

    }

    private fun selectTime() {
        val c = Calendar.getInstance()
        mHour = c.get(Calendar.HOUR_OF_DAY)
        mMin = c.get(Calendar.MINUTE)

        timePickerDialog = TimePickerDialog(this@AddMessageActivity,
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minutes ->
                val myCalInstance = Calendar.getInstance()
                val myRealCalender = Calendar.getInstance()

                if (myDateCheck == null) {
                    val timeStamp = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
                    val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    try {
                        myDateCheck = format.parse(timeStamp)
                    } catch (e: ParseException) {
                        e.printStackTrace()
                    }

                }

                myRealCalender.time = myDateCheck
                myRealCalender.set(Calendar.HOUR_OF_DAY, hourOfDay)
                myRealCalender.set(Calendar.MINUTE, minutes)

                if (myRealCalender.time.before(myCalInstance.time)) {
                    Toast.makeText(this@AddMessageActivity,
                        "Please, Enter a valid Time!", Toast.LENGTH_LONG).show()
                } else {
                    hours1 = hourOfDay
                    min1 = minutes
                    calendarAlarm = Calendar.getInstance()
                    calendarAlarm.set(Calendar.YEAR, year1)
                    calendarAlarm.set(Calendar.MONTH, month1 - 1)
                    calendarAlarm.set(Calendar.DAY_OF_MONTH, dayOfMonth1)
                    calendarAlarm.set(Calendar.HOUR_OF_DAY, hours1)
                    calendarAlarm.set(Calendar.MINUTE, min1)
                    calendarAlarm.set(Calendar.SECOND, 0)
                    if (hourOfDay < 10 && minutes >= 10) {
                        editTime_AddMessage.setText("0$hourOfDay:$minutes")
                    } else if (hourOfDay < 10 && minutes < 10) {
                        editTime_AddMessage.setText("0$hourOfDay:0$minutes")
                    } else if (hourOfDay >= 10 && minutes < 10) {
                        editTime_AddMessage.setText("$hourOfDay:0$minutes")
                    } else if (hourOfDay >= 10 && minutes >= 10) {
                        editTime_AddMessage.setText("$hourOfDay:$minutes")
                    }
                }
            }, mHour, mMin, false
        )
        timePickerDialog!!.show()
    }

    fun calender() {
        calender = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            var month = month
            month += 1

            year1 = year
            month1 = month
            dayOfMonth1 = dayOfMonth

            val startDate = "$dayOfMonth/$month/$year"
            val time = SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().time)
            val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            try {
                date = format.parse(time)
                myDateCheck = format.parse(startDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            when {
                myDateCheck == null -> try {
                    myDateCheck = format.parse(time)
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                myDateCheck!!.before(date) -> Toast.makeText(this@AddMessageActivity,
                    "Please, Enter a valid Date!", Toast.LENGTH_LONG).show()
                else -> {
                    editDate_AddMessage.setText(startDate)
                    editTime_AddMessage.setText("")
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == requestSendSMS && requestCode == requestReadState) {
            addMessageViewModel.addNowAddMessage(currentDate,currentTime)
            addMessageViewModel.addScheduleAddMessage()
        } else {
            Toast.makeText(this,"Sorry , Please Accept Permissions To Send Message", Toast.LENGTH_SHORT ).show()
        }
    }

    override fun onBackPressed() {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQ_CODE -> {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    //if (resultCode == RESULT_OK && null ! = data) {
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    //edttxt.setText(result.get(0));
                    if (result != null && !result.isEmpty()) {
                        edtMessage_AddMessage.setText(result[0])
                    }
                }
            }
        }
    }




}
