package com.example.thenewchatapp

import android.app.AlertDialog
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EasyCommandEntryAdapter(
    private var entries: List<Pair<String, String>>, // 제목 to 프롬프트
    private val onEditClick: (String, String) -> Unit,
    private val onDeleteClick: (String) -> Unit,
    private val onPromptClick: (String) -> Unit,
    private val onAddClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_COMMAND = 0
        private const val TYPE_ADD = 1
    }

    inner class EntryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textTitle: TextView = view.findViewById(R.id.textTitle)
        val textPrompt: TextView = view.findViewById(R.id.textPrompt)
        val btnExpand: ImageButton = view.findViewById(R.id.btnExpand)
        val btnMenu: ImageButton = view.findViewById(R.id.btnMenu)
        val expandedLayout: View = view.findViewById(R.id.expandedLayout)
    }

    inner class AddViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textAdd: TextView = view.findViewById(R.id.textAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ADD) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_easy_command_add, parent, false)
            AddViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_command_entry, parent, false)
            EntryViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_ADD) {
            (holder as AddViewHolder).textAdd.setOnClickListener {
                onAddClick()
            }
        } else {
            val (title, prompt) = entries[position]
            val vh = holder as EntryViewHolder

            // 1) 제목만 기본 노출
            vh.textTitle.text = title
            vh.textPrompt.text = prompt

            // expandedLayout 은 기본 숨김
            vh.expandedLayout.visibility = View.GONE
            vh.btnExpand.rotation = 0f

            // 2) 화살표 클릭 시 expand/collapse
            vh.btnExpand.setOnClickListener {
                val expand = vh.expandedLayout.visibility != View.VISIBLE
                vh.expandedLayout.visibility = if (expand) View.VISIBLE else View.GONE
                // 화살표 회전
                vh.btnExpand.animate().rotation(if (expand) 180f else 0f).start()
            }

            holder.textPrompt.apply {
                text = prompt
                // 1) 클릭 시 입력 필드에 값 채워주기
                setOnClickListener { onPromptClick(prompt) }
                // 2) 내부 스크롤 가능 설정
                movementMethod = ScrollingMovementMethod()
                setOnTouchListener { v, _ ->
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    false
                }
            }

            // 3) 메뉴 버튼 (•••)
            vh.btnMenu.setOnClickListener {
                PopupMenu(vh.itemView.context, vh.btnMenu).apply {
                    menu.add("수정").setOnMenuItemClickListener {
                        onEditClick(title, prompt)
                        true
                    }
                    menu.add("삭제").setOnMenuItemClickListener {
                        AlertDialog.Builder(vh.itemView.context)
                            .setTitle("삭제 확인")
                            .setMessage("정말 삭제하시겠습니까?")
                            .setPositiveButton("삭제") { _, _ -> onDeleteClick(title) }
                            .setNegativeButton("취소", null)
                            .show()
                        true
                    }
                    show()
                }
            }

            // 4) 내용(TextView) 스크롤 가능하게
            vh.textPrompt.movementMethod = ScrollingMovementMethod()
            vh.textPrompt.setOnTouchListener { v, _ ->
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            }
        }
    }



    override fun getItemCount(): Int = entries.size

    override fun getItemViewType(position: Int): Int {
        return if (entries[position].first == "+") TYPE_ADD else TYPE_COMMAND
    }

    fun updateList(newEntries: List<Pair<String, String>>) {
        entries = newEntries
        notifyDataSetChanged()
    }
}
