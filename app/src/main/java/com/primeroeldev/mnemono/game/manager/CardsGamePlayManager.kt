package com.primeroeldev.mnemono.game.manager

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import com.primeroeldev.mnemono.R
import com.primeroeldev.mnemono.general.CardAssetManager
import com.primeroeldev.mnemono.general.safeSubstring
import com.primeroeldev.mnemono.general.toInt
import kotlin.random.Random


class CardsGamePlayManager(private val resources: Resources) : GamePlayManager
{
    private val random = Random(System.currentTimeMillis())
    private val bitmap = BitmapFactory.decodeResource(resources, R.drawable.cards)

    companion object
    {
        const val CARD_WIDTH = 190*2.75
        const val CARD_HEIGHT = 270*2.75
    }

    override fun generateAnswers(count: Int): String
    {
        val cards: ArrayList<String> = ArrayList()
        var possible: ArrayList<String> = ArrayList()
        var i = 0
        while (i < count) {
            if (i % 52 == 0) {
                possible = this.getAllPossible()
            }
            val index = random.nextInt(possible.size)
            cards.add(possible[index])
            possible.removeAt(index)
            i++
        }

        return cards.joinToString(",")
    }

    override fun presentAnswersImage(answers: String): Bitmap
    {
        val cards = answers.split(",")
        val count = cards.size

        val mainBitmap = Bitmap.createBitmap(
            CARD_WIDTH.toInt() * count,
            CARD_HEIGHT.toInt(),
            Bitmap.Config.RGB_565
        )
        val canvas = Canvas(mainBitmap)

        var i = 0
        while (i < count) {
            val card = this.getCard(cards[i])
            canvas.drawBitmap(card, i * CardAssetManager.CARD_WIDTH.toFloat(), 0f, Paint())
            i++
        }

        return mainBitmap
    }

    override fun checkAnswers(correct: String, provided: String): Pair<String, Int>
    {
        val providedCards = provided.replace("(\\s)+".toRegex(), "").split(",")
        val correctCards = correct.replace("(\\s)+".toRegex(), "").split(",")
        var answers = ""
        var correctCount = 0
        var index = 0
        val limit = minOf(providedCards.size, correctCards.size)

        while (index < limit) {
            val providedCard = providedCards[index]
            val isCorrect = providedCard == correctCards[index]
            if (isCorrect) {
                answers += "<font color=\"black\">$providedCard</font>,\n"
            }
            else {
                answers += "<font color=\"red\">$providedCard</font>,\n"
            }
            correctCount += isCorrect.toInt()
            index++
        }

        return Pair(answers, correctCount)
    }

    override fun getAnswerInputHint(): String
    {
        return "Write cards divided by commas in poker hand range notation. " +
            "All white chars will be trimmed."
    }

    override fun getInputType(): String
    {
        return GamePlayManager.IMAGE_INPUT_TYPE
    }

    private fun getAllPossible(): ArrayList<String>
    {
        val suits = arrayOf("s", "c", "h", "d")
        val values = arrayOf("A", "1", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K")
        val result: ArrayList<String> = ArrayList()

        for (value in values) {
            for (suit in suits) {
                result.add(value + suit)
            }
        }

        return result
    }

    private fun getCard(card: String): Bitmap
    {
        val number = card.safeSubstring(0, 1)
        val suit = card.safeSubstring(1, 2)

        val x = this.getXOffset(number)
        val y = this.getYOffset(suit)

        return Bitmap.createBitmap(
            this.bitmap,
            x,
            y,
            CardAssetManager.CARD_WIDTH.toInt(),
            CardAssetManager.CARD_HEIGHT.toInt()
        )
    }

    private fun getXOffset(number: String): Int
    {
        val xOffset = when (number) {
            "2" -> 0
            "3" -> 1
            "4" -> 2
            "5" -> 3
            "6" -> 4
            "7" -> 5
            "8" -> 6
            "9" -> 7
            "T" -> 8
            "J" -> 9
            "Q" -> 10
            "K" -> 11
            "A" -> 12
            else -> 0
        }

        return xOffset * CardAssetManager.CARD_WIDTH.toInt()
    }

    private fun getYOffset(suit: String): Int
    {
        val yOffset = when (suit) {
            "s" -> 0
            "c" -> 1
            "h" -> 2
            "d" -> 3
            else -> 0
        }

        return yOffset * CardAssetManager.CARD_HEIGHT.toInt()
    }
}