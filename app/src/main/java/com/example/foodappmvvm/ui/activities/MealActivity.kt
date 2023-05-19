package com.example.foodappmvvm.ui.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodappmvvm.R
import com.example.foodappmvvm.databinding.ActivityMealBinding
import com.example.foodappmvvm.model.data_class.Meal
import com.example.foodappmvvm.model.room_db.MealRoomDB
import com.example.foodappmvvm.util.Constants
import com.example.foodappmvvm.view_model.MealViewModel
import com.example.foodappmvvm.view_model.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityMealBinding
    private var mMealName : String? = null
    private var mMealThumb : String? = null
    private var mMealId : String = "-1"
    private var mMealYoutube : String? = null
    private var mObservedMeal: Meal? = null
    private var mIsFavoriteIcon = false
    private lateinit var mViewModel: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mViewModel = ViewModelProvider(this,
            MealViewModelFactory(MealRoomDB.getInstance(this)))[MealViewModel::class.java]

        getMealDataFromIntent()

        bindUI()

        showProgressbar()

        mViewModel.getAPIMealById(mMealId)
        observeMeal()

        onYoutubeImgClick()

        onFavoriteButtonClick()

    }

    private fun setFavoriteIcon(apiMeal: Meal) {
        mViewModel.getDatabaseMealById(mMealId).observe(this) {
            dbMeal ->
            if (dbMeal?.idMeal == apiMeal.idMeal) {
                toggleFavoriteIcons()
            }
        }
    }



    private fun toggleFavoriteIcons(){
       if(mIsFavoriteIcon) {
           mBinding.floatBtnAddToFavorite.setImageDrawable(
               AppCompatResources.getDrawable(
                   this,
                   R.drawable.ic_favorite
               )
           )
           mIsFavoriteIcon = false
       }else{
           mBinding.floatBtnAddToFavorite.setImageDrawable(
               AppCompatResources.getDrawable(
                   this,
                   R.drawable.ic_filled_favorite
               )
           )
           mIsFavoriteIcon = true
       }

    }

    private fun onFavoriteButtonClick(){
        mBinding.floatBtnAddToFavorite.setOnClickListener {
            toggleFavoriteIcons()

            if(!mIsFavoriteIcon){
                mObservedMeal?.let {
                    mViewModel.deleteMeal(mObservedMeal)
                    Toast.makeText(this, "Meal removed from favorite", Toast.LENGTH_SHORT).show()
                }
            }else{
                mObservedMeal?.let {
                    mViewModel.upsertMeal(mObservedMeal)
                    Toast.makeText(this, "Meal saved to favorites", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun onYoutubeImgClick() {
        mBinding.youtubeImg.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW , Uri.parse(mMealYoutube)))
        }
    }

    private fun hideProgressbar(){
        mBinding.progressBar.visibility = View.INVISIBLE
        mBinding.floatBtnAddToFavorite.visibility = View.VISIBLE
        mBinding.areaTv.visibility = View.VISIBLE
        mBinding.categoryTv.visibility = View.VISIBLE
        mBinding.descriptionTv.visibility = View.VISIBLE
        mBinding.youtubeImg.visibility = View.VISIBLE
    }

    private fun showProgressbar(){
        mBinding.progressBar.visibility = View.VISIBLE
        mBinding.floatBtnAddToFavorite.visibility = View.INVISIBLE
        mBinding.areaTv.visibility = View.INVISIBLE
        mBinding.categoryTv.visibility = View.INVISIBLE
        mBinding.descriptionTv.visibility = View.INVISIBLE
        mBinding.youtubeImg.visibility = View.INVISIBLE
    }
    private fun observeMeal() {
        mViewModel.observeMealById().observe(this) {
            hideProgressbar()
            mBinding.categoryTv.text = "CategoryMealsResponse : ${it.strCategory}"
            mBinding.areaTv.text = "Area : ${it.strArea}"
            mBinding.descriptionTv.text = it.strInstructions
            mMealYoutube = it.strYoutube
            setFavoriteIcon(it)
            mObservedMeal = it
        }
    }

    private fun bindUI() {
        Glide.with(this)
            .load(mMealThumb)
            .into(mBinding.mealDetailIv)

        mBinding.collapseToolbarMeal.title = mMealName
        mBinding.collapseToolbarMeal.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        mBinding.collapseToolbarMeal.setExpandedTitleColor(resources.getColor(R.color.white))


    }

    private fun getMealDataFromIntent() {
        val mealDataIntent = intent

        mMealId = mealDataIntent.getStringExtra(Constants.MEAL_ID_TAG)!!
        mMealName = mealDataIntent.getStringExtra(Constants.MEAL_NAME_TAG)!!
        mMealThumb = mealDataIntent.getStringExtra(Constants.MEAL_THUMB_TAG)!!
    }
}