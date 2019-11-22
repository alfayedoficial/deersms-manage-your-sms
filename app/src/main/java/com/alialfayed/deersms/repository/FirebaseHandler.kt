package com.alialfayed.deersms.repository

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.alialfayed.deersms.model.ContactList
import com.alialfayed.deersms.model.GroupFirebase
import com.alialfayed.deersms.model.MessageFirebase
import com.alialfayed.deersms.viewmodel.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class FirebaseHandler(activity: Activity) {

    var mAuth: FirebaseAuth
    var signInViewModel: SignInViewModel? = null
    var signUpViewModel: SignUpViewModel? = null
    var currentUser: FirebaseUser? = null
    lateinit var databaseReference: DatabaseReference
    var forgetPasswodViewModel: ForgetPasswodViewModel? = null
    var homeViewModel: HomeViewModel? = null
    lateinit var currentSIMViewModel: CurrentSIMViewModel
    lateinit var addGroupViewModel: AddGroupViewModel
    lateinit var whatsAppViewModel : WhatsAppViewModel


//    var addGroupViewModel: AddGroupViewModel? = null
//    lateinit var profileViewModel: ProfileViewModel
//    lateinit var scheduleMessageViewModel: ScheduleMessageViewModel
//    lateinit var addMessageViewModel: AddMessageViewModel
//    internal lateinit var updateList: ArrayList<MessageFirebase>


    var activity: Activity? = null

    init {
        this.activity = activity
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity, signInViewModel: SignInViewModel)
            : this(activity) {
        this.signInViewModel = signInViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity, signUpViewModel: SignUpViewModel)
            : this(activity) {
        this.signUpViewModel = signUpViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity, forgetPasswodViewModel: ForgetPasswodViewModel)
            : this(activity) {
        this.forgetPasswodViewModel = forgetPasswodViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity, homeViewModel: HomeViewModel)
            : this(activity) {
        this.homeViewModel = homeViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
    }

    constructor(activity: Activity, currentSIMViewModel: CurrentSIMViewModel) : this(activity) {
        this.currentSIMViewModel = currentSIMViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        databaseReference = FirebaseDatabase.getInstance().getReference("Messages")
    }

    constructor(activity: Activity,addGroupViewModel: AddGroupViewModel):this(activity){
        this.addGroupViewModel = addGroupViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        databaseReference = FirebaseDatabase.getInstance().getReference("Groups")
    }

        constructor(activity: Activity, whatsAppViewModel: WhatsAppViewModel)
            : this( activity ) {
        this.whatsAppViewModel = whatsAppViewModel
        mAuth = FirebaseAuth.getInstance()
        currentUser = mAuth.currentUser
        databaseReference = FirebaseDatabase.getInstance().getReference("Message")
    }


//    constructor(activity: Activity, profileViewModel: ProfileViewModel)
//            : this(activity) {
//        this.profileViewModel = profileViewModel
//        mAuth = FirebaseAuth.getInstance()
//        currentUser = mAuth.currentUser
//    }


//    constructor(activity: Activity, addMessageViewModel: AddMessageViewModel)
//            : this(activity) {
//        this.addMessageViewModel = addMessageViewModel
//        mAuth = FirebaseAuth.getInstance()
//        currentUser = mAuth.currentUser
//        databaseReference = FirebaseDatabase.getInstance().getReference("Message")
//    }

//    constructor(activity: Activity, addGroupViewModel: AddGroupViewModel)
//            : this(activity) {
//        this.addGroupViewModel = addGroupViewModel
//        mAuth = FirebaseAuth.getInstance()
//        currentUser = mAuth.currentUser
//        databaseReference = FirebaseDatabase.getInstance().getReference("Groups")
//    }

//    constructor(activity: Activity, scheduleMessageViewModel: ScheduleMessageViewModel)
//            : this( activity ) {
//        this.scheduleMessageViewModel = scheduleMessageViewModel
//        mAuth = FirebaseAuth.getInstance()
//        currentUser = mAuth.currentUser
//        databaseReference = FirebaseDatabase.getInstance().getReference("Message")
//    }




    fun getFirebaseUser(): FirebaseUser {
        return mAuth.currentUser!!
    }

    fun signUp(email: String, password: String) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            OnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("signUp", "Success " + email)
                    mAuth.currentUser!!.sendEmailVerification()
                    signUpViewModel?.SignUpSuccessful()
                    Toast.makeText(activity, "Sign Up Success , please Sign In", Toast.LENGTH_LONG)
                        .show()
                } else {
                    Log.i("signUp", "Failed")
                    signUpViewModel?.SignUpfailed()
                }
            })
    }

    fun signIn(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(OnCompleteListener {
            if (it.isSuccessful) {
                if (mAuth.currentUser!!.isEmailVerified) {
                    Log.i("signIn", "Success" + email)
                    Toast.makeText(activity, " Success " + email, Toast.LENGTH_LONG).show()
                    signInViewModel?.SignInSuccessful()
                } else {
                    signInViewModel?.setMsgAlert("Please, verify your email address to login.")
                }

            } else {
                Log.i("signIn", "Fail")
                Toast.makeText(activity, " Failed " + email, Toast.LENGTH_LONG).show()
                signInViewModel?.SignInfailed()
            }
        })
    }

    fun resetPassword(email: String) {
        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(OnCompleteListener {
                if (it.isSuccessful) {
                    Log.i("forgetPassword", "Success" + email)
                    Toast.makeText(activity, "Please Check your Email", Toast.LENGTH_LONG).show()
                    forgetPasswodViewModel?.ForgetPasswordSuccessful()

                } else {
                    Log.i("forgetPassword", "Fail")
                    forgetPasswodViewModel?.ForgetPasswordfailed()
                }
            })
    }

    fun logout() {
        mAuth.signOut()
    }

    fun changePassword(oldPassword: String, password: String) {
        val user = getFirebaseUser()
        val credential = EmailAuthProvider
            .getCredential(user.email!!, oldPassword)
        user.reauthenticate(credential)
            .addOnCompleteListener(OnCompleteListener<Void> { task ->
                if (task.isSuccessful) {
                    user.updatePassword(password)
                        .addOnCompleteListener(OnCompleteListener<Void> { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(activity, " Password update ", Toast.LENGTH_LONG)
                                    .show()
                            } else {
                                Toast.makeText(
                                    activity,
                                    " Sorry your have Error ",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                } else {
                }
            })
    }


    fun scheduleMessageRepository(
        personName: String, personNumber: String, SMSMessage: String,
        date: String, time: String, status: String, type: String,
        calendar: Long, smsDelivered: String
    ) {

        /**
         * create  Message Firebase
         */
        databaseReference = FirebaseDatabase.getInstance().getReference("Messages")
        val smsId: String = databaseReference.push().key.toString()
        val user: String = FirebaseAuth.getInstance().currentUser!!.uid
        val message = MessageFirebase(
            smsId, personName, personNumber, SMSMessage, date, time, status, type,
            user, calendar, smsDelivered
        )
        /**
         * insert Message Firebase
         */
        databaseReference.child(smsId).setValue(message)

        /**
         * set Alarm Message
         */
//        addMessageViewModel.setSMSAlarm(
//            message.getSmsId(), message.getSmsReceiverName(),
//            message.getSmsReceiverNumber(), message.getSmsMsg(), message.getSmsDate(),
//            message.getSmsTime(), message.getSmsStatus(), message.getSmsType(), message.getUserID(),
//            message.getSmsCalender(), message.getSmsDelivered()
//        )
    }

    fun scheduleMessageRepository(
        personName: String, personNumber: String, SMSMessage: String,
        date: String, time: String, status: String, type: String,
        smsDelivered: String, calendar: Long
    ) {

        /**
         * create  Message Firebase
         */
        val smsId: String = databaseReference.push().key.toString()
        val user: String = FirebaseAuth.getInstance().currentUser!!.uid
        val message = MessageFirebase(
            smsId, personName, personNumber, SMSMessage, date, time, status, type,
            user, calendar, smsDelivered
        )
        /**
         * insert Message Firebase
         */
        databaseReference.child(smsId).setValue(message)

        /**
         * set Alarm Message
         */
        currentSIMViewModel.setSMSAlarm(
            message.getSmsId(),
            message.getSmsPersonName(),
            message.getSmsPersonNumber(),
            message.getSmsMessage(),
            message.getSmsDate(),
            message.getSmsTime(),
            message.getSmsStatus(),
            message.getsmsSendVia(),
            message.getUserID(),
            message.getSmsCalender(),
            message.getSmsDelivered()
        )
    }

    fun scheduleWhatsAppMessageRepository(
        personName: String, personNumber: String, whatsAppMessage: String,
        whatsAppDate: String, whatsAppTime: String, whatsAppStatus: String, whatsAppSendVia: String,
        whatsAppDelivered: String, calendar: Long
    ) {

        /**
         * create  Message Firebase
         */
        databaseReference = FirebaseDatabase.getInstance().getReference("Messages")
        val whatsAppId: String = databaseReference.push().key.toString()
        val user = FirebaseAuth.getInstance().currentUser!!.uid
        val message = MessageFirebase(
            whatsAppId,
            personName,
            personNumber,
            whatsAppMessage,
            whatsAppDate,
            whatsAppTime,
            whatsAppStatus,
            whatsAppSendVia,
            user,
            calendar,
            whatsAppDelivered
        )
        /**
         * insert Message Firebase
         */
        databaseReference.child(whatsAppId).setValue(message)
        /**
         * set Alarm Message
         */
        whatsAppViewModel.setWhatsAppAlarm(
            message.getSmsId(),
            message.getSmsPersonName(),
            message.getSmsPersonNumber(),
            message.getSmsMessage(),
            message.getSmsDate(),
            message.getSmsTime(),
            message.getSmsStatus(),
            message.getsmsSendVia(),
            message.getUserID(),
            message.getSmsCalender(),
            message.getSmsDelivered()
        )

    }


    fun insetGroup(list: ArrayList<ContactList>, groupName: String) {
        /**
         * create  Group Firebase
         * 1- create id group
         * 2- create group
         * 3- inset data
         */
        databaseReference = FirebaseDatabase.getInstance().getReference("Groups")
        val userId: String = FirebaseAuth.getInstance().currentUser!!.uid
        val groupId = databaseReference.push().key.toString()
        val group: GroupFirebase = GroupFirebase(userId, groupId, groupName, list)
        databaseReference.child(groupId).setValue(group)


    }


}







