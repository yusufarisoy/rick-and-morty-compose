package com.yusufarisoy.composeapp.domain

import com.yusufarisoy.composeapp.data.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStore @Inject constructor() {

    private var user: User? = null

    fun getSession() = user

    fun createSession(user: User) {
        this.user = user
    }
}
