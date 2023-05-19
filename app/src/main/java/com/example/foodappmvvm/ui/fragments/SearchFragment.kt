package com.example.foodappmvvm.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodappmvvm.R
import com.example.foodappmvvm.databinding.FragmentSearchBinding
import com.example.foodappmvvm.ui.activities.MainActivity
import com.example.foodappmvvm.ui.activities.MealActivity
import com.example.foodappmvvm.ui.adapters.MealsAdapter
import com.example.foodappmvvm.util.Constants
import com.example.foodappmvvm.view_model.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchFragment : Fragment() {

    lateinit var mBinding : FragmentSearchBinding
    lateinit var mMealsAdapter : MealsAdapter
    lateinit var mViewModel : HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = FragmentSearchBinding.inflate(inflater , container , false)

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareMealsRV()

        onSearchClick()

        onTextTyping()

        onSearchedMealClick()

        observeSearchedMeals()
    }

    private fun onTextTyping() {
        var searchJob : Job ? = null

        mBinding.searchEt.addTextChangedListener {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                mViewModel.searchMeals(it.toString())
            }
        }
    }

    private fun onSearchedMealClick() {
        mMealsAdapter.setOnViewHolderClickListener {
            val intent = Intent(activity , MealActivity::class.java)

            intent.putExtra(Constants.MEAL_ID_TAG , it.idMeal)
            intent.putExtra(Constants.MEAL_THUMB_TAG , it.strMealThumb)
            intent.putExtra(Constants.MEAL_NAME_TAG , it.strMeal)

            startActivity(intent)
        }
    }


    private fun observeSearchedMeals() {
        mViewModel.observeSearchedMeal().observe(viewLifecycleOwner , Observer {
            mMealsAdapter.differ.submitList(it)
        })
    }

    private fun onSearchClick() {
        mBinding.searchArrowIv.setOnClickListener{
            searchMeals()
        }
    }

    private fun searchMeals() {
        val searchQuery : String = mBinding.searchEt.text.toString()

        if(searchQuery.isNotEmpty()) mViewModel.searchMeals(searchQuery)
    }

    private fun prepareMealsRV() {
        mMealsAdapter = MealsAdapter()

        mBinding.rvSearchMeals.apply {
            layoutManager = GridLayoutManager(context , 2 , GridLayoutManager.VERTICAL , false)
            adapter = mMealsAdapter
        }

    }

}