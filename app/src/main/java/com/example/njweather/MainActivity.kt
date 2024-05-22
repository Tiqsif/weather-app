package com.example.njweather

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener
import kotlinx.android.synthetic.main.activity_main.btVar1
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    //weather url to get JSON
    var weather_url1 = ""
    //api id for url
    var api_id1 = "e91fed9e05004b689cad837190ce895f"
    private lateinit var textView: TextView
    private lateinit var district: TextView
    private lateinit var countrycode: TextView
    private lateinit var timezone: TextView
    private lateinit var datetime: TextView
    private lateinit var weather: TextView

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    var drawerLayout: DrawerLayout? = null
    var menu: ImageView? = null
    var home: LinearLayout? = null
    var settings:LinearLayout? = null
    var share:LinearLayout? = null
    var about:LinearLayout? = null
    var logout:LinearLayout? = null


    // declaring objects of Button class
    private var start: Button? = null
    private var stop: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //create an instance of the Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        //link the textView in which the temperature will be displayed
        textView = findViewById(R.id.textView)
        district = findViewById(R.id.district)
        countrycode = findViewById(R.id.countrycode)
        datetime = findViewById(R.id.datetime)
        timezone = findViewById(R.id.timezone)
        weather = findViewById(R.id.weather)
        // ---------------------------------------
        var toolbar: Toolbar? = null

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        start = findViewById(R.id.serviceStart);
        stop = findViewById(R.id.serviceStop);

        start?.setOnClickListener(View.OnClickListener { v ->
            if (v === start) {

                // starting the service
                startService(Intent(this@MainActivity, NewService::class.java))
            }
        })

        stop?.setOnClickListener(View.OnClickListener { v ->
            if (v === stop) {

                // stopping the service
                stopService(Intent(this@MainActivity, NewService::class.java))
            }
        })

        //-------------------------------------------------//
        drawerLayout = findViewById(R.id.drawerLayout)
        menu = findViewById<ImageView>(R.id.menu)
        about = findViewById<LinearLayout>(R.id.about1)
        home = findViewById<LinearLayout>(R.id.home1)
        logout = findViewById<LinearLayout>(R.id.logout)
        settings = findViewById<LinearLayout>(R.id.settings1)
        share = findViewById<LinearLayout>(R.id.share1)

        menu?.setOnClickListener(View.OnClickListener { SettingsActivity.openDrawer(drawerLayout) })
        home?.setOnClickListener(View.OnClickListener {
            recreate()
        })
        settings?.setOnClickListener(View.OnClickListener {
            SettingsActivity.redirectActivity(
            this@MainActivity,
            SettingsActivity::class.java
        ) })
        share?.setOnClickListener(View.OnClickListener {
            SettingsActivity.redirectActivity(
                this@MainActivity,
                ShareActivity::class.java
            )
        })
        about?.setOnClickListener(View.OnClickListener {
            SettingsActivity.redirectActivity(
                this@MainActivity,
                AboutActivity::class.java
            )
        })
        logout?.setOnClickListener(View.OnClickListener {
            Toast.makeText(
                this@MainActivity,
                "logout",
                Toast.LENGTH_SHORT
            ).show()
        })
        //-------------------------------------------------//


        //on clicking this button function to get the coordinates will be called
        btVar1.setOnClickListener {
            Log.e("lat", "buttonClicked")
            //function to find the coordinates of the last location
            obtainLocation()
        }

    }

    @SuppressLint("MissingPermission")
    private fun obtainLocation() {
        Log.e("lat", "obtainLocation")
        //get the last location
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, object : CancellationToken() {
            override fun onCanceledRequested(p0: OnTokenCanceledListener) = CancellationTokenSource().token

            override fun isCancellationRequested() = false
        })
            .addOnSuccessListener { location: Location? ->
                if (location == null)
                    Toast.makeText(this, "Cannot get location.", Toast.LENGTH_SHORT).show()
                else {
                    weather_url1 =
                        "https://api.weatherbit.io/v2.0/current?" + "lat=" + location?.latitude + "&lon=" + location?.longitude + "&key=" + api_id1
                    Log.e("lat", location?.latitude.toString())
                    Log.e("long", location?.longitude.toString())
                    //this function will fetch data from URL
                    getTemp()
                }

            }
        /*
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                //get the latitute and longitude and create the http URL
                weather_url1 =
                    "https://api.weatherbit.io/v2.0/current?" + "lat=" + location?.latitude + "&lon=" + location?.longitude + "&key=" + api_id1
                Log.e("lat", location?.latitude.toString())
                Log.e("long", location?.longitude.toString())
                Log.e("weather_url1", weather_url1.toString())
                //this function will fetch data from URL
                getTemp()
            }

         */
    }

    fun getTemp() {
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(this)
        val url: String = weather_url1
        Log.e("lat", url)
        // Request a string response from the provided URL.
        val stringReq = StringRequest(Request.Method.GET, url,
            Response.Listener<String> { response ->
                Log.e("resp", response.toString())
                //get the JSON object
                val obj = JSONObject(response)
                //get the Array from obj of name - "data"
                val arr = obj.getJSONArray("data")
                Log.e("arr", arr.toString())
                //get the JSON object from the array at index position 0
                val obj2 = arr.getJSONObject(0)
                val weatherObj = obj2.getJSONObject("weather")
                /*

                val temp = obj2.getString("temp")
                val country = obj2.getString("country_code")
                val district = obj2.getString("city_name")
                val timezone = obj2.getString("timezone")
                val datetime = obj2.getString("datetime")

                 */
                Log.e("obj2", obj2.toString())
                //set the temperature and the city name using getString() function
                textView.text = obj2.getString("temp") + "°"
                district.text = obj2.getString("city_name")
                timezone.text = obj2.getString("timezone")
                datetime.text = obj2.getString("ob_time")
                countrycode.text = obj2.getString("country_code")
                weather.text = weatherObj.getString("description")
            },
            //In case of any error
            Response.ErrorListener { textView!!.text = "Error!"})
        queue.add(stringReq)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.group) {
            Toast.makeText(this, "Create a new group", Toast.LENGTH_SHORT).show()
        }
        if (id == R.id.broadcast) {
            Toast.makeText(this, "Create a new broadcast", Toast.LENGTH_SHORT).show()
        }
        if (id == R.id.linked) {
            Toast.makeText(this, "Check linked device", Toast.LENGTH_SHORT).show()
        }
        if (id == R.id.settings) {
            Toast.makeText(this, "Go to settings", Toast.LENGTH_SHORT).show()
        }
        return true
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