package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.primeroeldev.mnemono.R
import java.util.*


class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val timestamp = System.currentTimeMillis()
//        val calendar = Calendar.getInstance()
////        val offset = calendar.getTimeZone().getOffset(timestamp)
////        val date = Date(timestamp + offset)
////        val df: DateFormat = DateFormat.getTimeInstance()
////        df.setTimeZone(TimeZone.getTimeZone("gmt"))
////        val gmtTime: String = df.format(Date())
//
//        val time = String.format(
//            "%d-%s-%s %s:%s:%s",
//            calendar.get(Calendar.YEAR),
//            calendar.get(Calendar.MONTH).toString().padStart(2, '0'),
//            calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0'),
//            calendar.get(Calendar.HOUR_OF_DAY).toString().padStart(2, '0'),
//            calendar.get(Calendar.MINUTE).toString().padStart(2, '0'),
//            calendar.get(Calendar.SECOND).toString().padStart(2, '0')
//        )
//        findViewById<TextView>(R.id.test).text = time
    }

    fun startGame(view: View): Unit
    {
        val intent = Intent(this, GameStartActivity::class.java)

        startActivity(intent)
    }

    fun statistics(view: View): Unit
    {
        val intent = Intent(this, GameStatisticsActivity::class.java)

        startActivity(intent)
    }

    fun listGames(view: View): Unit
    {
        val intent = Intent(this, GameIndexActivity::class.java)

        startActivity(intent)
    }

    fun about(view: View): Unit
    {
        val intent = Intent(this, AboutActivity::class.java)

        startActivity(intent)
    }
}