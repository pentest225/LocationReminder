package com.udacity.project4.authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.udacity.project4.R
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.utils.showToast
import org.koin.core.context.startKoin

/**
 * This class should be the starting point of the app, It asks the users to sign in / register, and redirects the
 * signed in users to the RemindersActivity.
 */
private const val TAG = "AuthenticationActivity"
class AuthenticationActivity : AppCompatActivity() {

    private val providers = arrayListOf(
        AuthUI.IdpConfig.EmailBuilder().build(),
        AuthUI.IdpConfig.GoogleBuilder().build(),
    )
    private val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)
        autoAuth()

        findViewById<Button>(R.id.btn_login).setOnClickListener {
            signInLauncher.launch(signInIntent)
        }
        // TODO: Implement the create account and sign in using FirebaseUI,
        //  use sign in using email and sign in using Google

        // TODO: If the user was authenticated, send him to RemindersActivity

        // TODO: a bonus is to customize the sign in flow to look nice using :
        //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#custom-layout
    }

    private fun autoAuth(){
        if(FirebaseAuth.getInstance().currentUser != null){
            startRemindersActivity()
        }
    }

    private fun startRemindersActivity(){
        Log.i(TAG, "onSignInResult: Coll")
        //Start Location Reminders Activity
        val intent = Intent(this@AuthenticationActivity,RemindersActivity::class.java)
        startActivity(intent)
    }
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult?){
        val response = result?.idpResponse
        if (result?.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            // ...
            user?.let {
                startRemindersActivity()
            }

        } else {
            if(response == null){
               showToast(getString(R.string.auth_error))
            }else{
                showToast(response.error?.message ?: getString(R.string.auth_error))
            }
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
            Log.e(TAG, "onSignInResult: $response",)
        }
    }

}