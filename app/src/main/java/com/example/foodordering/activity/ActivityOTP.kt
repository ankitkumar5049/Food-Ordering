package com.example.foodordering.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
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

class ActivityOTP : AppCompatActivity() {
    lateinit var txtOTP:EditText
    lateinit var txtNewPass:EditText
    lateinit var txtConfirmPass:EditText
    lateinit var btnSubmit:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)

        txtOTP = findViewById(R.id.txtOTP)
        txtNewPass = findViewById(R.id.txtNewPass)
        txtConfirmPass = findViewById(R.id.txtConfirmPass)
        btnSubmit = findViewById(R.id.btnSubmit)

        val mobile = intent.getStringExtra("mobile_number")


        btnSubmit.setOnClickListener {
            if(txtNewPass.text.toString().length<4 || txtConfirmPass.text.toString().length<4){
                Toast.makeText(this@ActivityOTP,"Password length must be grater",Toast.LENGTH_SHORT).show()
            }
            else if(txtNewPass.text.toString() != txtConfirmPass.text.toString()){
                Toast.makeText(this@ActivityOTP,"Password does not match",Toast.LENGTH_SHORT).show()
            }
            else{

                val queue = Volley.newRequestQueue(this@ActivityOTP)
                val url = "http://13.235.250.119/v2/register/fetch_result"

                val jsonParams = JSONObject()
                jsonParams.put("mobile_number",mobile.toString())
                jsonParams.put("password",txtNewPass.text.toString())
                jsonParams.put("otp",txtOTP.text.toString())

                if(ConnectionManager().checkConnection(this@ActivityOTP)){
                    val jsonRequest = object :JsonObjectRequest(Method.POST,url,jsonParams,Response.Listener {

                        try {

                            val dataJsonObject = it.getJSONObject("data")
                            val success = dataJsonObject.getBoolean("success")
                            if (success) {
                                Toast.makeText(
                                    this@ActivityOTP,
                                    "Password changed successfully",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                val intent = Intent(this, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@ActivityOTP,
                                    "Wrong details !",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }catch (e:Exception){
                            Toast.makeText(this@ActivityOTP,"Volley error $it",Toast.LENGTH_SHORT).show()
                        }


                    },Response.ErrorListener {

                        Toast.makeText(this@ActivityOTP,"Volley error $it",Toast.LENGTH_SHORT).show()
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

                    val dialog = AlertDialog.Builder(this@ActivityOTP)
                    dialog.setTitle("Error")
                    dialog.setMessage("Internet Connection Not Found")
                    dialog.setPositiveButton("Open Settings"){ text, listener->
                        val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit"){text,listener->
                        ActivityCompat.finishAffinity(this@ActivityOTP)
                    }
                    dialog.create()
                    dialog.show()

                }

            }
        }


    }
}