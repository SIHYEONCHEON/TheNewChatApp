package com.example.thenewchatapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EasyCommandAdapter(
    private val context: Context,
    private var commandList: List<String>,
    private val onItemClick: (String) -> Unit,
    private val onAddClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_COMMAND = 0
        private const val TYPE_ADD = 1
    }

    inner class CommandViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iconCommand)
        val label: TextView = view.findViewById(R.id.textCommand)
    }

    inner class AddViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textAdd: TextView = view.findViewById(R.id.textAdd)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < commandList.size) TYPE_COMMAND else TYPE_ADD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_COMMAND) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_easy_command, parent, false)
            CommandViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_easy_command_add, parent, false)
            AddViewHolder(view)
        }
    }

    override fun getItemCount(): Int = commandList.size + 1 // +추가 포함

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is CommandViewHolder) {
            val command = commandList[position]
            holder.label.text = command
            holder.icon.setImageResource(R.mipmap.ic_launcher)

            holder.itemView.setOnClickListener {
                onItemClick(command)
            }
        } else if (holder is AddViewHolder) {
            holder.textAdd.setOnClickListener {
                onAddClick()
            }
        }
    }

    fun refreshCommands() {
        val prefs = context.getSharedPreferences("commands", Context.MODE_PRIVATE)
        val saved = prefs.getStringSet("easyCommands", null)

        commandList = if (saved != null) {
            saved.toList() + listOf("요약", "다듬기", "늘리기").filter { !saved.contains(it) }
        } else {
            listOf("요약", "다듬기", "늘리기")
        }

        notifyDataSetChanged()
    }
}
