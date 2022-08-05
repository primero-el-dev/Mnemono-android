package com.primeroeldev.mnemono.activity

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend.LegendVerticalAlignment
import com.github.mikephil.charting.components.XAxis
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
    private val TEXT_SIZE = 18f
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
        val groupBy = this.groupByDateSpinner.selectedItem.toString()

        // Get needed games data
        val where: ArrayList<Pair<String, Any?>> = ArrayList()
        where.add(Pair("createdAt >= ?", startedFrom))
        where.add(Pair("createdAt <= ?", startedTo))
        where.add(Pair("includedInStatistics = ?", 1))
        where.add(Pair("status = ?", Game.FINISHED_STATUS))

        val games = this.gameRepository
            .findBy(where)
            .map { it as Game } as ArrayList

        val wordsGames = this.groupGames(games.filter { it.type == Game.WORDS_TYPE } as ArrayList, groupBy)
        val numbersGames = this.groupGames(games.filter { it.type == Game.NUMBERS_TYPE } as ArrayList, groupBy)

        // Init charts
        findViewById<TextView>(R.id.words_percent_correct_header).text = "Words games percent win"
        this.initCorrectAnswersPercentChart(
            R.id.words_percent_correct_chart,
            wordsGames
        )

        findViewById<TextView>(R.id.numbers_percent_correct_header).text = "Numbers games percent win"
        this.initCorrectAnswersPercentChart(
            R.id.numbers_percent_correct_chart,
            numbersGames
        )

        findViewById<TextView>(R.id.words_answer_period_header).text = "Time per answer in words game"
        this.initCorrectAnswersTimePeriodChart(
            R.id.words_answer_period_chart,
            wordsGames
        )

        findViewById<TextView>(R.id.numbers_answer_period_header).text = "Time per answer in numbers game"
        this.initCorrectAnswersTimePeriodChart(
            R.id.numbers_answer_period_chart,
            numbersGames
        )
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
        first.durationInSeconds += second.durationInSeconds

        return first
    }

    private fun initCorrectAnswersPercentChart(chartId: Int, games: Map<String, Game>): Unit
    {
        val entries = this.createEntries(
            games,
            { game: Game -> 100 * game.correctAnswersCount.toFloat() / game.allAnswersCount.toFloat() }
        )

        val lineChart = findViewById<LineChart>(chartId)
        val winPercentMean = calculateCorrectAnswersPercentMean(games.values.toList())

        val dataSet = LineDataSet(entries, "Percent correct (${winPercentMean}% mean)")

        this.initLineChart(lineChart, dataSet)
    }

    private fun initCorrectAnswersTimePeriodChart(chartId: Int, games: Map<String, Game>): Unit
    {
        val entries = this.createEntries(
            games,
            { game: Game -> game.correctAnswersCount.toFloat() / game.durationInSeconds.toFloat() }
        )

        val lineChart = findViewById<LineChart>(chartId)
        val winPercentMean = calculateMeanTimePerCorrectAnswers(games.values.toList())

        val dataSet = LineDataSet(entries, "Time per correct answer in seconds (${winPercentMean}s mean)")

        this.initLineChart(lineChart, dataSet)
    }

    private fun createEntries(games: Map<String, Game>, lambda: (Game) -> Float): ArrayList<Entry>
    {
        val entries: ArrayList<Entry> = ArrayList()
        for (entry in games.entries.iterator()) {
            val game = games[entry.key]!!
            entries.add(Entry(
                this.dateToFloat(entry.key),
                lambda(game)
            ))
        }

        return entries
    }

    private fun initLineChart(lineChart: LineChart, dataSet: LineDataSet): Unit
    {
        dataSet.lineWidth = 6f
        dataSet.circleRadius = 8f

        val color = resources.getColor(
            com.google.android.material.R.color.design_default_color_primary,
            theme
        )
        dataSet.color = color
        dataSet.valueTextSize = 0f

        val lineData = LineData(dataSet)
        val description = Description()
        description.text = ""
        lineChart.description = description
        lineChart.data = lineData
        lineChart.invalidate()
        lineChart.setVisibleXRangeMinimum(4f)
        lineChart.setVisibleXRangeMaximum(5f)

        lineChart.axisLeft.axisMinimum = 0f
        lineChart.axisLeft.axisMaximum = 100f
        lineChart.axisLeft.granularity = 25f
        lineChart.axisLeft.textSize = this.TEXT_SIZE
        lineChart.axisLeft.axisLineWidth = 2f
        lineChart.axisLeft.xOffset = 10f
        lineChart.axisLeft.zeroLineWidth = 4f
        lineChart.axisLeft.gridLineWidth = 1f
        lineChart.axisRight.isEnabled = false

        lineChart.xAxis.textSize = this.TEXT_SIZE
        lineChart.xAxis.gridLineWidth = 0f
        lineChart.xAxis.yOffset = 10f
        lineChart.xAxis.granularity = 1f
        lineChart.xAxis.setDrawLabels(true)
        lineChart.xAxis.valueFormatter = DateValueFormatter()
        lineChart.xAxis.spaceMin = 0f
        lineChart.xAxis.spaceMax = 0f
        lineChart.xAxis.setDrawAxisLine(true)
        lineChart.xAxis.labelRotationAngle = -20f
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.extraBottomOffset = 40f

        lineChart.legend.textSize = this.TEXT_SIZE
        lineChart.legend.formSize = this.TEXT_SIZE
        lineChart.legend.yEntrySpace = this.TEXT_SIZE
        lineChart.legend.verticalAlignment = LegendVerticalAlignment.TOP
        lineChart.legend.yOffset = 15f
    }

    private fun dateToFloat(date: String): Float
    {
        return date.replace("\\-|\\:|\\s".toRegex(), "").safeSubstring(2, 8).toFloat()
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

    private fun calculateMeanTimePerCorrectAnswers(games: List<Game>): Float
    {
        if (games.isEmpty()) {
            return 0f
        }

        return games
            .map { it.correctAnswersCount.toFloat() / it.durationInSeconds.toFloat() }
            .reduce { a, b -> a + b } / games.size
    }
}


class DateValueFormatter : ValueFormatter()
{
    override fun getFormattedValue(value: Float): String
    {
        val v = value.toLong().toString()
        var result = ""

        if (v.length >= 2) {
            result += "20" + v.safeSubstring(0, 2)
        }
        if (v.length >= 4) {
            result += "-" + v.safeSubstring(2, 4)
        }
        if (v.length >= 6) {
            result += "-" + v.safeSubstring(4, 6)
        }

        return result
    }
}