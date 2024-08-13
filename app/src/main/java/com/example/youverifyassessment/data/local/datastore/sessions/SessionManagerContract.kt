package com.example.youverifyassessment.data.local.datastore.sessions

import com.example.youverifyassessment.domain.model.UserDetailsDomain
import kotlinx.coroutines.flow.Flow

interface SessionManagerContract {
    suspend fun saveUser(user: UserDetailsDomain)
    fun getCurrentlyLoggedInUser(): Flow<UserDetailsDomain?>
}