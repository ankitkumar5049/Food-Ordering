package com.example.foodordering.activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodordering.R
import com.example.foodordering.util.ConnectionManager
import org.json.JSONObject
import java.lang.Exception


class ActivitySignup : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    lateinit var txtName : EditText
    lateinit var txtEmail : EditText
    lateinit var txtAddress : EditText
    lateinit var txtPassword: EditText
    lateinit var txtConfirmPassword : EditText
    lateinit var txtMobileNumber: EditText
    lateinit var btnRegister : Button

    lateinit var database : Database


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)



        toolbar = findViewById(R.id.toolbar)
        txtName = findViewById(R.id.txtName)
        txtEmail = findViewById(R.id.txtEmail)
        txtAddress = findViewById(R.id.txtAddress)
        txtPassword = findViewById(R.id.txtPassword)
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword)
        txtMobileNumber = findViewById(R.id.txtMobileNumber)
        btnRegister = findViewById(R.id.btnRegister)


        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }




        btnRegister.setOnClickListener { v ->

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken, 0)

            if (txtName.text.toString().equals("") ||
                txtEmail.text.toString().equals("") ||
                txtMobileNumber.text.toString().equals("") ||
                txtAddress.text.toString().equals("") ||
                txtPassword.text.toString().equals("") ||
                txtConfirmPassword.text.toString().equals("")
            ) {
                Toast.makeText(this@ActivitySignup, "Fill complete details |||", Toast.LENGTH_SHORT)
                    .show()
            }




            if(txtPassword.text.toString().length<4){
                Toast.makeText(this@ActivitySignup, "Password must be length of 4 or greater", Toast.LENGTH_SHORT)
                    .show()
            }

            else if (txtPassword.text.toString() !=
                txtConfirmPassword.text.toString()
            ) {
                Toast.makeText(
                    this@ActivitySignup,
                    "Your Password Does not match",
                    Toast.LENGTH_SHORT
                ).show()
            }else{


                val queue = Volley.newRequestQueue(this@ActivitySignup)
                val url = "http://13.235.250.119/v2/register/fetch_result"

                val jsonParams = JSONObject()
                jsonParams.put("name",txtName.text.toString())
                jsonParams.put("mobile_number",txtMobileNumber.text.toString())
                jsonParams.put("email",txtEmail.text.toString())
                jsonParams.put("address",txtAddress.text.toString())
                jsonParams.put("password",txtPassword.text.toString())

                if(ConnectionManager().checkConnection(this@ActivitySignup)){
                    val jsonRequest = object :JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {

                        try {

                            val dataJsonObject = it.getJSONObject("data")
                            val success = dataJsonObject.getBoolean("success")
                            if (success) {
                                Toast.makeText(
                                    this@ActivitySignup,
                                    "Registered Successfully",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                val intent = Intent(this, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@ActivitySignup,
                                    "Already Registered",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }catch (e:Exception){
                            Toast.makeText(this@ActivitySignup,"Volley error $it",Toast.LENGTH_SHORT).show()
                        }


                    },Response.ErrorListener {

                        Toast.makeText(this@ActivitySignup,"Volley error $it",Toast.LENGTH_SHORT).show()
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

                    val dialog = AlertDialog.Builder(this@ActivitySignup)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection Not Found")
                    dialog.setPositiveButton("Open Settings"){ text, listener->
                        val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit"){text,listener->
                        ActivityCompat.finishAffinity(this@ActivitySignup)
                    }
                    dialog.create()
                    dialog.show()

                }


            }


        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }




}
