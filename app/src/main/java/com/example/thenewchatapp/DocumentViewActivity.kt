package com.example.thenewchatapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.thenewchatapp.MainActivity.Companion.prefs
import java.io.File

class DocumentViewActivity : AppCompatActivity() {

    private lateinit var editTextContent: EditText
    private lateinit var saveButton: Button
    private var fileName: String? = null  // 전달받은 파일명 저장용

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences("settings", MODE_PRIVATE)
        AppCompatDelegate.setDefaultNightMode(
            prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document_view)

        // XML의 EditText와 저장 버튼 연결
        editTextContent = findViewById(R.id.editTextDocumentContent)
        saveButton = findViewById(R.id.buttonSaveDocument)

        // ✅ ListActivity로부터 전달받은 파일명
        fileName = intent.getStringExtra("fileName")

        // ✅ 실제 파일에서 내용을 읽어서 EditText에 표시
        fileName?.let { name ->
            val file = File(filesDir, name)
            if (file.exists()) {
                val content = file.readText()
                editTextContent.setText(content)
            } else {
                Toast.makeText(this, "파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        // ✅ 저장 버튼 클릭 시 파일 덮어쓰기 저장
        saveButton.setOnClickListener {
            fileName?.let { name ->
                val file = File(filesDir, name)
                file.writeText(editTextContent.text.toString())
                Toast.makeText(this, "저장되었습니다", Toast.LENGTH_SHORT).show()
                finish() // 저장 후 닫기
            }
        }
    }
}
