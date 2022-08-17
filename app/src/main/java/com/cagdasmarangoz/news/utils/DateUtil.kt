package com.cagdasmarangoz.news.utils

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import java.text.ParseException
import java.util.*

@SuppressLint("SimpleDateFormat")
object DateUtil {

    fun getDateFromStandardFormat(
        dateString:String,
        format: String = Constants.SERVER_DATE_FORMAT_TYPE
    ): Date? {
        val sdf = SimpleDateFormat(format)
        return try {
            sdf.parse(dateString)
        } catch (e: ParseException) {
            null
        }
    }

    fun dateToFormattedString(date:Date, format:String): String {
        val sdf = SimpleDateFormat(format)
        return try {
            sdf.format(date)
        } catch (e:Exception){
            ""
        }
    }
}