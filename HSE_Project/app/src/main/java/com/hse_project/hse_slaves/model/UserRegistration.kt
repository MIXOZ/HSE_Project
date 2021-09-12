package com.hse_project.hse_slaves.model

data class UserRegistration(
    var userRole: String,
    var firstName: String,
    var lastName: String,
    var patronymic: String,
    var username: String,
    var password: String,
    var specialization: String,
    var description: String,
    var images: ArrayList<String>
)
