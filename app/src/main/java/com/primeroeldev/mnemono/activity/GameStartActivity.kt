package com.primeroeldev.mnemono.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.game.Game
import com.primeroeldev.mnemono.game.GameRepository
import com.primeroeldev.mnemono.validation.getErrorsOfGame


class GameStartActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_start)

        this.initGameTypeSpinner()
        this.initDurationPicker()
    }

    fun submit(view: View): Unit
    {
        val game = Game()
        game.type = (findViewById(R.id.spinner_game_type) as Spinner).getSelectedItem().toString()
        game.status = Game.NOT_STARTED_STATUS
        game.allAnswersCount = Integer.parseInt((findViewById(R.id.edit_text_game_items_count) as EditText).text.toString())
        game.includedInStatistics = (findViewById(R.id.check_box_game_included_in_statistics) as CheckBox).isChecked
        game.durationInSeconds = this.getDurationInSeconds()

        val errors = getErrorsOfGame(game)

        if (errors.isEmpty()) {
            val gameId = GameRepository(applicationContext, null).insert(game)

            val intent = Intent(this, GamePlayActivity::class.java)
            intent.putExtra("gameId", gameId)
            startActivity(intent)
        }

        for ((_, error) in errors) {
            Toast.makeText(applicationContext, getResources().getIdentifier(error, "string", packageName), Toast.LENGTH_SHORT).show()
        }
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

    private fun getDurationInSeconds(): Int
    {
        return (findViewById(R.id.number_picker_game_duration_hours) as NumberPicker).value * 3600
            + (findViewById(R.id.number_picker_game_duration_minutes) as NumberPicker).value * 60
            + (findViewById(R.id.number_picker_game_duration_seconds) as NumberPicker).value
    }
}