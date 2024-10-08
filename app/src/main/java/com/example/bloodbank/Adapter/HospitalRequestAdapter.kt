package com.example.bloodbank.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.bloodbank.R
import com.example.bloodbank.databinding.HospitalRequestItemBinding
import com.example.bloodbank.model.Request

class HospitalRequestAdapter(var arr: ArrayList<Request>) :
    RecyclerView.Adapter<HospitalRequestAdapter.ViewHolder>() {

    fun updateData(newList: ArrayList<Request>) {
        arr = newList
        Log.i("Guitar", "HospitalRequestAdapter: $arr")
        notifyDataSetChanged()
    }

    inner class ViewHolder(var binding: HospitalRequestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val textView = binding.status
        fun bind(item: Request) {
            binding.requestItem = item
            when (item.bloodType) {
                "A+" -> {
                    binding.hosReqImg.setImageResource(R.drawable.a_pos)
                }

                "B+" -> {
                    binding.hosReqImg.setImageResource(R.drawable.b_pos)
                }

                "A-" -> {
                    binding.hosReqImg.setImageResource(R.drawable.a_neg)
                }

                "B-" -> {
                    binding.hosReqImg.setImageResource(R.drawable.b_neg)
                }

                "AB+" -> {
                    binding.hosReqImg.setImageResource(R.drawable.ab_pos)
                }

                "AB-" -> {
                    binding.hosReqImg.setImageResource(R.drawable.ab_neg)
                }

                "RH+" -> {
                    binding.hosReqImg.setImageResource(R.drawable.rh_pos)
                }

                "RH-" -> {
                    binding.hosReqImg.setImageResource(R.drawable.rh_neg)
                }

                "O+" -> {
                    binding.hosReqImg.setImageResource(R.drawable.o_pos)
                }

                "O-" -> {
                    binding.hosReqImg.setImageResource(R.drawable.o_neg)
                }

                else -> {
                    binding.hosReqImg.setImageResource(R.drawable.blood_drop)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = HospitalRequestItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return arr.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Request = arr[position]
        if (item.status == "pending") {
            holder.textView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.pureGray
                )
            )
        } else if (item.status == "accepted") {
            holder.textView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.green
                )
            )
        }else if(item.status == "declined"){
            holder.textView.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.primaryColor
                )
            )
        }


        holder.bind(item)
    }

}