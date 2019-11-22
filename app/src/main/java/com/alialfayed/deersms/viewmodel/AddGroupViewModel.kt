package com.alialfayed.deersms.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alialfayed.deersms.ContactsRepository.ContactsRepository
import com.alialfayed.deersms.model.ContactList
import com.alialfayed.deersms.repository.FirebaseHandler
import com.alialfayed.deersms.view.activity.AddGroupActivity

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class AddGroupViewModel(addGroupActivityRef: AddGroupActivity) : ViewModel() {


    var addGroupActivityRef: AddGroupActivity? = addGroupActivityRef
    var contactsRepository: ContactsRepository? = null
//    var groupsRepository: GroupsRepository? = null
    var firebaseHandler:FirebaseHandler
    private lateinit var mutableLiveData: MutableLiveData<List<ContactList>>


    init {
//        groupsRepository = GroupsRepository(this)
        contactsRepository = ContactsRepository(this)
        mutableLiveData = MutableLiveData()
        firebaseHandler = FirebaseHandler(addGroupActivityRef,this)
    }

    fun uploadGroup(list : ArrayList<ContactList>, groupName: String) {
//        groupsRepository!!.insetGroup(list ,groupName )
        firebaseHandler.insetGroup(list ,groupName)
    }


    fun getContacts(): MutableLiveData<ArrayList<ContactList>> {
        mutableLiveData.postValue(contactsRepository!!.getContactsList(addGroupActivityRef!!))
        return mutableLiveData as MutableLiveData<ArrayList<ContactList>>
    }


}