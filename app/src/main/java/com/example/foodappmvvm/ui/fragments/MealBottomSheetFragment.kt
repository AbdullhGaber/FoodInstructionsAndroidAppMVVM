package com.example.foodappmvvm.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodappmvvm.databinding.FragmentMealBottomSheetBinding
import com.example.foodappmvvm.model.data_class.Meal
import com.example.foodappmvvm.ui.activities.MainActivity
import com.example.foodappmvvm.ui.activities.MealActivity
import com.example.foodappmvvm.util.Constants
import com.example.foodappmvvm.view_model.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


private const val MEAL_ID = "meal_id"


class MealBottomSheetFragment : BottomSheetDialogFragment() {

    private var mMealId: String? = null
    private var mMealName: String? = null
    private var mMealThumb: String? = null

    private lateinit var mBinding : FragmentMealBottomSheetBinding
    private lateinit var mViewModel : HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mMealId = it.getString(MEAL_ID)
        }
        mViewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentMealBottomSheetBinding.inflate(inflater , container , false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel.getMealById(mMealId)

        observeMealById()

        onBottomSheetClick()

    }

    private fun onBottomSheetClick(){
         mBinding.root.setOnClickListener(){
             if(mMealName != null && mMealThumb != null){
                 val intent = Intent(activity , MealActivity::class.java)

                 intent.apply {
                     putExtra(Constants.MEAL_ID_TAG , mMealId)
                     putExtra(Constants.MEAL_THUMB_TAG , mMealThumb)
                     putExtra(Constants.MEAL_NAME_TAG , mMealName)
                 }

                 startActivity(intent)
             }
         }
    }

    private fun observeMealById() {
        mViewModel.observeMealById().observe(viewLifecycleOwner , Observer {
            bindUI(it)
            setMealsData(it)
        })
    }

    private fun setMealsData(meal: Meal?) {
        mMealName = meal?.strMeal
        mMealThumb = meal?.strMealThumb
    }

    private fun bindUI(meal : Meal) {
        Glide.with(mBinding.root).load(meal.strMealThumb).into(mBinding.bottomSheetIv)
        mBinding.bottomSheetAreaTv.text = meal.strArea
        mBinding.bottomSheetCategoriesTv.text = meal.strCategory
        mBinding.mealNameTv.text = meal.strMeal
    }

    companion object {
        @JvmStatic
        fun newInstance(mealId: String) =
            MealBottomSheetFragment().apply {
               arguments = Bundle().apply {
                    putString(MEAL_ID, mealId)
              }
          }
    }

}












