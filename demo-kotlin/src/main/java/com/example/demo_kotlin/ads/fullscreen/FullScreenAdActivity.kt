package com.example.demo_kotlin.ads.fullscreen

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.demo_kotlin.R
import com.example.demo_kotlin.misc.Constant

abstract class FullScreenAdActivity : AppCompatActivity() {

    protected lateinit var adUnitName: String
    protected lateinit var statusTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen)

        adUnitName = intent.getStringExtra(Constant.adUnitNameKey) ?: ""
        title = adUnitName

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        statusTextView = findViewById(R.id.status_text_view)

        loadAd()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    protected abstract fun loadAd()
    protected abstract fun showAd()
}