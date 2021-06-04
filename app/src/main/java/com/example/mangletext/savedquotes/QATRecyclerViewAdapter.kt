package com.example.mangletext.savedquotes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mangletext.databinding.QatItemBinding

class QATRecyclerViewAdapter(private val qatList: List<QATObject>,
    private val onItemClicked: (QATObject) -> Unit): RecyclerView.Adapter<QATRecyclerViewAdapter.QATViewHolder>() {


    class QATViewHolder(private var binding: QatItemBinding,
    onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init{
            itemView.setOnClickListener {
                onItemClicked(adapterPosition)
            }
        }

        fun bind(qat: QATObject) {
            binding.qat = qat
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): QATViewHolder {

        val viewHolder = QatItemBinding.inflate(LayoutInflater.from(parent.context))
        return QATViewHolder(viewHolder){
            onItemClicked(
                qatList[it]
            )
        }
    }

    override fun onBindViewHolder(holder: QATViewHolder, position: Int) {
        val qatObject = qatList[position]
        holder.bind(qatObject)
    }

    override fun getItemCount(): Int {
        return qatList.size
    }

}