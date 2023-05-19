package com.example.foodappmvvm.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.foodappmvvm.databinding.ActivityMainBinding
import com.example.foodappmvvm.model.room_db.MealRoomDB
import com.example.foodappmvvm.view_model.HomeViewModel
import com.example.foodappmvvm.view_model.HomeViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var mBinding : ActivityMainBinding

    val viewModel : HomeViewModel by lazy {
        val homeViewModelFactory = HomeViewModelFactory(MealRoomDB.getInstance(this))

        ViewModelProvider(this , homeViewModelFactory)[HomeViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(mBinding.hostFragment.id) as NavHostFragment
        val navController = navHostFragment.findNavController()

        NavigationUI.setupWithNavController(mBinding.navBottom,navController)
    }
}