package com.example.thenewchatapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.example.thenewchatapp.MainActivity.Companion.prefs

class ReceivedEditActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private var messagePosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences("settings", MODE_PRIVATE)
        AppCompatDelegate.setDefaultNightMode(
            prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_received_edit)

        // Toolbar 설정
        val toolbar = findViewById<Toolbar>(R.id.toolbar_received_edit)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        // 직접 삽입한 저장 버튼
        val btnSave = findViewById<TextView>(R.id.btn_save_direct)
        btnSave.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("editedText", editText.text.toString())
                putExtra("messagePosition", messagePosition)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        // 뒤로가기 동작
        toolbar.setNavigationOnClickListener {
            finish()
        }

        // EditText 설정
        editText = findViewById(R.id.editTextReceived)
        messagePosition = intent.getIntExtra("messagePosition", -1)
        val originalText = intent.getStringExtra("originalText") ?: ""
        editText.setText(originalText)
    }
}
