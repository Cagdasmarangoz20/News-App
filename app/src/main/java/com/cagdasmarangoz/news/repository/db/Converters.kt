package com.cagdasmarangoz.news.repository.db

import androidx.room.TypeConverter
import com.cagdasmarangoz.news.model.Source


class Converters {
    @TypeConverter
    fun fromSource(source: Source):String? {
        return source.name
    }

    @TypeConverter
    fun toSource(name : String): Source {
        return Source(name, name)
    }
}