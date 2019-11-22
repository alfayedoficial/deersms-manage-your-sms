package com.alialfayed.deersms.view.adabter

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.alialfayed.deersms.R
import com.alialfayed.deersms.model.MessageFirebase
import com.alialfayed.deersms.utils.SMSReceiver
import com.alialfayed.deersms.view.activity.HomeActivity
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.ArrayList

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class PendingAdabter : RecyclerView.Adapter<PendingAdabter.ViewHolder> {

    private var dataPendingAdabter = ArrayList<MessageFirebase>()
    internal var activity: Activity
    private var mdatabaseReference = FirebaseDatabase.getInstance().getReference("Messages")

    constructor(activity: Activity) {
        this.activity = activity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_pending, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (dataPendingAdabter.size > 0) {
            dataPendingAdabter.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val message = dataPendingAdabter[position]
        holder.getTxtTime()!!.text = message.getSmsTime()
        holder.getTxtDate()!!.text = message.getSmsDate()
        holder.getTxtContacts()!!.text = message.getSmsPersonName()
        holder.getTxtMessage()!!.text = message.getSmsMessage()

        if (message.getsmsSendVia() == "SMS") {
            holder.getImageSender()!!.setImageResource(R.drawable.ic_sms)
        } else {
            holder.getImageSender()!!.setImageResource(R.drawable.ic_whatsapp)
        }


        holder.getBtnDelete()!!.setOnClickListener {
            mdatabaseReference.child(message.getSmsId()).removeValue()
            val alarRemoved = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intentRemoved = Intent(activity, SMSReceiver::class.java)
            intentRemoved.putExtra("SmsId", message.getSmsId())
            intentRemoved.putExtra("SmsPersonName", message.getSmsPersonName())
            intentRemoved.putExtra("SmsPersonNumber", message.getSmsPersonNumber())
            intentRemoved.putExtra("SmsMessage", message.getSmsMessage())
            intentRemoved.putExtra("SmsDate", message.getSmsDate())
            intentRemoved.putExtra("SmsTime", message.getSmsTime())
            intentRemoved.putExtra("SmsStatus", message.getSmsStatus())
            intentRemoved.putExtra("SmsSendVia", message.getsmsSendVia())
            intentRemoved.putExtra("UserID", message.getUserID())
            intentRemoved.putExtra("SmsCalender", message.getSmsCalender())
            intentRemoved.putExtra("SmsDelivered",message.getSmsDelivered())

            val pendingIntent = PendingIntent.getBroadcast(
                activity, message.getSmsId().hashCode(),intentRemoved,PendingIntent.FLAG_UPDATE_CURRENT)
            alarRemoved.cancel(pendingIntent)

            Toast.makeText( activity, "Message Deleted Successfully!", Toast.LENGTH_SHORT ).show()

        }


        holder.getBtnEdit()!!.setOnClickListener {
            //TODO Edite Activity
            val editIntent = Intent(activity, HomeActivity::class.java)
            editIntent.putExtra("SmsId", message.getSmsId())
            editIntent.putExtra("SmsPersonName", message.getSmsPersonName())
            editIntent.putExtra("SmsPersonNumber", message.getSmsPersonNumber())
            editIntent.putExtra("SmsMessage", message.getSmsMessage())
            editIntent.putExtra("SmsDate", message.getSmsDate())
            editIntent.putExtra("SmsTime", message.getSmsTime())
            editIntent.putExtra("SmsStatus", message.getSmsStatus())
            editIntent.putExtra("SmsSendVia", message.getsmsSendVia())
            editIntent.putExtra("UserID", message.getUserID())
            editIntent.putExtra("SmsCalender", message.getSmsCalender())
            editIntent.putExtra("SmsDelivered",message.getSmsDelivered())
            activity.startActivity(editIntent)
        }


        holder.getBtnSend()!!.setOnClickListener {
            val alarSend = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intentSend = Intent(activity, SMSReceiver::class.java)
            intentSend.putExtra("SmsId", message.getSmsId())
            intentSend.putExtra("SmsPersonName", message.getSmsPersonName())
            intentSend.putExtra("SmsPersonNumber", message.getSmsPersonNumber())
            intentSend.putExtra("SmsMessage", message.getSmsMessage())
            intentSend.putExtra("SmsDate", message.getSmsDate())
            intentSend.putExtra("SmsTime", message.getSmsTime())
            intentSend.putExtra("SmsStatus", message.getSmsStatus())
            intentSend.putExtra("SmsSendVia", message.getsmsSendVia())
            intentSend.putExtra("UserID", message.getUserID())
            intentSend.putExtra("SmsCalender", message.getSmsCalender())
            intentSend.putExtra("SmsDelivered",message.getSmsDelivered())

            val pendingIntent = PendingIntent.getBroadcast(
                activity,  message.getSmsId().hashCode(), intentSend,PendingIntent.FLAG_UPDATE_CURRENT )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarSend.setExact(AlarmManager.RTC_WAKEUP,Calendar.getInstance().time.time, pendingIntent )
            } else {
                alarSend.set(AlarmManager.RTC_WAKEUP,Calendar.getInstance().time.time,pendingIntent )
            }
            Toast.makeText( activity, "Message Send Successfully!", Toast.LENGTH_SHORT).show()
        }


    }

    fun setDataToAdapter(dataPendingAdabter: ArrayList<MessageFirebase>) {
        this.dataPendingAdabter = dataPendingAdabter
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var txtTimeCardViewPending: TextView? = null
        private var txtDataCardViewPending: TextView? = null
        private var txtContactsCardViewPending: TextView? = null
        private var txtMessageCardViewPending: TextView? = null
        private var btnEditCardViewPending: Button? = null
        private var btnDeleteCardViewPending: Button? = null
        private var btnSendCardViewPending: Button? = null
        private var imageSender: ImageView? = null

        fun getTxtTime(): TextView? {
            if (txtTimeCardViewPending == null) {
                txtTimeCardViewPending = itemView.findViewById(R.id.txtTime_CardView_Pending)
            }
            return txtTimeCardViewPending
        }
        fun getTxtDate(): TextView? {
            if (txtDataCardViewPending == null) {
                txtDataCardViewPending = itemView.findViewById(R.id.txtData_CardView_pending)
            }
            return txtTimeCardViewPending
        }

        fun getTxtContacts(): TextView? {
            if (txtContactsCardViewPending == null) {
                txtContactsCardViewPending =
                    itemView.findViewById(R.id.txtContacts_CardView_pending)
            }
            return txtContactsCardViewPending
        }

        fun getTxtMessage(): TextView? {
            if (txtMessageCardViewPending == null) {
                txtMessageCardViewPending =
                    itemView.findViewById(R.id.txtMessage_CardView_pending)
            }
            return txtMessageCardViewPending
        }

        fun getImageSender(): ImageView? {
            if (imageSender == null) {
                imageSender = itemView.findViewById(R.id.imageSender)
            }
            return imageSender
        }

        fun getBtnEdit(): Button? {
            if (btnEditCardViewPending == null) {
                btnEditCardViewPending = itemView.findViewById(R.id.btnedit_CardView_pending)
            }
            return btnEditCardViewPending
        }

        fun getBtnDelete(): Button? {
            if (btnDeleteCardViewPending == null) {
                btnDeleteCardViewPending = itemView.findViewById(R.id.btnDelete_CardView_pending)
            }
            return btnDeleteCardViewPending
        }

        fun getBtnSend(): Button? {
            if (btnSendCardViewPending == null) {
                btnSendCardViewPending = itemView.findViewById(R.id.btnSend_CardView_pending)
            }
            return btnSendCardViewPending
        }


    }
}