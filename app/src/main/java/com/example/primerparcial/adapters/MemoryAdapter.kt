package com.example.primerparcial.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.primerparcial.R
import com.example.primerparcial.entities.Memory
import java.text.SimpleDateFormat
import java.util.*

class MemoryAdapter(
    private var memoryList : MutableList<Memory>,
    private var onClick: (Int)->Unit): RecyclerView.Adapter<MemoryAdapter.MemoryHolder>()
{
    class MemoryHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View
        init{
            this.view = v
        }

        fun setPlace(place : String){
            var txtPlace : TextView = view.findViewById(R.id.txtPlaceItem)
            txtPlace.text = place
        }

        fun setDate(date : String){
            var txtDate : TextView = view.findViewById(R.id.txtDateItem)
            txtDate.text = date
        }

        fun setImage(url : String){
            Glide.with(view)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(view.findViewById(R.id.imgMemoryImageItem))
        }
        fun getCard() : CardView {
            return view.findViewById(R.id.cardViewItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.memory_item, parent, false)
        return MemoryAdapter.MemoryHolder(view)
    }

    override fun onBindViewHolder(holder: MemoryHolder, position: Int) {
        holder.setPlace(memoryList[position].country + " - " + memoryList[position].city)
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val strDate: String = formatter.format(memoryList[position].date)

        holder.setDate(strDate)
        holder.setImage(memoryList[position].urlImage)
        holder.getCard().setOnClickListener{
            onClick(position)
        }
    }

    override fun getItemCount(): Int {
        return memoryList.size
    }

}