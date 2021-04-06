package com.example.paperscissorsstone

fun main() {

    var resultString = ""
    for (string in "-MXabt20pJ8KXp6aA0f4") {
        val char = string
        val temp : Int = string.toInt()
        print("$temp")
        resultString+=temp
    }
    println("")
    println("Result : ${resultString.substring(0,8)}")
}