package com.example.foodappmvvm.model.retrofit

import androidx.lifecycle.LiveData
import com.example.foodappmvvm.model.data_class.CategoryMealsResponse
import com.example.foodappmvvm.model.data_class.CategoryResponse
import com.example.foodappmvvm.model.data_class.MealResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApiService {
    @GET("random.php")
    fun getRandomMeal() : Call<MealResponse>

    @GET("lookup.php?")
    fun getMealById(@Query("i") id : String?) : Call<MealResponse>

    @GET("filter.php?")
    fun getMealByCategory(@Query("c") category : String?) : Call<CategoryMealsResponse>

    @GET("categories.php")
    fun getCategories() : Call<CategoryResponse>

    @GET("search.php")
    fun searchMeals(@Query("s") searchedMeal : String) : Call<MealResponse>

}