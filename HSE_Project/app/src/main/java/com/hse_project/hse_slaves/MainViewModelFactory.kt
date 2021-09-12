package com.hse_project.hse_slaves

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hse_project.hse_slaves.repository.Repository

class MainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}