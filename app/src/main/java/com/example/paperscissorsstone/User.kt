package com.example.paperscissorsstone

data class User(
    val uuid : String,
    val name : String
){
    constructor():this("","")
}