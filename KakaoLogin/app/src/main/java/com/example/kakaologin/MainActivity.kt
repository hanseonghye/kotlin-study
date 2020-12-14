package com.example.kakaologin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvNickname.text = intent.getStringExtra("name")
        intent.getStringExtra("name")?.let { Log.d("name is : ", it) }


        Glide.with(this)
            .load(intent.getStringExtra("profile"))
            .into(imgProfile)
    }
}