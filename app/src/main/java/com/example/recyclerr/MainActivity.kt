package com.example.recyclerr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Collections.*

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerAdapter: RecyclerAdapter
    private var countryList = mutableListOf<String>()
    private lateinit var deletedCountry: String

    private var simpleCallback =
        object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP.or(ItemTouchHelper.DOWN),
            ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val startPosition = viewHolder.adapterPosition
                val endPosition = target.adapterPosition

                swap(countryList, startPosition, endPosition)
                recyclerView.adapter!!.notifyItemMoved(startPosition, endPosition)
                return true
            }

            // xu ly cac su kien khi vuot item theo huong khac nhau
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        deletedCountry = countryList[position]
                        countryList.removeAt(position)
                        recyclerAdapter.notifyItemRemoved(position)

                        Snackbar.make(
                            recyclerViewContainer,
                            "$deletedCountry is deleted",
                            Snackbar.LENGTH_LONG
                        ).setAction("Undo") {
                            countryList.add(position, deletedCountry)
                            recyclerAdapter.notifyItemInserted(position)
                        }.show()
                    }

                    ItemTouchHelper.RIGHT -> {
                        val editText = EditText(this@MainActivity)
                        editText.setText(countryList[position])
                        val builder = AlertDialog.Builder(this@MainActivity)
                        builder.setTitle("Update an Item")
                        builder.setCancelable(true)
                        builder.setView(editText)
                        builder.setNegativeButton(
                            "cancel"
                        ) { dialog, which ->
                            countryList.clear()
                            getData()
                            recyclerViewContainer.adapter!!.notifyDataSetChanged()
                        }

                        builder.setPositiveButton(
                            "update"
                        ) { dialog, which ->
                            countryList.set(position, editText.text.toString())
                            recyclerViewContainer.adapter!!.notifyItemChanged(position)
                        }
                        builder.show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getData()
        recyclerAdapter = RecyclerAdapter(countryList)
        recyclerViewContainer.adapter = recyclerAdapter

//        val lay =
//            LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.item_animation_fall_down))
//        lay.delay = 0.20f
//        lay.order = LayoutAnimationController.ORDER_NORMAL
//        recyclerViewContainer.layoutAnimation = lay

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(recyclerViewContainer)

        swipeRefreshLayout.setOnRefreshListener {
            countryList.clear()
            getData()
            recyclerViewContainer.adapter!!.notifyDataSetChanged()
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getData() {
        for (i in 0..30) {
            countryList.add("View Holder $i")
        }
    }
}