package com.example.weatherapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity()/*,EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks*/ {

    private val REQUEST_CODE_LOCATION = 124
    private val location: String = android.Manifest.permission.ACCESS_FINE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Asking location permission (Sadly it doesn't make sense because the app won't take the user's location [I wish I could do it only if I had more time])
        location()

        //Creating channel for Firebase notifications
        createChannel()

        //Navigation (Bottom nav bar)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bott_nav)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)

    }

    private fun createChannel()
    {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O) {
            val name = "Ahmad Ghaith"
            val descriptionText = "Weather notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NotificationFirebase.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }


    //Location request on runtime (Made it using YouTube)
    private fun location(){
        if(!EasyPermissions.hasPermissions(this, location))
        {
            EasyPermissions.requestPermissions(this, "Permission for Internet", REQUEST_CODE_LOCATION, location)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }




}