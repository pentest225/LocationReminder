package com.udacity.project4.locationreminders

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.BuildConfig
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityRemindersBinding
import com.udacity.project4.utils.showToast

/**
 * The RemindersActivity that holds the reminders fragments
 */
private const val TAG = "RemindersActivity"
class RemindersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRemindersBinding
    //Request Location Permission
    private val runningQOrLater = Build.VERSION.SDK_INT >=
            Build.VERSION_CODES.Q

    private val requestPermissionResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
        if(it.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION,false) &&
            it.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION,false)
            ){
            //WHE HAVE LOCATION PERMISSION
            Log.d(TAG, "Location Granted: WHE HAVE LOCATION PERMISSION")
            showToast("WHE HAVE LOCATION PERMISSION")
        }else{
            Snackbar.make(
                binding.root,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_SHORT
            ).setAction(R.string.settings) {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()
        }
    }

    @TargetApi(26)
    private val requestBackgroundLocationPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){isGranted ->
        if(!isGranted){
            Snackbar.make(
                binding.root,
                R.string.permission_denied_explanation,
                Snackbar.LENGTH_SHORT
            )
                .setAction(R.string.settings) {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    })
                }.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemindersBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onStart() {
        if(!hasLocationPermission()){
            val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
            requestPermissionResult.launch(permissions)
        }
        @RequiresApi(Build.VERSION_CODES.Q)
        if(runningQOrLater && !hasLocationPermission()){
            requestBackgroundLocationPermission.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
        super.onStart()
    }

    private fun requestLocationPermissionIsNotGranted(){

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                (binding.navHostFragment as NavHostFragment).navController.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun hasLocationPermission():Boolean {
            val foregroundLocationApproved = (
                    PackageManager.PERMISSION_GRANTED ==
                            ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION))
            val backgroundPermissionApproved =
                if (runningQOrLater) {
                    PackageManager.PERMISSION_GRANTED ==
                            ActivityCompat.checkSelfPermission(
                                this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            )
                } else {
                    true
                }
            return foregroundLocationApproved && backgroundPermissionApproved

    }

}
