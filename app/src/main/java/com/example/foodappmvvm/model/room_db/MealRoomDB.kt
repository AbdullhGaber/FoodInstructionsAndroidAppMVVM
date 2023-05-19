package com.example.foodappmvvm.model.room_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foodappmvvm.model.data_class.Meal
import com.example.foodappmvvm.model.room_db.DAO.MealDao
import com.example.foodappmvvm.util.MealTypeConverter

@Database(entities = [Meal::class] , version = 1 )
@TypeConverters(MealTypeConverter::class)
abstract class MealRoomDB : RoomDatabase() {
    abstract fun mealDao() : MealDao

    companion object{
        @Volatile
        private var mInstance : MealRoomDB? = null

        @Synchronized
        fun getInstance(context: Context) : MealRoomDB{
            if (mInstance == null){
                mInstance = Room.databaseBuilder(
                    context ,
                    MealRoomDB::class.java,
                    "meals.db"
                ).fallbackToDestructiveMigration().build()
            }

            return mInstance as MealRoomDB
        }
    }
}