package com.hse_project.hse_slaves.model

data class User(
    var id : Int,
    var userRole : String,
    var firstName : String,
    var lastName : String,
    var patronymic : String,
    var username : String,
    var password : String,
    var specialization : String,
    var rating : Double,
    var description : String,
    var images : ArrayList<String>,
    var enabled : Boolean,
    var authorities : ArrayList<Authorities>,
    var accountNonLocked : Boolean,
    var accountNonExpired : Boolean,
    var credentialsNonExpired : Boolean
)
