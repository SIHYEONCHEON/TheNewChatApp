package com.example.thenewchatapp

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.getValue
import androidx.activity.viewModels

class FieldActivity : AppCompatActivity() {

    private lateinit var btnPlus: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnVoice: ImageButton
    private lateinit var btnDropdown: ImageButton
    private val viewModel: FieldViewModel by viewModels()
    private lateinit var fragmentContainer: FrameLayout
    private lateinit var backButton: ImageButton

    private val easyCommandMap = mutableMapOf(
        "요약" to mutableListOf("간단 요약: 이 내용을 간단하게 요약해줘"),
        "다듬기" to mutableListOf("문장 정리: 더 자연스럽게 바꿔줘"),
        "늘리기" to mutableListOf("내용 확장: 더 길게 써줘")
    )

    // 필드 목록
    private val fields = listOf(
        Field("목적", ""), Field("주제", ""), Field("독자", ""), Field("형식 혹은 구조", ""),
        Field("근거자료", ""), Field("어조", ""), Field("분량, 문체, 금지어 등", ""), Field("추가사항", "")
    )

    private val fieldKeys = listOf(
        "목적","주제","독자","형식 혹은 구조",
        "근거자료","어조","분량, 문체, 금지어 등","추가사항"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field)

        btnVoice = findViewById(R.id.btnVoice)
        btnPlus         = findViewById(R.id.btnPlus)
        recyclerView    = findViewById(R.id.recyclerView)
        btnDropdown = findViewById(R.id.btnFieldDropdown)
        fragmentContainer = findViewById(R.id.fragmentContainer)
        backButton = findViewById(R.id.backButton)

        val recycler = findViewById<RecyclerView>(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(this)

        // ViewModel 기본 제목 초기화 (한 번만)
        viewModel.initTitles(fieldKeys)

        // ② 드롭다운 메뉴: ViewModel 에 저장된 제목(label) 사용
        btnDropdown.setOnClickListener { anchor ->
            PopupMenu(this@FieldActivity, anchor).apply {
                fieldKeys.forEach { key ->
                    menu.add(viewModel.getTitle(key))
                }
                setOnMenuItemClickListener { item ->
                    val key = fieldKeys.first { viewModel.getTitle(it) == item.title }
                    val frag = FieldDetailFragment.newInstance(key, viewModel.getContent(key))
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainer, frag)
                        .addToBackStack(null)
                        .commit()
                    true
                }
                show()
            }
        }

        // **1) ViewModel에 기본 필드명 채우기**
        val fieldKeys = fields.map { it.title }
        viewModel.initTitles(fieldKeys)

        // RecyclerView 설정
        recyclerView.layoutManager = LinearLayoutManager(this)
        val fieldAdapter = FieldAdapter(
            fields = fields,
            titleMap   = viewModel.titles.value   ?: emptyMap(),
            contentMap = viewModel.contents.value ?: emptyMap()
        ) { field ->
            // 상세 화면으로 이동 (MainFragment에서 내용 저장 제거)
            val frag = FieldDetailFragment.newInstance(
                field.title,
                viewModel.getContent(field.title)
            )
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = fieldAdapter

        // 2) LiveData 관찰 → 리스트 갱신
        viewModel.titles.observe(this@FieldActivity)   { fieldAdapter.notifyDataSetChanged() }
        viewModel.contents.observe(this@FieldActivity) { fieldAdapter.notifyDataSetChanged() }

        // ➕ 버튼 팝업 메뉴
        btnPlus.setOnClickListener {
            PopupMenu(this@FieldActivity, it).apply {
                menu.add("챗봇").setOnMenuItemClickListener {
                    startActivity(Intent(this@FieldActivity, ChatActivity::class.java))
                    true
                }
                menu.add("일반 글 화면").setOnMenuItemClickListener {
                    startActivity(Intent(this@FieldActivity, MainActivity::class.java))
                    true
                }
                show()
            }
        }

        // 음성 버튼 (추후 구현)
        btnVoice.setOnClickListener {
            Toast.makeText(this, "음성 인식 기능은 추후 추가됩니다.", Toast.LENGTH_SHORT).show()
        }

    }

}
