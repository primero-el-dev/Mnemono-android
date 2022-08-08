package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.entity.Game
import com.primeroeldev.mnemono.repository.GameRepository
import com.primeroeldev.mnemono.general.TimeUtil


class GameIndexActivity : AppCompatActivity()
{
    private lateinit var gamesList: ListView
    private lateinit var gameRepository: GameRepository
    private lateinit var startedFromDatePicker: DatePicker
    private lateinit var startedToDatePicker: DatePicker
    private lateinit var typeSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_index)

        this.gameRepository = GameRepository(applicationContext, null)

        this.startedFromDatePicker = findViewById(R.id.date_picker_game_started_from)
        this.startedToDatePicker = findViewById(R.id.date_picker_game_started_to)
        this.typeSpinner = findViewById(R.id.spinner_game_type)

        this.startedFromDatePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            this.onFiltersChange(view)
        }
        this.startedToDatePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            this.onFiltersChange(view)
        }
        this.initGameTypeSpinner()

        this.initGameList()
    }

    fun onFiltersChange(view: View): Unit
    {
        this.initGameList()
    }

    private fun initGameTypeSpinner(): Unit
    {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Game.getTypes()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.typeSpinner.adapter = adapter

        val activity = this
        this.typeSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                activity.initGameList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?)
            {
                activity.initGameList()
            }
        }
    }

    private fun initGameList(): Unit
    {
        val startedFrom = TimeUtil.getFormattedDatePickerValue(this.startedFromDatePicker)
        val startedTo = TimeUtil.getFormattedDatePickerValue(this.startedToDatePicker)
        val type = this.typeSpinner.selectedItem.toString()

        // Get needed games data
        val where: ArrayList<Pair<String, Any?>> = ArrayList()
        where.add(Pair("createdAt >= ?", "$startedFrom 00:00:00"))
        where.add(Pair("createdAt <= ?", "$startedTo 23:59:59"))
        where.add(Pair("type = ?", type))
        where.add(Pair("status = ?", Game.FINISHED_STATUS))

        val games = this.gameRepository
            .findBy(where)
            .map { it as Game } as ArrayList

        // View
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            games.map { it.createdAt + " - " + it.type }
        )

        this.gamesList = findViewById(R.id.game_index_list)
        this.gamesList.adapter = adapter
        this.gamesList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val intent = Intent(applicationContext, GameResultActivity::class.java)
            intent.putExtra(ParamDictionary.GAME_ID_KEY, games[position]._id)
            startActivity(intent)
        }
    }

    private fun getFormattedDatePickerValue(picker: DatePicker): String
    {
        return "${picker.year}-${(picker.month + 1)}-${picker.dayOfMonth}"
    }
}