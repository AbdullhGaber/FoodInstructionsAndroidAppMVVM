package com.example.foodappmvvm.model.data_class


import com.google.gson.annotations.SerializedName

data class CategoryMealsResponse(
    @SerializedName("meals")
    val meals: List<CategoryMeals>
)