package com.example.foodappmvvm.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodappmvvm.databinding.PopularListItemBinding
import com.example.foodappmvvm.model.data_class.CategoryMeals


class PopularItemsAdapter : RecyclerView.Adapter<PopularItemsAdapter.PopularViewHolder>() {
    private var mMeals = ArrayList<CategoryMeals>()
    private lateinit var mViewHolderClickListener : (CategoryMeals) -> Unit
    private lateinit var mItemLongClickListener: (CategoryMeals) -> Unit


    fun setMeals(meals : ArrayList<CategoryMeals>){
        mMeals = meals
        notifyDataSetChanged()
    }

    fun setViewHolderOnClickListener(viewHolderClickListener : (CategoryMeals) -> Unit ){
        mViewHolderClickListener = viewHolderClickListener
    }

    fun setViewHolderOnLongClickListener(viewHolderLongClickListener : (CategoryMeals) -> Unit ){
        mItemLongClickListener = viewHolderLongClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {

        return PopularViewHolder(PopularListItemBinding.inflate(
            LayoutInflater.from(parent.context),parent, false
        ))
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val meal = mMeals[position]
        holder.bind(meal)
        holder.setViewHolderOnClickListener(mViewHolderClickListener)
        holder.setOnItemViewLongClickListener(mItemLongClickListener)
    }

    override fun getItemCount() = mMeals.size


    inner class PopularViewHolder(private val mBinding: PopularListItemBinding) : RecyclerView.ViewHolder(mBinding.root){
        private lateinit var mCategoryMeal : CategoryMeals

         fun bind(meal : CategoryMeals){
            mCategoryMeal = meal
            Glide.with(mBinding.root).
            load(meal.strMealThumb).
            into(mBinding.overPopularItemsIv)
        }
        
        fun setViewHolderOnClickListener( listener : (CategoryMeals) -> Unit){
            itemView.setOnClickListener { listener.invoke(mCategoryMeal) }
        }

        fun setOnItemViewLongClickListener(listener: (CategoryMeals) -> Unit){
            itemView.setOnLongClickListener { listener.invoke(mCategoryMeal); true }
        }
    }

}
