package com.denzsbs.flipview


import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.flipview_sample_item.view.*
import java.util.*

class FlipViewSampleAdapter(var itemList: ArrayList<String>) :
    RecyclerView.Adapter<FlipViewSampleAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.flipview_sample_item, parent, false)

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindView(position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(position: Int) {
           //itemView.backView.setBackgroundColor(Color().randomColor())
           //itemView.frontView.setBackgroundColor(Color().randomColor())
        }

    }

    fun Color.randomColor(): Int {
        var rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }

}
