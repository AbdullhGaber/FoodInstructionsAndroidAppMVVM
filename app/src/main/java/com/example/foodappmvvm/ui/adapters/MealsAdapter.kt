package com.example.foodappmvvm.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodappmvvm.databinding.MealItemBinding
import com.example.foodappmvvm.model.data_class.Meal

class MealsAdapter : RecyclerView.Adapter<MealsAdapter.FavoriteMealViewHolder>() {
    private lateinit var mViewHolderClickListener : (meal : Meal) -> Unit

    private val differUtil = object : DiffUtil.ItemCallback<Meal>(){
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this , differUtil)

    fun setOnViewHolderClickListener(viewHolderClickListener : (meal : Meal) -> Unit ){
        mViewHolderClickListener = viewHolderClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteMealViewHolder {
        return FavoriteMealViewHolder(
            MealItemBinding.inflate(LayoutInflater.from(parent.context),parent , false)
        )
    }

    override fun onBindViewHolder(holder: FavoriteMealViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.onViewHolderClickListener(mViewHolderClickListener)
    }

    override fun getItemCount() = differ.currentList.size

    inner class FavoriteMealViewHolder(private val mBinding: MealItemBinding) : RecyclerView.ViewHolder(mBinding.root){
        private lateinit var mMeal : Meal

        fun bind(meal : Meal){
            mMeal = meal
            mBinding.categoryMealTv.text = meal.strMeal
            Glide.with(itemView).load(meal.strMealThumb).into(mBinding.categoryMealIv)
        }
        fun onViewHolderClickListener(listener : (meal : Meal) -> Unit ){
            itemView.setOnClickListener {
                listener.invoke(mMeal)
            }
        }
    }
}