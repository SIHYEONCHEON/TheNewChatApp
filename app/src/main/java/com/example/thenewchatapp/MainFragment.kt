package com.example.thenewchatapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.fragment.app.activityViewModels

class MainFragment : Fragment(R.layout.activity_field) {

    private lateinit var btnPlus: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnVoice: ImageButton
    private lateinit var btnDropdown: ImageButton
    private val viewModel: FieldViewModel by activityViewModels()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnVoice = view.findViewById(R.id.btnVoice)
        btnPlus         = view.findViewById(R.id.btnPlus)
        recyclerView    = view.findViewById(R.id.recyclerView)
        btnDropdown = view.findViewById(R.id.btnFieldDropdown)
        fragmentContainer = view.findViewById(R.id.fragmentContainer)
        backButton = view.findViewById(R.id.backButton)

        // backButton 뷰 찾기 (Fragment 안에서는 view.findViewById)
        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            // 이전 화면으로 돌아가기
            requireActivity().finish()
            // 또는 requireActivity().onBackPressed()
        }

        val recycler = view.findViewById<RecyclerView>(R.id.recyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        // ViewModel 기본 제목 초기화 (한 번만)
        viewModel.initTitles(fieldKeys)

        // ② 드롭다운 메뉴: ViewModel 에 저장된 제목(label) 사용
        btnDropdown.setOnClickListener { anchor ->
            PopupMenu(requireContext(), anchor).apply {
                // menu에 label(=getTitle) 추가
                fieldKeys.forEach { key ->
                    menu.add(viewModel.getTitle(key))
                }
                setOnMenuItemClickListener { item ->
                    // 선택된 label → 원본 key 찾기
                    val selectedLabel = item.title.toString()
                    val key = fieldKeys.first { viewModel.getTitle(it) == selectedLabel }

                    // ③ 상세화면으로 이동, content도 동기화
                    val frag = FieldDetailFragment.newInstance(
                        key,
                        viewModel.getContent(key)
                    )
                    parentFragmentManager.beginTransaction()
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
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, frag)
                .addToBackStack(null)
                .commit()
        }
        recyclerView.adapter = fieldAdapter

        // 2) LiveData 관찰 → 리스트 갱신
        viewModel.titles.observe(viewLifecycleOwner)   { fieldAdapter.notifyDataSetChanged() }
        viewModel.contents.observe(viewLifecycleOwner) { fieldAdapter.notifyDataSetChanged() }



        // ➕ 버튼 팝업 메뉴
        btnPlus.setOnClickListener {
            PopupMenu(requireContext(), it).apply {
                menu.add("챗봇").setOnMenuItemClickListener {
                    startActivity(Intent(requireContext(), ChatActivity::class.java))
                    true
                }
                menu.add("일반 글 화면").setOnMenuItemClickListener {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    true
                }
                show()
            }

        }

        // 음성 버튼 (추후 구현)
        btnVoice.setOnClickListener {
            Toast.makeText(requireContext(), "음성 인식 기능은 추후 추가됩니다.", Toast.LENGTH_SHORT).show()
        }

    }

}
