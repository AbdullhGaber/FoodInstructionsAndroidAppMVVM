package com.example.foodappmvvm.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemLongClickListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodappmvvm.databinding.MealItemBinding
import com.example.foodappmvvm.model.data_class.CategoryMeals

class CategoryMealsAdapter : RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsHolder>() {
    private var mCategoryMeals = ArrayList<CategoryMeals>()
    private lateinit var mItemClickListener : (CategoryMeals) -> Unit


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsHolder {
        return CategoryMealsHolder(
            MealItemBinding.
            inflate(LayoutInflater.from(parent.context) , parent , false)
        )
    }

    override fun onBindViewHolder(holder: CategoryMealsHolder, position: Int) {
        holder.bind(mCategoryMeals[position])
        holder.setOnItemViewClickListener(mItemClickListener)
    }

    override fun getItemCount() = mCategoryMeals.size

    fun setCategoryMeals(categoryMeals: ArrayList<CategoryMeals>){
        mCategoryMeals = categoryMeals
        notifyDataSetChanged()
    }
    fun setOnItemClickListener(listener : (CategoryMeals) -> Unit){
        mItemClickListener = listener
    }

    inner class CategoryMealsHolder(private val mBinding : MealItemBinding) : RecyclerView.ViewHolder(mBinding.root){
        private lateinit var mCategoryMeal : CategoryMeals

        fun bind(categoryMeal : CategoryMeals){
            mCategoryMeal = categoryMeal
            Glide.with(itemView).load(categoryMeal.strMealThumb).into(mBinding.categoryMealIv)
            mBinding.categoryMealTv.text = categoryMeal.strMeal
        }

        fun setOnItemViewClickListener(listener: (CategoryMeals) -> Unit){
            itemView.setOnClickListener { listener.invoke(mCategoryMeal) }
        }


    }

}