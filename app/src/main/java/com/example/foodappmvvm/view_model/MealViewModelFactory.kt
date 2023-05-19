package com.example.foodappmvvm.view_model



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodappmvvm.model.room_db.MealRoomDB

class MealViewModelFactory(
    private val mealRoomDB: MealRoomDB
) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MealViewModel(mealRoomDB) as T
    }
}