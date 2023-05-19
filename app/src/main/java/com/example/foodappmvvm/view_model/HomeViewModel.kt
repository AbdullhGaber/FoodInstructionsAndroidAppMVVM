package com.example.foodappmvvm.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodappmvvm.model.data_class.*
import com.example.foodappmvvm.model.retrofit.BaseRetrofit
import com.example.foodappmvvm.model.room_db.MealRoomDB
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private val mMealDatabase : MealRoomDB
) : ViewModel() {
    private var mRandomMealLiveData = MutableLiveData<Meal>()
    private var mMealByCategoryLiveData = MutableLiveData<List<CategoryMeals>>()
    private var mCategoriesLiveData = MutableLiveData<List<Category>>()
    private var mBottomFragmentMealsLiveData = MutableLiveData<Meal>()
    private var mSearchMealLiveData = MutableLiveData<List<Meal>>()
    private var mFavoriteMeals = mMealDatabase.mealDao().getMeals()
    private var mSaveMealState : Meal? = null

    fun getRandomMeal(){
        mSaveMealState?.let {
            mRandomMealLiveData.postValue(it)
            return
        }

        BaseRetrofit.apiService.getRandomMeal().enqueue(object : Callback<MealResponse> {

            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if(response.body() != null){
                    val meal : Meal = response.body()!!.meals[0]
                    mRandomMealLiveData.postValue(meal)
                    mSaveMealState = meal
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

    fun observeRandomMeal() : LiveData<Meal>{
        return mRandomMealLiveData
    }

    fun searchMeals(searchedMeals : String){
        BaseRetrofit.apiService.searchMeals(searchedMeals).enqueue(object : Callback<MealResponse>{
            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if(response.body() != null){
                    val meals : List<Meal> = response.body()!!.meals
                    mSearchMealLiveData.postValue(meals)
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

    fun observeSearchedMeal() : LiveData<List<Meal>> = mSearchMealLiveData

    fun getPopularItems(){
        BaseRetrofit.apiService.getMealByCategory("seaFood").
        enqueue(object : Callback<CategoryMealsResponse>{
            override fun onResponse(
                call: Call<CategoryMealsResponse>,
                response: Response<CategoryMealsResponse>
            ) {
                if(response.body()!= null){
                    mMealByCategoryLiveData.postValue(response.body()!!.meals)
                }else
                    Log.v("TAG" , "body empty!")
            }

            override fun onFailure(call: Call<CategoryMealsResponse>, t: Throwable) {
                Log.v("TAG" , t.message.toString())
            }

        })
    }

    fun observeCategoryMeals() : LiveData<List<CategoryMeals>>{
        return mMealByCategoryLiveData
    }

    fun getCategories(){
        BaseRetrofit.
        apiService.
        getCategories().
        enqueue(object : Callback<CategoryResponse>{

            override fun onResponse(
                call: Call<CategoryResponse>,
                response: Response<CategoryResponse>
            ) {
                if(response.body() != null){
                    response.body()?.let {
                        mCategoriesLiveData.postValue(it.categories)
                    }
                }else
                    Log.v("TAG" , "body empty!")
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.v("TAG" , t.message.toString())
            }
        })
    }

    fun observeCategories() : LiveData<List<Category>>{
        return mCategoriesLiveData
    }

    fun getMealById(id : String?){
        BaseRetrofit.apiService.getMealById(id).enqueue(object : Callback<MealResponse>{
            override fun onResponse(call: Call<MealResponse>, response: Response<MealResponse>) {
                if(response.body() != null){
                    response.body()?.let {
                        mBottomFragmentMealsLiveData.postValue(it.meals.first())
                    }
                }else
                Log.v("TAG" , "body empty!")
            }

            override fun onFailure(call: Call<MealResponse>, t: Throwable) {
                Log.v("TAG" , t.message.toString())
            }

        })
    }

    fun observeMealById() : LiveData<Meal> = mBottomFragmentMealsLiveData

    fun observeFavoriteMeals() : LiveData<List<Meal?>>{
        return mFavoriteMeals
    }

    fun upsertMeal(meal : Meal?){
        viewModelScope.launch {
            mMealDatabase.mealDao().upsert(meal)
        }
    }

    fun deleteMeal(meal : Meal){
        viewModelScope.launch {
            mMealDatabase.mealDao().deleteMeal(meal)
        }
    }

}