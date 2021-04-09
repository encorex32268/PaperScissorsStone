package com.example.paperscissorsstone.model

data class User(
    val uuid : String,
    val name : String
){
    constructor():this("","")
}