package com.example.paperscissorsstone

import kotlin.random.Random
import kotlin.random.nextUInt

fun main() {


    val papper = R.drawable.icon_play_papper
    val scissors = R.drawable.icon_play_scissors
    val stone = R.drawable.icon_play_stone
    val cards = arrayListOf(5,2,0)
    for (i in 1..1000){
        val random1 = Random.nextInt(0,cards.size)
        val random2 = Random.nextInt(0,cards.size)
        println("card1 1 : ${cards[random1]} , card2 : ${cards[random2]}")
        val player1 = Player("Nick",cards[random1])
        val player2 = Player("Lee",cards[random2])
        var result = compare(player1,player2)
        println("Result >>> $result")

    }




}

fun compare(player1 : Player , player2 : Player) : String {
    var result = ""
    when (player1.card) {
        5 ->
            result =  when(player2.card){
                2 -> "${player1.name} Loss , ${player2.name} Win"
                0 -> "${player1.name} Win , ${player2.name} Loss"
                else -> "Not Loss Not Win"
            }
        2 ->
            result =  when(player2.card){
                5 -> "${player1.name} Win , ${player2.name} Loss"
                0 -> "${player1.name} Loss , ${player2.name} Win"
                else -> "Not Loss Not Win"
            }
        0 ->
            result = when(player2.card){
                2 -> "${player1.name} Win , ${player2.name} Loss"
                5 -> "${player1.name} Loss , ${player2.name} Win"
                else -> "Not Loss Not Win"
            }
    }
    return result

}

data class Player(
        val name : String ,
        val card : Int
)