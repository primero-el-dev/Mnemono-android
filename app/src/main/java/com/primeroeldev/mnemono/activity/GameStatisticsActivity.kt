package com.primeroeldev.mnemono.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.entity.Game
import com.primeroeldev.mnemono.general.safeSubstring
import com.primeroeldev.mnemono.repository.GameRepository
import kotlin.math.roundToInt


class GameStatisticsActivity : AppCompatActivity()
{
    private val GROUP_BY_NONE = "None"
    private val GROUP_BY_YEAR = "Year"
    private val GROUP_BY_MONTH = "Month"
    private val GROUP_BY_DAY = "Day"
    private val textSize = 18f
    private lateinit var gameRepository: GameRepository
    private lateinit var startedFromDatePicker: DatePicker
    private lateinit var startedToDatePicker: DatePicker
    private lateinit var groupByDateSpinner: Spinner


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_statistics)

        this.gameRepository = GameRepository(applicationContext, null)

        this.startedFromDatePicker = findViewById(R.id.date_picker_game_started_from)
        this.startedToDatePicker = findViewById(R.id.date_picker_game_started_to)
        this.groupByDateSpinner = findViewById(R.id.spinner_game_group_by)

        this.initDatePickers()
        this.initGroupByDateSpinner()
        this.initCharts()
    }

    fun onFiltersChange(view: View): Unit
    {
        this.initCharts()
    }

    private fun initDatePickers(): Unit
    {
        this.startedFromDatePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            this.onFiltersChange(view)
        }
        this.startedToDatePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            this.onFiltersChange(view)
        }
    }

    private fun getFormattedDatePickerValue(picker: DatePicker): String
    {
        return "${picker.year}-${(picker.month + 1)}-${picker.dayOfMonth}"
    }

    private fun initGroupByDateSpinner(): Unit
    {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            arrayOf(this.GROUP_BY_NONE, this.GROUP_BY_YEAR, this.GROUP_BY_MONTH, this.GROUP_BY_DAY)
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        this.groupByDateSpinner.adapter = adapter

        val activity = this
        this.groupByDateSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                activity.initCharts()
            }

            override fun onNothingSelected(parent: AdapterView<*>?)
            {
                activity.initCharts()
            }
        }
    }

    private fun initCharts(): Unit
    {
        val startedFrom = this.getFormattedDatePickerValue(this.startedFromDatePicker)
        val startedTo = this.getFormattedDatePickerValue(this.startedToDatePicker)
        val groupBy = findViewById<Spinner>(R.id.spinner_game_group_by).selectedItem.toString()

        val where: ArrayList<Pair<String, Any?>> = ArrayList()
        where.add(Pair("createdAt >= ?", startedFrom))
        where.add(Pair("createdAt <= ?", startedTo))
        where.add(Pair("includedInStatistics = ?", 1))

        val games = this.gameRepository
            .findBy(where)
            .map { it as Game } as ArrayList

        val wordsGames = this.groupGames(games.filter { it.type == Game.WORDS_TYPE } as ArrayList, groupBy)
        val numbersGames = this.groupGames(games.filter { it.type == Game.NUMBERS_TYPE } as ArrayList, groupBy)

        val testView = findViewById<TextView>(R.id.test)
        testView.text = "${wordsGames.values.map { it.createdAt }.joinToString(", ")}"
//        testView.text = "${(games.filter { it.type == Game.WORDS_TYPE } as ArrayList).map { it.createdAt }.joinToString(", ")}"
//        testView.text = this.getFormattedDatePickerValue(this.startedFromDatePicker)

        findViewById<TextView>(R.id.words_percent_correct_header).text = "Words percent win"
        this.initCorrectAnswersPercentChart(R.id.words_percent_correct_chart, wordsGames)

        findViewById<TextView>(R.id.numbers_percent_correct_header).text = "Numbers percent win"
        this.initCorrectAnswersPercentChart(R.id.numbers_percent_correct_chart, numbersGames)
    }

    /**
     * Returns a date strings of grouped by date games values mapped to merged games data
     */
    private fun groupGames(games: ArrayList<Game>, groupBy: String): Map<String, Game>
    {
        val map: MutableMap<String, Game> = HashMap()

        for (game in games) {
            val period = when (groupBy) {
                GROUP_BY_YEAR -> game.createdAt.substring(0, 4)
                GROUP_BY_MONTH -> game.createdAt.substring(0, 7)
                GROUP_BY_DAY -> game.createdAt.substring(0, 10)
                else -> game.createdAt
            }

            game.createdAt = period
            if (map[period] != null) {
                map[period] = this.mergeGames(map[period]!!, game)
            }
            else {
                map[period] = game
            }
        }

        return map.toSortedMap()
    }

    private fun mergeGames(first: Game, second: Game): Game
    {
        first.correctAnswersCount += second.correctAnswersCount
        first.allAnswersCount += second.allAnswersCount

        return first
    }

    private fun initCorrectAnswersPercentChart(chartId: Int, games: Map<String, Game>): Unit
    {
        val entries: ArrayList<BarEntry> = ArrayList()
        for (entry in games.entries.iterator()) {
            val game = games[entry.key]!!
            entries.add(BarEntry(this.dateToFloat(entry.key), (
                100
                * game.correctAnswersCount.toFloat()
                / game.allAnswersCount.toFloat()))
            )
        }

        val barChart = findViewById<BarChart>(chartId)
        val winPercentMean = calculateCorrectAnswersPercentMean(games.values.toList())

        val dataSet = BarDataSet(entries, "Percent correct (${winPercentMean}% mean)")
        dataSet.color = Color.BLACK
        dataSet.valueTextSize = 0f

        val lineData = BarData(dataSet)
        val description = Description()
        description.text = ""
        barChart.setData(lineData)
        barChart.invalidate()
        barChart.description = description
        barChart.axisLeft.axisMinimum = 0f
        barChart.axisLeft.axisMaximum = 100f
        barChart.axisLeft.granularity = 25f
        barChart.axisLeft.textSize = this.textSize
        barChart.axisLeft.axisLineWidth = 2f
        barChart.axisLeft.xOffset = 10f
        barChart.axisRight.isEnabled = false
        barChart.xAxis.textSize = this.textSize
        barChart.xAxis.gridLineWidth = 0f
        barChart.xAxis.yOffset = 2f
        barChart.xAxis.granularity = 1f
        barChart.xAxis.setDrawLabels(true)
        barChart.xAxis.valueFormatter = DateValueFormatter()
        barChart.xAxis.spaceMax = 2f
        barChart.legend.textSize = this.textSize
        barChart.legend.formSize = this.textSize
        barChart.legend.yEntrySpace = this.textSize
    }

    private fun dateToFloat(date: String): Float
    {
        return date.replace("\\-|\\:|\\s".toRegex(), "").toFloat()
    }

    private fun calculateCorrectAnswersPercentMean(games: List<Game>): Float
    {
        if (games.isEmpty()) {
            return 0f
        }
        val mean = games
            .map { it.correctAnswersCount.toFloat() / it.allAnswersCount.toFloat() }
            .reduce { a, b -> a + b } / games.size

        return (mean * 1000).roundToInt().toFloat() / 10
    }
}


class DateValueFormatter : ValueFormatter()
{
    override fun getFormattedValue(value: Float): String
    {
        val v = value.toLong().toString()
        var result = ""

        if (v.length >= 4) {
            result += v.safeSubstring(0, 4)
        }
        if (v.length >= 6) {
            result += "-" + v.safeSubstring(4, 6)
        }
        if (v.length >= 8) {
            result += "-" + v.safeSubstring(6, 8)
        }
        if (v.length >= 10) {
            result += " " + v.safeSubstring(8, 10)
        }
        if (v.length >= 12) {
            result += ":" + v.safeSubstring(10, 12)
        }
        if (v.length >= 14) {
            result += ":" + v.safeSubstring(12, 14)
        }

        return result
    }
}