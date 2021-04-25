package com.abrahamlay.ssoauth.sso

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult

object AuthFacebookMethod : AuthMethod() {

    var mCallbackManager: CallbackManager? = null
    override fun setupSSO(activity: Activity, googleClientId:String, signInResultCallback: AuthMethod.SignInResultCallback?) {
        this.signInResultCallback = signInResultCallback
        mCallbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(mCallbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
                if (result != null) {
                    signInResultCallback?.onSuccess(result)
                    Log.w(TAG, "signInResult: SUCCESS " + result?.accessToken?.token)
                } else {
                    signInResultCallback?.onFailed(NullPointerException())
                }
            }

            override fun onCancel() {
                signInResultCallback?.onCancel()
                Log.w(TAG, "signInResult: CANCELED ")
            }

            override fun onError(error: FacebookException?) {
                signInResultCallback?.onFailed(error)
                Log.e(TAG, "signInResult: FAILED error=${error?.message}")
            }

        })
    }

    override fun signIn(fragment: Fragment, scope: Array<String>) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, scope.asList())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult:$requestCode:$resultCode:${data?.data}")
        mCallbackManager?.onActivityResult(requestCode, resultCode, data)
    }


    const val TAG = "FACEBOOK_SIGN_IN"

}