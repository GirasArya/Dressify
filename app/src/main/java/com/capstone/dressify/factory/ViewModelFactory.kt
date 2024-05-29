package com.capstone.dressify.factory

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.compose.ui.window.application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.capstone.dressify.di.Injection
import com.capstone.dressify.ui.viewmodel.FavoriteViewModel
import com.capstone.dressify.ui.viewmodel.MainViewModel

class ViewModelFactory private constructor(
    private val mApplication: Application,
    private val context: Context
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(mApplication) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application, context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(application, context)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}