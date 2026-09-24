package com.example.testapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.testapp.data.AuthRepository
import com.example.testapp.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val currentUser: StateFlow<User?> = authRepository.currentUser

    fun logout() = authRepository.logout()
}
