package com.example.foodappmvvm.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodappmvvm.model.data_class.CategoryMeals
import com.example.foodappmvvm.model.data_class.CategoryMealsResponse
import com.example.foodappmvvm.model.retrofit.BaseRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel : ViewModel(){

    private var mCategoryMealsLiveData = MutableLiveData<List<CategoryMeals>>()

    fun getMealByCategory(category : String?){
        BaseRetrofit.apiService.getMealByCategory(category).
        enqueue(object : Callback<CategoryMealsResponse>{
            override fun onResponse(
                call: Call<CategoryMealsResponse>,
                response: Response<CategoryMealsResponse>
            ) {
                if(response.body()!= null){
                    mCategoryMealsLiveData.postValue(response.body()!!.meals)
                }else
                    Log.v("TAG" , "body empty!")
            }

            override fun onFailure(call: Call<CategoryMealsResponse>, t: Throwable) {
                Log.v("TAG" , t.message.toString())
            }

        })
    }

    fun observeCategoryMeals() : LiveData<List<CategoryMeals>> {
        return mCategoryMealsLiveData
    }
}