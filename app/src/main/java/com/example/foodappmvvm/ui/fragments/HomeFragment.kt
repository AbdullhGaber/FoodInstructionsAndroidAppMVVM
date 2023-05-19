package com.example.foodappmvvm.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodappmvvm.R
import com.example.foodappmvvm.databinding.FragmentHomeBinding
import com.example.foodappmvvm.model.data_class.Category
import com.example.foodappmvvm.model.data_class.CategoryMeals
import com.example.foodappmvvm.model.data_class.Meal
import com.example.foodappmvvm.ui.activities.CategoryMealsActivity
import com.example.foodappmvvm.ui.activities.MainActivity
import com.example.foodappmvvm.ui.adapters.PopularItemsAdapter
import com.example.foodappmvvm.ui.activities.MealActivity
import com.example.foodappmvvm.ui.adapters.CategoryItemAdapter
import com.example.foodappmvvm.util.Constants
import com.example.foodappmvvm.view_model.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var mBinding: FragmentHomeBinding
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mMeal: Meal
    private lateinit var mPopularItemsAdapter: PopularItemsAdapter
    private lateinit var mCategoryItemAdapter: CategoryItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = (activity as MainActivity).viewModel
        mPopularItemsAdapter = PopularItemsAdapter()
        mCategoryItemAdapter = CategoryItemAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentHomeBinding.inflate(inflater , container , false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.getRandomMeal()
        observeRandomMeal()

        onRandomMealImageClick()
        onPopularItemClick()
        onPopularItemLongClick()

        preparePopularRecView()
        mViewModel.getPopularItems()
        observeCategoryMeals()

        prepareCategoriesRecView()
        mViewModel.getCategories()
        observeCategories()

        onCategoryItemClick()

        onSearchIconClick()
    }

    private fun onSearchIconClick() {
        mBinding.searchIconIv.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun observeCategories(){
        mViewModel.observeCategories().observe(viewLifecycleOwner , Observer {
            mCategoryItemAdapter.setCategoryList(it as ArrayList<Category>)
        })
    }

    private fun prepareCategoriesRecView(){
        mBinding.categoriesRv.apply {
            layoutManager = GridLayoutManager(activity ,3, LinearLayoutManager.VERTICAL , false )
            adapter = mCategoryItemAdapter
        }
    }

    private fun onCategoryItemClick(){
        mCategoryItemAdapter.setViewHolderOnClickListener {
            val intent = Intent(activity , CategoryMealsActivity::class.java)

            intent.putExtra(Constants.MEAL_NAME_TAG, it.strCategory )

            startActivity(intent)
        }
    }
    private fun onRandomMealImageClick() {
        mBinding.randomMealIv.setOnClickListener{
            val intent = Intent(activity , MealActivity::class.java)

            intent.putExtra(Constants.MEAL_ID_TAG,mMeal.idMeal)
            intent.putExtra(Constants.MEAL_NAME_TAG,mMeal.strMeal)
            intent.putExtra(Constants.MEAL_THUMB_TAG,mMeal.strMealThumb)

            startActivity(intent)
        }
    }

    private fun observeRandomMeal(){
        mViewModel.observeRandomMeal().observe(viewLifecycleOwner , Observer{
            Glide.with(this@HomeFragment).load(it.strMealThumb).into(mBinding.randomMealIv)
            mMeal = it
        })
    }

    private fun preparePopularRecView(){
        mBinding.mealRv.apply {
            layoutManager = LinearLayoutManager(activity , LinearLayoutManager.HORIZONTAL , false )
            adapter = mPopularItemsAdapter
        }
    }


    private fun onPopularItemClick(){
        mPopularItemsAdapter.setViewHolderOnClickListener {
            val intent = Intent(activity , MealActivity::class.java)

            intent.putExtra(Constants.MEAL_ID_TAG , it.idMeal)
            intent.putExtra(Constants.MEAL_THUMB_TAG , it.strMealThumb)
            intent.putExtra(Constants.MEAL_NAME_TAG , it.strMeal)

            startActivity(intent)
        }
    }

    private fun onPopularItemLongClick(){
        mPopularItemsAdapter.setViewHolderOnLongClickListener {
            val bottomSheetFragment = MealBottomSheetFragment.newInstance(it.idMeal)
            bottomSheetFragment.show(childFragmentManager , "Meal Info")
        }
    }

    private fun observeCategoryMeals(){
        mViewModel.observeCategoryMeals().observe(viewLifecycleOwner , Observer{
            mPopularItemsAdapter.setMeals(it as ArrayList<CategoryMeals>)
        })
    }
}