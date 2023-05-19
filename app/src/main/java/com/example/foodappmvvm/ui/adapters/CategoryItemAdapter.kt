package com.example.foodappmvvm.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodappmvvm.databinding.CategoryItemBinding
import com.example.foodappmvvm.model.data_class.Category
import com.example.foodappmvvm.model.data_class.CategoryMeals

class CategoryItemAdapter : RecyclerView.Adapter<CategoryItemAdapter.CategoryHolder>() {
    private var mCategories : ArrayList<Category> = ArrayList()
    private lateinit var mViewHolderClickListener : (Category) -> Unit


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(CategoryItemBinding.inflate(
            LayoutInflater.from(parent.context) , parent , false
        ))

    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(mCategories[position])
        holder.setViewHolderOnClickListener(mCategories[position] , mViewHolderClickListener)
    }

    override fun getItemCount() = mCategories.size

    fun setCategoryList(categoryList: ArrayList<Category>){
         mCategories = categoryList
         notifyDataSetChanged()
    }

    fun setViewHolderOnClickListener(listener : (Category) -> Unit ){
        mViewHolderClickListener = listener
    }

    inner class CategoryHolder(private val mBinding: CategoryItemBinding) : RecyclerView.ViewHolder(mBinding.root){

        fun bind(category : Category){
             Glide.with(itemView).load(category.strCategoryThumb).into(mBinding.categoryIv)
             mBinding.categoryTv.text = category.strCategory
        }

        fun setViewHolderOnClickListener(category: Category , listener: (Category) -> Unit){
            itemView.setOnClickListener { listener.invoke(category) }
        }
    }

}