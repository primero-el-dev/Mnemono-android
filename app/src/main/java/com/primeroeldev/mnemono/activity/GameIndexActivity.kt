package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.entity.Game
import com.primeroeldev.mnemono.repository.GameRepository
import com.primeroeldev.mnemono.entity.EntityInterface


class GameIndexActivity : AppCompatActivity()
{
    private lateinit var gamesList: ListView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_index)

        val games = GameRepository(applicationContext, null).findBy(ArrayList(), "createdAt DESC")

        this.initGamesList(games)
    }

    private fun initGamesList(games: ArrayList<EntityInterface>): Unit
    {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            games.map { g -> (g as Game).createdAt }
        )

        this.gamesList = findViewById(R.id.game_index_list)
        this.gamesList.adapter = adapter
        this.gamesList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(applicationContext, GameResultActivity::class.java)
            intent.putExtra(ParamDictionary.GAME_ID_KEY, games[position]._id)
            startActivity(intent)
        }
    }
}