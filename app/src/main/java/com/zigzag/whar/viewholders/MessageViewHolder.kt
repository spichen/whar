package com.zigzag.whar.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.zigzag.whar.R

/**
 * Created by salah on 21/1/18.
 */
class MessageViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    fun setMessage(message : String){
        itemView.findViewById<TextView>(R.id.tv_message).text = message
    }
}