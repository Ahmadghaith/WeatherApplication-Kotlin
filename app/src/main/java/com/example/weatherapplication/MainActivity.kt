package com.example.weatherapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.weatherapplication.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.jar.Manifest

class MainActivity : AppCompatActivity()/*,EasyPermissions.PermissionCallbacks, EasyPermissions.RationaleCallbacks*/ {

    lateinit var binding: ActivityMainBinding
    val REQUESTCODEINTERNET = 123
    val REQUESTCODELOCATION = 124
    val internet: String = android.Manifest.permission.INTERNET
    val location: String = android.Manifest.permission.ACCESS_FINE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Location()

        createChannel()

        //Navigation
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


    //Location request on runtime
    private fun Location(){
        if(!EasyPermissions.hasPermissions(this, location))
        {
            EasyPermissions.requestPermissions(this, "Permission for Internet", REQUESTCODELOCATION, location)
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