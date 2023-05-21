package com.example.foodappmvvm.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodappmvvm.databinding.ActivityCategoryMealsBinding
import com.example.foodappmvvm.model.data_class.CategoryMeals
import com.example.foodappmvvm.ui.adapters.CategoryMealsAdapter
import com.example.foodappmvvm.util.Constants
import com.example.foodappmvvm.view_model.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityCategoryMealsBinding
    private lateinit var mViewModel : CategoryMealsViewModel
    private var mMealCategory : String? = null
    private lateinit var mCategoryMealsAdapter: CategoryMealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        getDataFromIntent()
        mCategoryMealsAdapter = CategoryMealsAdapter()
        prepareCategoryMealsRV()
        mViewModel = CategoryMealsViewModel()


        getCategoryMeals(mMealCategory)
        observeCategoryMeals()

    }

    private fun onCategoryItemClick(){
        mCategoryMealsAdapter.setOnItemClickListener {
            val intent = Intent(this , MealActivity::class.java)

            intent.putExtra(Constants.MEAL_ID_TAG, it.idMeal )
            intent.putExtra(Constants.MEAL_NAME_TAG, it.strMeal )
            intent.putExtra(Constants.MEAL_THUMB_TAG, it.strMealThumb )

            startActivity(intent)
        }
    }

    private fun prepareCategoryMealsRV(){
        mBinding.categoryMealRv.apply {
            layoutManager = GridLayoutManager(context , 2 ,GridLayoutManager.VERTICAL, false)
            adapter = mCategoryMealsAdapter
        }

        onCategoryItemClick()
    }
    private fun getDataFromIntent() {
        val incomingIntent = intent
        mMealCategory = incomingIntent.getStringExtra(Constants.MEAL_NAME_TAG)!!
    }

    private fun getCategoryMeals(category : String?) {
        mViewModel.getMealByCategory(category)
    }

    private fun observeCategoryMeals() {
        mViewModel.observeCategoryMeals().observe(this , Observer {
            mCategoryMealsAdapter.setCategoryMeals(it as ArrayList<CategoryMeals>)
            mBinding.categoryCountTv.text =  it.size.toString() + " meals of "+ mMealCategory +" category"
        })
    }
}