package com.example.njweather

import android.R.id
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.drawerlayout.widget.DrawerLayout.SimpleDrawerListener
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_about.drawerLayout
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import android.app.Activity
import android.content.Intent
import androidx.core.view.GravityCompat

class SettingsActivity : AppCompatActivity() {
    var drawerLayout: DrawerLayout? = null
    var menu: ImageView? = null
    var home: LinearLayout? = null
    var settings: LinearLayout? = null
    var share: LinearLayout? = null
    var about: LinearLayout? = null
    var logout: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        drawerLayout = findViewById(R.id.drawerLayout)
        menu = findViewById(R.id.menu)
        about = findViewById(R.id.about1)
        home = findViewById(R.id.home1)
        logout = findViewById(R.id.logout)
        settings = findViewById(R.id.settings1)
        share = findViewById(R.id.share1)


        menu?.setOnClickListener(View.OnClickListener { openDrawer(drawerLayout) })
        home?.setOnClickListener(View.OnClickListener {
            redirectActivity(
                this@SettingsActivity,
                MainActivity::class.java
            )
        })
        settings?.setOnClickListener(View.OnClickListener { recreate() })
        share?.setOnClickListener(View.OnClickListener {
            redirectActivity(
                this@SettingsActivity,
                ShareActivity::class.java
            )
        })
        about?.setOnClickListener(View.OnClickListener {
            redirectActivity(
                this@SettingsActivity,
                AboutActivity::class.java
            )
        })
        logout?.setOnClickListener(View.OnClickListener {
            Toast.makeText(
                this@SettingsActivity,
                "logout",
                Toast.LENGTH_SHORT
            ).show()
        })
    }

    override fun onPause() {
        super.onPause()
        closeDrawer(drawerLayout)
    }

    companion object {
        fun openDrawer(drawerLayout: DrawerLayout?) {
            drawerLayout!!.openDrawer(GravityCompat.START)
        }

        fun closeDrawer(drawerLayout: DrawerLayout?) {
            if (drawerLayout!!.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        fun redirectActivity(activity: Activity, secondActivity: Class<*>?) {
            val intent = Intent(activity, secondActivity)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            activity.startActivity(intent)
            activity.finish()
        }
    }
}