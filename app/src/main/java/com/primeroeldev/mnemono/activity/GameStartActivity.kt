package com.primeroeldev.mnemono.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.primeroeldev.mnemono.game.Game
import com.primeroeldev.mnemono.R


class GameStartActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_start)

        this.initGameTypeSpinner()
        this.initDurationPicker()
    }

    private fun initGameTypeSpinner(): Unit
    {
        val spinner: Spinner = findViewById(R.id.spinner_game_type)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Game.getTypes()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(adapter)
    }

    private fun initDurationPicker(): Unit
    {
        this.initNumberPicker(R.id.number_picker_game_duration_hours, 23)
        this.initNumberPicker(R.id.number_picker_game_duration_minutes, 59)
        this.initNumberPicker(R.id.number_picker_game_duration_seconds, 59)
    }

    private fun initNumberPicker(id: Int, max: Int): Unit
    {
        val picker: NumberPicker = findViewById(id)
        picker.minValue = 0
        picker.maxValue = max
    }
}