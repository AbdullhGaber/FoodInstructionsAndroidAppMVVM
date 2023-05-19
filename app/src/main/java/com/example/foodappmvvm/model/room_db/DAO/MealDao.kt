package com.example.foodappmvvm.model.room_db.DAO

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.foodappmvvm.model.data_class.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE )
    suspend fun upsert(meal : Meal?)

    @Delete
    suspend fun deleteMeal(meal : Meal?)

    @Query("SELECT * FROM meals")
    fun getMeals() : LiveData<List<Meal?>>

    @Query("SELECT * FROM meals WHERE idMeal = :id")
    fun getMealById(id : String) : LiveData<Meal?>
}