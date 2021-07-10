package com.example.foodordering.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodordering.R
import com.example.foodordering.adapter.HomeRecyclerAdapter
import com.example.foodordering.model.FoodItem
import com.example.foodordering.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.Comparator
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var recyclerDashboard : RecyclerView
    private lateinit var layoutManager : RecyclerView.LayoutManager
    lateinit var progressLayout : RelativeLayout
    private lateinit var progressBar : ProgressBar

    val foodInfoList = arrayListOf<FoodItem>()

//    var ratingComparator = Comparator<Book> { book1,book2 ->
//        if(book1.bookRating.compareTo(book2.bookRating,true)==0){
//            book1.bookName.compareTo(book2.bookName,true)
//        }else {
//            book1.bookRating.compareTo(book2.bookRating, true)
//        }
//    }

    lateinit var recyclerAdapter: HomeRecyclerAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //setHasOptionsMenu(true)

        recyclerDashboard = view.findViewById(R.id.recycleDashboard) as RecyclerView


        layoutManager = LinearLayoutManager(activity)

        progressLayout = view.findViewById(R.id.progressLayout)

        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE

        recyclerAdapter = HomeRecyclerAdapter(activity as Context,foodInfoList)

        recyclerDashboard.adapter = recyclerAdapter

        recyclerDashboard.layoutManager = layoutManager


        val queue = Volley.newRequestQueue(activity as Context)

        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnection(activity as Context)){

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
                                val foodObject = FoodItem(
                                    foodJsonObject.getString("id").toInt(),
                                    foodJsonObject.getString("name"),
                                    foodJsonObject.getString("rating"),
                                    foodJsonObject.getString("cost_for_one").toInt(),
                                    foodJsonObject.getString("image_url")
                                )
                                foodInfoList.add(foodObject)
                            }
                            recyclerAdapter.notifyDataSetChanged()
                        } else {
                            Toast.makeText(
                                activity as Context,
                                "Some Error Occurred!!",
                                Toast.LENGTH_LONG
                            ).show()
                        }


                    },
                    Response.ErrorListener {

                        //Here we will handle the error
                        //println("error is $it")
                        if (activity != null) {
                            Toast.makeText(
                                activity as Context,
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

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Settings"){ text, listener->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text,listener->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create()
            dialog.show()

        }



        return view


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home,menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id = item?.itemId
//        if(id==R.id.action_sort){
//            Collections.sort(bookInfoList,ratingComparator)
//            bookInfoList.reverse()
//        }
//
//        recyclerAdapter.notifyDataSetChanged()
//        return super.onOptionsItemSelected(item)
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}