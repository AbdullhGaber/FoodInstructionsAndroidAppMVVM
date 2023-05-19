package com.example.foodappmvvm.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappmvvm.model.data_class.Meal
import com.example.foodappmvvm.model.data_class.MealResponse
import com.example.foodappmvvm.model.retrofit.BaseRetrofit
import com.example.foodappmvvm.model.room_db.MealRoomDB
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealViewModel(
    private val mRoomDB: MealRoomDB
) : ViewModel(){

    private var mealLiveData = MutableLiveData<Meal>()

    fun getAPIMealById(id : String?){
        BaseRetrofit.apiService.getMealById(id).enqueue(object : Callback<MealResponse> {

            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if(response.body() != null){
                    val meal : Meal = response.body()!!.meals[0]
                    mealLiveData.postValue(meal)
                    Log.v("TAG" , "Load done")
                }else{
                    Log.v("TAG" , "body empty!")
                }

            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                Log.v("TAG" , t.message.toString())
            }

        })
    }

    fun observeMealById() : LiveData<Meal> {
        return mealLiveData
    }

    fun upsertMeal(meal : Meal?){
       viewModelScope.launch {
           mRoomDB.mealDao().upsert(meal)
       }
    }

    fun deleteMeal(meal : Meal?){
        viewModelScope.launch {
            mRoomDB.mealDao().deleteMeal(meal)
        }
    }

    fun getDatabaseMealById(id : String) : LiveData<Meal?>{
        return mRoomDB.mealDao().getMealById(id)
    }
}