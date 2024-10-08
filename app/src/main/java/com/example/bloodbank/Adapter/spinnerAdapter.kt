package com.example.bloodbank.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.bloodbank.R

class spinnerAdapter(var Context: Context, var list: ArrayList<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val rootView: View =
            LayoutInflater.from(Context).inflate(R.layout.blood_string, parent, false)
        val name: TextView = rootView.findViewById(R.id.bloodName)
        name.text = list.get(position)
        return rootView

    }

}