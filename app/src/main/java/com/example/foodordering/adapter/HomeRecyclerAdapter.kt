package com.example.foodordering.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodordering.R
import com.example.foodordering.activity.ResturantMenuActivity
import com.example.foodordering.fragment.FavResturantFragment
import com.example.foodordering.model.FoodItem
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context : Context, private val itemList : ArrayList<FoodItem>) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtFoodName : TextView = view.findViewById(R.id.txtFoodItem)
        val txtFoodPrice : TextView = view.findViewById(R.id.txtFoodRate)
        val txtFoodRating : TextView = view.findViewById(R.id.txtFoodRating)
        val imgFoodImage : ImageView = view.findViewById(R.id.imgFoodImage)
        val llContent : LinearLayout = view.findViewById(R.id.llContent)
        val imgHeart : ImageView = view.findViewById(R.id.imgHeart)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.resturant_single_row,parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val food = itemList[position]
        holder.txtFoodName.text = food.foodName
        holder.txtFoodPrice.text = "₹"+food.foodPrice.toString()+"/person"
        holder.txtFoodRating.text = food.foodRating
        Picasso.get().load(food.foodImage).error(R.drawable.app).into(holder.imgFoodImage)

        holder.llContent.setOnClickListener {
            val intent = Intent(context,ResturantMenuActivity::class.java)
            intent.putExtra("id",food.foodId.toString())
            intent.putExtra("restName",food.foodName)
            context.startActivity(intent)
        }


        var tick = false
        holder.imgHeart.setOnClickListener {
            tick = if(tick){
                holder.imgHeart.setImageResource(R.drawable.ic_hollow_heart)
                false
            } else {
                holder.imgHeart.setImageResource(R.drawable.ic_heart_red)
                true
            }
        }

    }

}

