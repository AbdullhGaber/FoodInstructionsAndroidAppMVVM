package com.example.foodappmvvm.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodappmvvm.databinding.FragmentFavoritesBinding
import com.example.foodappmvvm.ui.activities.MainActivity
import com.example.foodappmvvm.ui.activities.MealActivity
import com.example.foodappmvvm.ui.adapters.MealsAdapter
import com.example.foodappmvvm.util.Constants
import com.example.foodappmvvm.view_model.HomeViewModel
import com.google.android.material.snackbar.Snackbar


class FavoritesFragment : Fragment() {
    lateinit var mBinding: FragmentFavoritesBinding
    lateinit var mViewModel : HomeViewModel
    lateinit var mFavoritesRVAdapter : MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = (activity as MainActivity).viewModel
        mFavoritesRVAdapter = MealsAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentFavoritesBinding.inflate(inflater , container , false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareFavoriteRv()
        onFavoriteMealClick()
        observeFavoriteMeals()

       val itemTouchHelperCallBack = setItemTouchHelperCallBack()

        attachItemTouchHelperToRV(itemTouchHelperCallBack)

    }

    private fun attachItemTouchHelperToRV(itemTouchHelperCallBack: ItemTouchHelper.SimpleCallback) {
        ItemTouchHelper(itemTouchHelperCallBack).
        attachToRecyclerView(mBinding.favoriteMealRv)
    }

    private fun setItemTouchHelperCallBack() : ItemTouchHelper.SimpleCallback{
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val meal = mFavoritesRVAdapter.differ.currentList[position]
                mViewModel.deleteMeal(meal)

                Snackbar.make(requireView() , "Meal Deleted" , Snackbar.LENGTH_LONG).setAction(
                    "Undo"
                ) {
                    mViewModel.upsertMeal(meal)
                }.show()
            }

        }

        return itemTouchHelper
    }

    private fun prepareFavoriteRv() {
        mBinding.favoriteMealRv.apply {
            adapter = mFavoritesRVAdapter
            layoutManager = GridLayoutManager(context , 2 ,  GridLayoutManager.VERTICAL , false)
        }
    }

    private fun onFavoriteMealClick(){
        mFavoritesRVAdapter.setOnViewHolderClickListener {
            val intent = Intent(activity , MealActivity::class.java)

            intent.putExtra(Constants.MEAL_ID_TAG , it.idMeal)
            intent.putExtra(Constants.MEAL_THUMB_TAG , it.strMealThumb)
            intent.putExtra(Constants.MEAL_NAME_TAG , it.strMeal)

            startActivity(intent)
        }
    }

    private fun observeFavoriteMeals(){
        mViewModel.observeFavoriteMeals().observe(requireActivity() , Observer {
            mFavoritesRVAdapter.differ.submitList(it)
        })
    }

}