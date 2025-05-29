package com.example.thenewchatapp

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.thenewchatapp.MainActivity.Companion.prefs

class AddCommandActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = getSharedPreferences("settings", MODE_PRIVATE)
        AppCompatDelegate.setDefaultNightMode(
            prefs.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        )

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_command)

        val editCommand = findViewById<EditText>(R.id.editCommand)
        val btnSave = findViewById<Button>(R.id.btnSave)

        btnSave.setOnClickListener {
            val commandText = editCommand.text.toString().trim()

            if (commandText.isNotEmpty()) {
                val prefs = getSharedPreferences("commands", Context.MODE_PRIVATE)
                val editor = prefs.edit()

                val currentList = prefs.getStringSet("easyCommands", mutableSetOf())?.toMutableSet() ?: mutableSetOf()
                currentList.add(commandText)

                editor.putStringSet("easyCommands", currentList)
                editor.apply()

                Toast.makeText(this, "명령어가 추가되었습니다", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "명령어를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
