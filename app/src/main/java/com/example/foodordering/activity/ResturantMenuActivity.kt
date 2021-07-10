package com.example.foodordering.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodordering.R
import com.example.foodordering.adapter.MenuRecyclerAdapter
import com.example.foodordering.model.MenuItem
import com.example.foodordering.util.ConnectionManager

class ResturantMenuActivity : AppCompatActivity() {

    private lateinit var recyclerDashboard : RecyclerView
    private lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var progressLayout : RelativeLayout
    private lateinit var progressBar : ProgressBar

    val menuList = arrayListOf<MenuItem>()

    lateinit var toolbar : Toolbar



    lateinit var recyclerAdapter: MenuRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resturant_menu)

        recyclerDashboard = findViewById(R.id.recycleDashboard) as RecyclerView


        layoutManager = LinearLayoutManager(this@ResturantMenuActivity)

        progressLayout = findViewById(R.id.progressLayout)

        toolbar = findViewById(R.id.toolbar)

        progressBar = findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE

        recyclerAdapter = MenuRecyclerAdapter(this@ResturantMenuActivity,menuList)

        recyclerDashboard.adapter = recyclerAdapter

        recyclerDashboard.layoutManager = layoutManager

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val id:String = intent.getStringExtra("id").toString()
        val restName: String? = intent.getStringExtra("restName")

        if (restName != null) {
            setUpToolbar(restName)
        }

        val queue = Volley.newRequestQueue(this@ResturantMenuActivity)

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$id"

        if (ConnectionManager().checkConnection(this@ResturantMenuActivity)){

            try {

                val jsonObjectRequest = object : JsonObjectRequest(
                    Method.GET, url, null, Response.Listener {

                        // Here we will handle the response

                        progressLayout.visibility = View.GONE
                        val dataJsonObject = it.getJSONObject("data")
                        val success = dataJsonObject.getBoolean("success")
                        if (success) {
                            val dataJsonArray = dataJsonObject.getJSONArray("data")
                            for (i in 0 until dataJsonArray.length()) {
                                val foodJsonObject = dataJsonArray.getJSONObject(i)
                                val foodObject = MenuItem(
                                    foodJsonObject.getString("name"),
                                    foodJsonObject.getString("cost_for_one"),
                                )
                                menuList.add(foodObject)
                            }
                            recyclerAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(
                                this@ResturantMenuActivity,
                                "Some Error Occurred!!",
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    },
                    Response.ErrorListener {

                        //Here we will handle the error
                        //println("error is $it")
                        if (this@ResturantMenuActivity != null) {
                            Toast.makeText(
                                this@ResturantMenuActivity,
                                "Volley error occurred !",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "a087f3bb8710df"
                        return headers
                    }
                }

                queue.add(jsonObjectRequest)
            }catch (e:Exception){
                e.printStackTrace()
            }

        }
        else{

            val dialog = AlertDialog.Builder(this@ResturantMenuActivity)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings"){ text, listener->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                this@ResturantMenuActivity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(this@ResturantMenuActivity)
            }
            dialog.create()
            dialog.show()

        }

    }

    private fun setUpToolbar(title:String){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "$title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}