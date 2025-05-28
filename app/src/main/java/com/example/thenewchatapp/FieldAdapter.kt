package com.example.thenewchatapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FieldAdapter(
    private val fields: List<Field>,
    private val titleMap: Map<String, String>,
    private val contentMap: Map<String, String>,
    private val onFieldClick: (Field) -> Unit
) : RecyclerView.Adapter<FieldAdapter.FieldViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FieldViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_field, parent, false)
        return FieldViewHolder(view)
    }

    override fun onBindViewHolder(holder: FieldViewHolder, position: Int) {
        val field = fields[position]

        // ① Preview 뷰를 항상 숨깁니다.
        holder.previewView.visibility = View.GONE

        // ② 제목만 표시
        holder.titleView.text = titleMap[field.title] ?: field.title

        // ③ 클릭 리스너
        holder.itemView.setOnClickListener { onFieldClick(field) }
    }

    override fun getItemCount(): Int = fields.size

    inner class FieldViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView   = itemView.findViewById(R.id.textFieldTitle)
        val previewView: TextView = itemView.findViewById(R.id.textFieldPreview)
    }
}

//📌 이게 뭔가?
//필드 화면을 RecyclerView로 보여주기 위한 어댑터 (리스트를 그리는 도구)
//
//📍 어디서 쓰이나?
//MainFragment.kt 안의 RecyclerView에 연결됨
//
//"필드1, 필드2, 필드3..." 리스트를 보여주고 클릭 가능하게 함
//
//📦 정리
//각 필드 아이템을 그리는 역할
//
//클릭하면 -> onFieldClick() 이 실행됨 -> 필드 상세화면으로 이동
//
//📌 왜 필요해?
//필드를 스크롤 가능한 리스트로 보여주기 위해 필요함.