package com.primeroeldev.mnemono.general

import android.graphics.Bitmap
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*


class CardAssetManager(private val bitmap: Bitmap)
{
    private val random = Random(System.currentTimeMillis())

    companion object
    {
        const val CARD_WIDTH = 190*2.75
        const val CARD_HEIGHT = 270*2.75
    }

    fun getRandomCardSymbol(): String
    {
        val suits = arrayOf("s", "c", "h", "d")
        val values = arrayOf("A", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")

        return suits[random.nextInt(4)] + values[random.nextInt(13)]
    }

    fun getCard(card: String): Bitmap
    {
        val suit = card.safeSubstring(0, 1)
        val number = card.safeSubstring(1, 3)

        val x = this.getXOffset(number)
        val y = this.getYOffset(suit)

        return Bitmap.createBitmap(this.bitmap, x, y, CARD_WIDTH.toInt(), CARD_HEIGHT.toInt())
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
            "10" -> 8
            "J" -> 9
            "Q" -> 10
            "K" -> 11
            "A" -> 12
            else -> 0
        }

        return xOffset * CARD_WIDTH.toInt()
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

        return yOffset * CARD_HEIGHT.toInt()
    }
}