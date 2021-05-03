package com.example.recyclerr

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_layout.view.*

class RecyclerAdapter(countryList: MutableList<String>) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var countries: MutableList<String> = countryList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.bindData(countries[position])
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(s: String) {
            itemView.textItemTitle.text = s
            itemView.setOnClickListener {
                val position: Int = adapterPosition
                Toast.makeText(
                    itemView.context,
                    "You clicked on ${countries[position]}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}