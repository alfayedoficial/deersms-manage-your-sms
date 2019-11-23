package com.alialfayed.deersms.view.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.alialfayed.deersms.R
import com.alialfayed.deersms.model.GroupFirebase
import com.alialfayed.deersms.view.activity.AddMessageActivity
import com.alialfayed.deersms.view.activity.CurrentSIMActivity
import com.alialfayed.deersms.view.activity.WhatsAppActivity
import com.google.firebase.database.FirebaseDatabase

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class GroupAdabter : RecyclerView.Adapter<GroupAdabter.ViewHolder> {

    private var dataGroupAdabter = ArrayList<GroupFirebase>()
    internal var activity: Activity
    private var mdatabaseReference = FirebaseDatabase.getInstance().getReference("Groups")
    var positionlist = 0

    interface SingleChoiceListener {
        fun onPositiveButtonClicked(list: Array<String>, position: Int)
        fun onNegativeButtonClicked()
    }

    var mListener: SingleChoiceListener? = null

    constructor(activity: Activity) {
        this.activity = activity
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupAdabter.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.cardview_group, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return if (dataGroupAdabter.size > 0) {
            dataGroupAdabter.size
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: GroupAdabter.ViewHolder, position: Int) {
        val group = dataGroupAdabter[position]
        holder.getGroupName()!!.text = group.getGroupName()
        holder.getGroupNumber()!!.text = group.getGroupNumbers().size.toString()
        if (group.getKindGroup() == "SMS") {
            holder.getKindGroup()!!.setImageResource(R.drawable.ic_sms)
        } else {
            holder.getKindGroup()!!.setImageResource(R.drawable.ic_whatsapp)
        }

        holder.getBtnDelete()!!.setOnClickListener {
            mdatabaseReference.child(group.getGroupId()).removeValue()
        }
        holder.getCardViewGroup()!!.setOnClickListener {

            if (group.getKindGroup() == "SMS") {


                val builder = AlertDialog.Builder(activity)
                val list = activity.resources.getStringArray(R.array.choose_activityGroup)
                builder.setTitle("Choose Plan")
                    .setIcon(R.drawable.ic_logo)
                    .setSingleChoiceItems(list, positionlist) { _, i -> this.positionlist = i }
                    .setPositiveButton("Ok") { _, _ ->
                        when (positionlist) {
                            0 -> {
                                val nameGroup = group.getGroupName()
                                val phoneGroup = group.getGroupNumbers()
                                val listStringPhone = ArrayList<String>()
                                for (i in phoneGroup!!.indices) {
                                    if (phoneGroup.size >= 0) {
                                        listStringPhone.add(phoneGroup[i].number)
                                    }
                                }
                                val intent = Intent(activity, CurrentSIMActivity::class.java)
                                intent.putExtra("nameGroup", nameGroup)
                                intent.putExtra("phoneGroup", listStringPhone)
                                activity.startActivity(intent)
                                activity.finish()
                            }
                            1 -> {
                                val nameGroup = group.getGroupName()
                                val phoneGroup = group.getGroupNumbers()
                                val listStringPhone = ArrayList<String>()
                                for (i in phoneGroup!!.indices) {
                                    if (phoneGroup.size >= 0) {
                                        listStringPhone.add(phoneGroup[i].number)
                                    }
                                }
                                val intent = Intent(activity, AddMessageActivity::class.java)
                                intent.putExtra("nameGroup", nameGroup)
                                intent.putExtra("phoneGroup", listStringPhone)
                                activity.startActivity(intent)
                                activity.finish()
                            }
                        }
                    }
                    .setNegativeButton(
                        "Cancel"
                    ) { _, _ -> mListener?.onNegativeButtonClicked() }.show()

            } else {

                val nameGroup = group.getGroupName()
                val phoneGroup = group.getGroupNumbers()
                val listStringPhone = ArrayList<String>()
                for (i in phoneGroup!!.indices) {
                    if (phoneGroup.size >= 0) {
                        listStringPhone.add(phoneGroup[i].number)
                    }
                }
                val intent = Intent(activity, WhatsAppActivity::class.java)
                intent.putExtra("nameGroup", nameGroup)
                intent.putExtra("phoneGroup", listStringPhone)
                activity.startActivity(intent)
                activity.finish()            }

        }

    }

    fun setDataToAdapter(groupsList: java.util.ArrayList<GroupFirebase>) {
        this.dataGroupAdabter = groupsList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var txtGroupName_cardview_group: TextView? = null
        private var txtGroupnumber_cardview_group: TextView? = null
        private var btnDelete_cardview_group: Button? = null
        private var cardview_group: CardView? = null
        private var kindGroup: ImageView? = null


        fun getGroupName(): TextView? {
            if (txtGroupName_cardview_group == null) {
                txtGroupName_cardview_group =
                    itemView.findViewById(R.id.txtGroupName_cardview_group)
            }
            return txtGroupName_cardview_group
        }

        fun getGroupNumber(): TextView? {
            if (txtGroupnumber_cardview_group == null) {
                txtGroupnumber_cardview_group =
                    itemView.findViewById(R.id.txtGroupnumber_cardview_group)
            }
            return txtGroupnumber_cardview_group
        }

        fun getBtnDelete(): Button? {
            if (btnDelete_cardview_group == null) {
                btnDelete_cardview_group = itemView.findViewById(R.id.btnDelete_cardview_group)
            }
            return btnDelete_cardview_group
        }

        fun getCardViewGroup(): CardView? {
            if (cardview_group == null) {
                cardview_group = itemView.findViewById(R.id.cardview_group)
            }
            return cardview_group
        }

        fun getKindGroup(): ImageView? {
            if (kindGroup == null) {
                kindGroup = itemView.findViewById(R.id.txtKindGroup_cardview_group)
            }
            return kindGroup
        }


    }

}