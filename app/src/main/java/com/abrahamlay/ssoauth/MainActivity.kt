package com.abrahamlay.ssoauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addFragment(MainFragment(), R.id.frame, MainFragment.TAG)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.fragments.forEach { fragment ->
            fragment.onActivityResult(
                resultCode,
                resultCode,
                data
            )
        }
        Log.d(TAG, "onActivityResult:$requestCode:$resultCode:${data?.data}")
    }

    companion object {
        const val TAG = "MainActivity"
    }

    private fun addFragment(fragment: Fragment, placeHolder: Int, tag: String = "") {
        val manager = supportFragmentManager
        val transaction = manager.beginTransaction()
        transaction.replace(placeHolder, fragment, tag)
            .commitAllowingStateLoss()
    }
}