package com.alialfayed.deersms.view.fragment


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.alialfayed.deersms.R
import com.alialfayed.deersms.R.*
import com.alialfayed.deersms.model.MessageFirebase
import com.alialfayed.deersms.view.adabter.PendingAdabter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

/**
 * A simple [Fragment] subclass.
 */
class PendingFragment : Fragment() {

    lateinit var adabterPending: PendingAdabter
    lateinit var pendingRcyclerView: RecyclerView
    lateinit var mdatabaseReference : DatabaseReference
    internal lateinit var pendingList: ArrayList<MessageFirebase>
    internal lateinit var view: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         view = inflater.inflate(layout.fragment_pending, container, false)
        pendingRcyclerView  = view.findViewById(R.id.recyclerView_Pending)
        mdatabaseReference = FirebaseDatabase.getInstance().getReference("Messages")
        mdatabaseReference.keepSynced(true)
        pendingList = ArrayList()
        return view
    }

    override fun onStart() {
        super.onStart()
        mdatabaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                pendingList.clear()
                for (pendingSnapShot in p0.children){
                    val pendingMessage = pendingSnapShot.getValue(MessageFirebase::class.java)
                    if (pendingMessage!!.getUserID() == FirebaseAuth.getInstance().currentUser!!.uid){
                       if (pendingMessage.getSmsStatus() == "Pending") {
                           pendingList.add(pendingMessage)
                       }
                    }
                    setPendingListAdabter()
                }
                if (pendingList.size <=0){
                    view.findViewById<LinearLayout>(R.id.layoutNoHaveItem_PendingFragent).visibility = View.VISIBLE
                }else{
                    view.findViewById<LinearLayout>(R.id.layoutNoHaveItem_PendingFragent).visibility = View.GONE
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                //TODO Result if Have Error on database
            }
        })
    }
    private fun setPendingListAdabter(){
        val linerMangager = LinearLayoutManager(activity)
        pendingRcyclerView.layoutManager = linerMangager
        adabterPending = PendingAdabter(this.activity!!)
        pendingRcyclerView.adapter =adabterPending
        adabterPending.setDataToAdapter(pendingList)

    }






}
