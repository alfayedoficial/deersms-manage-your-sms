package com.alialfayed.deersms.model

/**
 * Class do :
 * Created  ( Eng Ali)
 */
class MessageFirebase {

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


    constructor()

    constructor(smsId: String, smsPersonName: String, smsPersonNumber: String, smsMessage: String,
                smsDate: String, smsTime: String, smsStatus: String, smsSendVia: String, userID: String,
                smsCalender: Long, smsDelivered:String) {
        this.smsId = smsId
        this.smsPersonName = smsPersonName
        this.smsPersonNumber = smsPersonNumber
        this.smsMessage = smsMessage
        this.smsDate = smsDate
        this.smsTime = smsTime
        this.smsStatus = smsStatus
        this.smsSendVia = smsSendVia
        this.userID = userID
        this.smsCalender = smsCalender
        this.smsDelivered = smsDelivered

    }

    fun getSmsId(): String{ return smsId }

    fun getSmsPersonName(): String{ return smsPersonName }

    fun getSmsPersonNumber(): String { return smsPersonNumber }

    fun getSmsMessage(): String{ return smsMessage }

    fun getSmsDate(): String{ return smsDate }

    fun getSmsTime(): String{ return smsTime }

    fun getSmsStatus(): String{ return smsStatus }

    fun getsmsSendVia(): String{ return smsSendVia }

    fun getUserID(): String{ return userID }

    fun getSmsCalender(): Long{ return smsCalender }

    fun getSmsDelivered(): String{ return smsDelivered}
}