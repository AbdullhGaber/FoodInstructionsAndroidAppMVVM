package com.example.foodappmvvm.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.foodappmvvm.databinding.FragmentCategoriesBinding
import com.example.foodappmvvm.databinding.FragmentFavoritesBinding
import com.example.foodappmvvm.model.data_class.Category
import com.example.foodappmvvm.ui.activities.CategoryMealsActivity
import com.example.foodappmvvm.ui.activities.MainActivity
import com.example.foodappmvvm.ui.adapters.CategoryItemAdapter
import com.example.foodappmvvm.ui.adapters.CategoryMealsAdapter
import com.example.foodappmvvm.util.Constants
import com.example.foodappmvvm.view_model.HomeViewModel

class CategoriesFragment : Fragment() {
    private lateinit var mBinding: FragmentCategoriesBinding
    private lateinit var mViewModel: HomeViewModel
    private lateinit var mCategoryItemAdapter: CategoryItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = (activity as MainActivity).viewModel
        mCategoryItemAdapter = CategoryItemAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentCategoriesBinding.inflate(inflater , container , false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareCategoriesRV()
        onCategoryItemClick()
        observeCategories()
    }

    private fun onCategoryItemClick() {
        mCategoryItemAdapter.setViewHolderOnClickListener {
            val intent = Intent(activity , CategoryMealsActivity::class.java)

            intent.putExtra(Constants.MEAL_NAME_TAG, it.strCategory )

            startActivity(intent)
        }
    }

    private fun observeCategories() {
        mViewModel.observeCategories().observe(requireActivity() , Observer {
            mCategoryItemAdapter.setCategoryList(it as ArrayList<Category>)
        })
    }

    private fun prepareCategoriesRV() {
        mBinding.categoriesRv.apply {
            adapter = mCategoryItemAdapter
            layoutManager = GridLayoutManager(context , 3 , GridLayoutManager.VERTICAL , false)
        }
    }
}