package com.example.thenewchatapp

import android.app.AlertDialog
import android.graphics.PorterDuff
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EasyCommandCategoryAdapter(
    private val categories: List<String>,
    val onCategoryClick: (String) -> Unit,
    val onDeleteConfirmed: (String) -> Unit
) : RecyclerView.Adapter<EasyCommandCategoryAdapter.CategoryViewHolder>() {

    private var onAddCategoryClick: (() -> Unit)? = null

    companion object {
        private const val TYPE_CATEGORY = 0
        private const val TYPE_ADD = 1
    }

    inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgIcon: ImageView = view.findViewById(R.id.imgCategory)
        val tvLabel: TextView = view.findViewById(R.id.tvCategoryLabel)
        val btnClose: ImageButton? = view.findViewById(R.id.btnClose)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == categories.size) TYPE_ADD else TYPE_CATEGORY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layout = if (viewType == TYPE_ADD) R.layout.item_category_add else R.layout.item_category
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val context = holder.itemView.context

        // 1) 테마에서 colorOnSurface 컬러 가져오기 (MaterialComponents R 사용)
        val typedValue = TypedValue()
           context.theme.resolveAttribute(
                   com.google.android.material.R.attr.colorOnSurface,
                   typedValue,
                  true
           )
           val onSurfaceColor = typedValue.data

        // padding 8dp → px
        val pad = (8 * context.resources.displayMetrics.density).toInt()

        if (getItemViewType(position) == TYPE_ADD) {
            // 추가 아이템
            holder.imgIcon.setImageResource(R.drawable.ic_add)
            holder.imgIcon.setColorFilter(onSurfaceColor, PorterDuff.Mode.SRC_IN)

            // 리소스로 분리한 경우
            // holder.tvLabel.text = context.getString(R.string.add_category)
            // 간단히 하드코딩해도 상관없다면:
            holder.tvLabel.text = "추가"

            holder.tvLabel.setTextColor(onSurfaceColor)
            holder.btnClose?.visibility = View.GONE

            holder.itemView.setPadding(pad, 0, pad, 0)
            holder.itemView.setOnClickListener { onAddCategoryClick?.invoke() }

        } else {
            val category = categories[position]

            // 2) 빨강/노랑/초록 배경 순환
            val bgList = listOf(
                R.drawable.bg_circle_red,
                R.drawable.bg_circle_yellow,
                R.drawable.bg_circle_green
            )
            holder.imgIcon.setBackgroundResource(bgList[position % bgList.size])
            holder.imgIcon.clearColorFilter()

            holder.tvLabel.text = category
            holder.tvLabel.setTextColor(onSurfaceColor)

            holder.itemView.setPadding(pad, 0, pad, 0)

            // 3) 닫기 버튼 가시 + 컬러 필터
            holder.btnClose?.visibility = View.VISIBLE
            holder.btnClose?.setColorFilter(onSurfaceColor, PorterDuff.Mode.SRC_IN)

            holder.itemView.setOnClickListener { onCategoryClick(category) }
            holder.btnClose?.setOnClickListener {
                AlertDialog.Builder(context)
                    // 리소스 분리한 경우:
                    // .setTitle(R.string.delete_category)
                    // .setMessage(R.string.delete_category_confirm)
                    .setTitle("카테고리 삭제")
                    .setMessage("정말 삭제하시겠습니까?")
                    .setPositiveButton("삭제") { _, _ -> onDeleteConfirmed(category) }
                    .setNegativeButton("취소", null)
                    .show()
            }
        }
    }

    override fun getItemCount(): Int = categories.size + 1

    fun setOnAddCategoryClickListener(listener: () -> Unit) {
        onAddCategoryClick = listener
    }
}
