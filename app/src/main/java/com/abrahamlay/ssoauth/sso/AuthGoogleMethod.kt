package com.abrahamlay.ssoauth.sso

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task


object AuthGoogleMethod : AuthMethod() {

    private var scope: Array<String>? = null
    var googleClientId: String = ""
    override fun setupSSO(
        activity: Activity,
        googleClientId: String,
        signInResultCallback: SignInResultCallback?
    ) {
        this.signInResultCallback = signInResultCallback
        this.googleClientId = googleClientId
        Log.w(TAG, "setupGoogleSSO: " + this.googleClientId)
    }

    override fun signIn(fragment: Fragment, scope: Array<String>) {
        AuthGoogleMethod.scope = scope
        signIn(fragment, getClient(fragment.requireActivity(), this.googleClientId))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult:$requestCode:$resultCode:${data?.data}")
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult:$requestCode:$resultCode:${data?.data}")
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult: RESULT_OK")
                handleSignInResult(data)
            } else {
                Log.d(TAG, "onActivityResult: not RESULT_OK")
                signInResultCallback?.onFailed(NullPointerException())
            }
        }
    }

    private fun getClient(activity: Activity, googleClientId: String): GoogleSignInClient {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(googleClientId)
            .requestProfile()
            .requestEmail()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        return GoogleSignIn.getClient(activity, gso)
    }

    fun getCurrentGoogleUserAccount(context: Context): GoogleSignInAccount? {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        return GoogleSignIn.getLastSignedInAccount(context)
    }

    fun isUserIsLogin(context: Context): Boolean = (getCurrentGoogleUserAccount(context) != null)

    private fun signIn(fragment: Fragment, googleSignInClient: GoogleSignInClient?) {
        val signInIntent: Intent? = googleSignInClient?.signInIntent
        fragment.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun getSignInResult(intent: Intent): Task<GoogleSignInAccount>? {
        return GoogleSignIn.getSignedInAccountFromIntent(intent)
    }

    private fun handleSignInResult(intent: Intent?) {
        try {
            if (intent != null) {
                val result = getSignInResult(intent)?.getResult(ApiException::class.java)
                if (result != null) {
                    signInResultCallback?.onSuccess(result)
                    Log.w(TAG, "signInResult: SUCCESS " + result.idToken)
                } else {
                    Log.w(TAG, "signInResult: FAILED result is null")
                    signInResultCallback?.onFailed(NullPointerException())
                }
            } else {
                Log.w(TAG, "signInResult: FAILED intent is null")
                signInResultCallback?.onFailed(NullPointerException())
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult: FAILED code=" + e.statusCode)
            signInResultCallback?.onFailed(e)
        }
    }


    const val RC_SIGN_IN = 424
    const val TAG = "GOOGLE_SIGN_IN"

}