package com.example.foodordering.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodordering.R
import com.example.foodordering.util.ConnectionManager
import org.json.JSONObject


class LogInActivity : AppCompatActivity() {
    lateinit var txtMobileNumber : EditText
    lateinit var txtPassword : EditText
    lateinit var btnLogIn : Button
    lateinit var txtForgotPassword : TextView
    lateinit var txtSignUp : TextView
    lateinit var database : Database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        database = Database(this.applicationContext)


        txtMobileNumber = findViewById(R.id.txtMobileNumber)
        txtPassword = findViewById(R.id.txtPassword)
        btnLogIn = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtSignUp = findViewById(R.id.txtSignUp)



        btnLogIn.setOnClickListener {v->

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken,0)

            if (txtMobileNumber.text.toString().equals("")||
                txtPassword.text.toString().equals("")){
                Toast.makeText(this@LogInActivity,"Fill Credential !!!",Toast.LENGTH_SHORT).show()
            }
            else{

                val queue = Volley.newRequestQueue(this@LogInActivity)
                val url = "http://13.235.250.119/v2/login/fetch_result/"

                val jsonParams = JSONObject()
                jsonParams.put("mobile_number",txtMobileNumber.text.toString())
                jsonParams.put("password",txtPassword.text.toString())

                if(ConnectionManager().checkConnection(this@LogInActivity)){
                    val jsonRequest = object :
                        JsonObjectRequest(Method.POST,url,jsonParams, Response.Listener {


                            try {
                                val dataJsonObject = it.getJSONObject("data")
                                val success = dataJsonObject.getBoolean("success")
                                if (success) {
                                    Toast.makeText(
                                        this@LogInActivity,
                                        "Logged In Successfully",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@LogInActivity,
                                        "Invalid Credential",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }catch (e:Exception){
                                Toast.makeText(this@LogInActivity,"Some error Occurred",Toast.LENGTH_SHORT).show()
                            }


                    }, Response.ErrorListener {

                        Toast.makeText(this@LogInActivity,"Volley error $it",Toast.LENGTH_SHORT).show()
                    }){
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String,String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "a087f3bb8710df"
                            return headers
                        }
                    }

                    queue.add(jsonRequest)
                }else{

                    val dialog = AlertDialog.Builder(this@LogInActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection Not Found")
                    dialog.setPositiveButton("Open Settings"){ text, listener->
                        val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit"){text,listener->
                        ActivityCompat.finishAffinity(this@LogInActivity)
                    }
                    dialog.create()
                    dialog.show()

                }
            }




        }

        txtSignUp.setOnClickListener {
            val intent = Intent(this,ActivitySignup::class.java)
            startActivity(intent)
        }

        txtForgotPassword.setOnClickListener {
            val intent = Intent(this,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }


}