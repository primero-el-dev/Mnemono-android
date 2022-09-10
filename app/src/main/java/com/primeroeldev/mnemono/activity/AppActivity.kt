package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.primeroeldev.mnemono.R


open class AppActivity : AppCompatActivity()
{
    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.navigation_menu, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        return when (item.itemId) {
            R.id.navigation_main_activity -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

                return true
            }
            R.id.navigation_game_start_activity -> {
                val intent = Intent(this, GameStartActivity::class.java)
                startActivity(intent)

                return true
            }
            R.id.navigation_game_index_activity -> {
                val intent = Intent(this, GameIndexActivity::class.java)
                startActivity(intent)

                return true
            }
            R.id.navigation_game_statistics_activity -> {
                val intent = Intent(this, GameStatisticsActivity::class.java)
                startActivity(intent)

                return true
            }
            else -> false
        }
    }
}