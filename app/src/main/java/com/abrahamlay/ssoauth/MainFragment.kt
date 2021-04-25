package com.abrahamlay.ssoauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.abrahamlay.ssoauth.sso.AuthFacebookMethod
import com.abrahamlay.ssoauth.sso.AuthGoogleMethod
import com.abrahamlay.ssoauth.sso.AuthMethod
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount


/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    private var btnGoogle: Button? = null
    private var btnFacebook: Button? = null
    private var tvUsername: TextView? = null
    private val signInResultCallback: AuthMethod.SignInResultCallback =
        object : AuthMethod.SignInResultCallback {
            override fun onSuccess(ssoAccount: Any?) {
                Toast.makeText(requireContext(), "Login Success $ssoAccount", Toast.LENGTH_LONG)
                    .show()
                when (ssoAccount) {
                    is GoogleSignInAccount -> {
                        tvUsername?.text = ssoAccount.displayName
                        Toast.makeText(requireContext(), ssoAccount.idToken, Toast.LENGTH_SHORT)
                            .show()
                    }
                    is LoginResult? -> {
                        tvUsername?.text = ssoAccount?.accessToken?.token
                        Toast.makeText(
                            requireContext(),
                            ssoAccount?.accessToken?.token,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), "empty Token", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onCancel() {
                Toast.makeText(requireContext(), "Login Cancel", Toast.LENGTH_LONG).show()
            }

            override fun onFailed(e: java.lang.Exception?) {
                Toast.makeText(requireContext(), "Login Failed ${e?.message}", Toast.LENGTH_LONG)
                    .show()
            }

        }

    private fun initLoginSso() {
        val googleClientId =
//            "1087995859001-kg21hcdbcp24vt5m4oimjn1nmrbivirj.apps.googleusercontent.com"
            "1087995859001-atm9tdaajskd5jbtj0ak7922dinnl9cj.apps.googleusercontent.com"
        AuthFacebookMethod.setupSSO(requireActivity(), "", signInResultCallback)
        AuthGoogleMethod.setupSSO(requireActivity(), googleClientId, signInResultCallback)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        btnGoogle = view.findViewById<Button>(R.id.button_login_google)
        btnFacebook = view.findViewById<Button>(R.id.button_login_facebook)
        tvUsername = view.findViewById<TextView>(R.id.tv_username)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initLoginSso()
        btnGoogle?.setOnClickListener {
            AuthGoogleMethod.signIn(this, arrayOf("email", "profile"))
        }

        btnFacebook?.setOnClickListener {
            AuthFacebookMethod.signIn(this, arrayOf("email"))
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            AuthGoogleMethod.RC_SIGN_IN -> {
                AuthGoogleMethod.onActivityResult(requestCode, resultCode, data)
            }
            else -> {
                AuthFacebookMethod.onActivityResult(requestCode, resultCode, data)
            }
        }
        Log.d(TAG, "onActivityResult:$requestCode:$resultCode:${data?.data}")
    }

    companion object {
        const val TAG = "MainFragment"
    }
}