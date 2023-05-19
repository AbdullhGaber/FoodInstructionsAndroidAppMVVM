package com.example.foodappmvvm.util

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConverter {

    @TypeConverter
    fun fromAnyToString(data : Any?) : String{
        if (data == null) return "null"
        return data.toString()
    }

    @TypeConverter
    fun fromStringToAny(data : String?) : Any{
        if (data == null) return "null"
        return data
    }
}