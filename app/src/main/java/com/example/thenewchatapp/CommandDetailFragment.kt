package com.example.thenewchatapp

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.thenewchatapp.MainActivity.Companion.prefs

class CommandDetailFragment : DialogFragment() {

    private lateinit var etTitle: EditText
    private lateinit var etCommand: EditText
    private lateinit var btnMic: ImageButton
    private lateinit var btnSave: Button
    private lateinit var btnBack: ImageButton

    companion object {
        private const val ARG_CATEGORY = "ARG_CATEGORY"
        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_PROMPT = "ARG_PROMPT"

        /**
         * @param category 현재 선택된 카테고리
         * @param title 수정할 명령어 제목 (null이면 신규 추가)
         * @param prompt 수정할 프롬프트 내용 (null이면 빈 값)
         */
        fun newInstance(category: String, title: String?, prompt: String?): CommandDetailFragment {
            val args = Bundle().apply {
                putString(ARG_CATEGORY, category)
                putString(ARG_TITLE, title)
                putString(ARG_PROMPT, prompt)
            }
            return CommandDetailFragment().apply { arguments = args }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_command_detail, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        @Suppress("DEPRECATION")
        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etTitle = view.findViewById(R.id.etTitle)
        etCommand = view.findViewById(R.id.etCommand)
        btnMic = view.findViewById(R.id.btnMic)
        btnSave = view.findViewById(R.id.btnSave)
        btnBack = view.findViewById(R.id.btnBack)

        // 전달받은 인자 채우기
        val initTitle = arguments?.getString(ARG_TITLE) ?: ""
        val initPrompt = arguments?.getString(ARG_PROMPT) ?: ""
        etTitle.setText(initTitle)
        etCommand.setText(initPrompt)

        // 뒤로가기 아이콘
        btnBack.setOnClickListener { dismiss() }

        // 입력 감지하여 저장 버튼 활성화
        etCommand.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnSave.isEnabled = !s.isNullOrBlank()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // 저장 (현재는 단순 닫기 처리)
        btnSave.setOnClickListener {
            // TODO: 실제 저장 로직 구현 시 Callback 또는 ViewModel 연동
            dismiss()
        }

        // 음성인식 기능 (추후 구현)
        btnMic.setOnClickListener {
            // TODO: 음성 인식 로직 연동
        }

        // **8. 저장 버튼 항상 활성화**
        btnSave.isEnabled = true

        // **7. 저장 로직: FragmentResult API 사용**
        btnSave.setOnClickListener {
            val newTitle = etTitle.text.toString().trim()
            val newPrompt = etCommand.text.toString().trim()
            parentFragmentManager.setFragmentResult(
                "easy_command_save",
                bundleOf("title" to newTitle, "prompt" to newPrompt, "original" to (arguments?.getString(ARG_TITLE)
                    ?: ""))
            )
            dismiss()
        }
    }
}
