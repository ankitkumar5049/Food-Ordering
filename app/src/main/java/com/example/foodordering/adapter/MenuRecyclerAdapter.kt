package com.example.foodordering.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodordering.R
import com.example.foodordering.model.FoodItem
import com.example.foodordering.model.MenuItem

class MenuRecyclerAdapter(val context : Context, private val itemList : ArrayList<MenuItem>) :RecyclerView.Adapter<MenuRecyclerAdapter.MenuViewHolder>(){

    class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtFoodName : TextView = view.findViewById(R.id.txtFoodItem)
        val txtFoodPrice : TextView = view.findViewById(R.id.txtFoodRate)
        val txtSerialNumber :TextView = view.findViewById(R.id.txtSerialNumber)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.resturant_menu_single_row,parent,false)
        return MenuRecyclerAdapter.MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val food = itemList[position]
        holder.txtFoodName.text = food.foodName
        holder.txtFoodPrice.text = "Rs."+food.foodPrice
        holder.txtSerialNumber.text = (position+1).toString()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}