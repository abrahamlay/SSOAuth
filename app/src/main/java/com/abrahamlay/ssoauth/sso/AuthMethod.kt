package com.abrahamlay.ssoauth.sso

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import java.lang.Exception

abstract class AuthMethod {
    abstract fun setupSSO(activity: Activity, googleClientId: String = "", signInResultCallback: SignInResultCallback?)
    abstract fun signIn(fragment: Fragment, scope: Array<String>)
    abstract fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    protected var signInResultCallback: SignInResultCallback? = null

    interface SignInResultCallback {
        fun onSuccess(ssoAccount: Any?)
        fun onCancel()
        fun onFailed(e: Exception?)
    }

}