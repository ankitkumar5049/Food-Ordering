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
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodordering.R
import com.example.foodordering.util.ConnectionManager
import org.json.JSONObject
import java.lang.Exception

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var txtForgotMobile : EditText
    lateinit var txtForgotEmail : EditText
    lateinit var btnNext : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        txtForgotEmail = findViewById(R.id.txtForgotEmail)
        txtForgotMobile = findViewById(R.id.txtForgotMobile)
        btnNext = findViewById(R.id.btnNext)

        btnNext.setOnClickListener{v->

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v.windowToken,0)

            if(txtForgotEmail.text.toString().equals("")||
                    txtForgotMobile.text.toString().equals("")){
                Toast.makeText(this@ForgotPasswordActivity,"Fill both details !!!", Toast.LENGTH_SHORT).show()
            }else{

                val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)
                val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

                val jsonParams = JSONObject()
                jsonParams.put("mobile_number",txtForgotMobile.text.toString())
                jsonParams.put("email",txtForgotEmail.text.toString())

                if(ConnectionManager().checkConnection(this@ForgotPasswordActivity)){
                    val jsonRequest = object :
                        JsonObjectRequest(Method.POST,url,jsonParams, Response.Listener {

                            try {

                                val dataJsonObject = it.getJSONObject("data")
                                val success = dataJsonObject.getBoolean("success")


                                if (success) {
                                    val firstTry = dataJsonObject.getBoolean("first_try")
                                    if(firstTry){
                                        Toast.makeText(
                                            this@ForgotPasswordActivity,
                                            "Refer email for OTP",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        val intent = Intent(this, ActivityOTP::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        intent.putExtra("mobile_number",txtForgotMobile.text.toString())
                                        startActivity(intent)
                                        finish()
                                    }else{
                                        Toast.makeText(
                                            this@ForgotPasswordActivity,
                                            "Refer your previous email",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        val intent = Intent(this, ActivityOTP::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        intent.putExtra("mobile_number",txtForgotMobile.text.toString())
                                        startActivity(intent)
                                        finish()
                                    }

                                } else {
                                    Toast.makeText(
                                        this@ForgotPasswordActivity,
                                        "Invalid Details",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }catch (e: Exception){
                                Toast.makeText(this@ForgotPasswordActivity,"Volley error $it",Toast.LENGTH_SHORT).show()
                            }


                        }, Response.ErrorListener {

                            Toast.makeText(this@ForgotPasswordActivity,"Volley error $it",Toast.LENGTH_SHORT).show()
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

                    val dialog = AlertDialog.Builder(this@ForgotPasswordActivity)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection Not Found")
                    dialog.setPositiveButton("Open Settings"){ text, listener->
                        val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit"){text,listener->
                        ActivityCompat.finishAffinity(this@ForgotPasswordActivity)
                    }
                    dialog.create()
                    dialog.show()

                }

            }

        }
    }

}