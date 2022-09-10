package com.primeroeldev.mnemono.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.primeroeldev.mnemono.R

class DebugActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_debug)

//        if (callingActivity?.getClassName() != GameStartActivity::class.java.toString()) {
//        findViewById<TextView>(R.id.test).text = GameStartActivity::class.java.toString()
        findViewById<TextView>(R.id.test).text = callingActivity?.getClassName()
    }
}