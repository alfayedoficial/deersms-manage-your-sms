package com.alialfayed.deersms.viewmodel

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.alialfayed.deersms.repository.FirebaseHandler
import com.alialfayed.deersms.view.activity.ForgetPasswordActivity
import com.alialfayed.deersms.view.activity.SignInActivity

/**
 * Class do :
 * Created by ( Eng Ali)
 */
class ForgetPasswodViewModel : ViewModel(){
    lateinit var activity: Activity
    lateinit var forgetPasswordActivity: ForgetPasswordActivity
    var firebaseHandler: FirebaseHandler? = null

    fun setForgetPasswordInActivity(activity: Activity) {
        this.activity = activity
        this.forgetPasswordActivity = activity as ForgetPasswordActivity
        firebaseHandler = FirebaseHandler(activity , this)
    }

    fun forgetPassword(email : String){
        firebaseHandler?.resetPassword(email)
    }

    /**
     * method go to Sign In Activity After check method on viewmodel
     */
    fun ForgetPasswordSuccessful() {
        val start = Intent(forgetPasswordActivity, SignInActivity::class.java)
        forgetPasswordActivity.startActivity(start)
        forgetPasswordActivity.finish()

    }

    /**
     * method Error
     */
    fun ForgetPasswordfailed() {
                Toast.makeText(activity, " Failed Forget Password", Toast.LENGTH_LONG).show()
    }


}