package com.example.youverifyassessment.data.local.datastore.sessions

import com.example.youverifyassessment.domain.model.UserDetailsDomain
import kotlinx.coroutines.flow.Flow

interface SessionManagerContract {
    suspend fun saveUser(user: UserDetailsDomain)
    suspend fun getCurrentlyLoggedInUser(): Flow<UserDetailsDomain?>
}