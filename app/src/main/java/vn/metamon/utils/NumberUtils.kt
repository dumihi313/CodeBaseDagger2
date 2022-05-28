package vn.metamon.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

object NumberUtils {
    fun Int.standardFormat(): String {
        return this.toLong().standardFormat()
    }

    fun Long.standardFormat(): String {
        return getDecimalFormat().format(this)
    }

    private fun getDecimalFormat(): DecimalFormat {
        val df = DecimalFormat("###,###.#")
        val decimalFormatSymbols = DecimalFormatSymbols()
        decimalFormatSymbols.decimalSeparator = '.'

        df.decimalFormatSymbols = decimalFormatSymbols
        df.roundingMode = RoundingMode.FLOOR
        return df
    }
}