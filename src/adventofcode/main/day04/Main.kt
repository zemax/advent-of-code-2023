package day04

import utils.getResourceAsText
import kotlin.math.pow

class Card {
    var winningNumbers: List<Int>
    var numbers: List<Int>
    var copies: Int = 1

    constructor(line: String) {
        val a = line.split(": ").last().split(" | ")
        winningNumbers = a.first().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        numbers = a.last().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
    }

    fun matches(): Int {
        return numbers.filter { winningNumbers.contains(it) }.size
    }

    fun score(): Int {
        val m = matches()
        if (m == 0) {
            return 0
        }

        return 2.0.pow(matches() - 1).toInt()
    }
}

fun problem1(file: String): Int {
    return getResourceAsText(file)
        .split("\n")
        .map { Card(it) }
        .map { it.score() }
        .sum()
}

fun problem2(file: String): Int {
    val cards = getResourceAsText(file)
        .split("\n")
        .map { Card(it) }

    cards.forEachIndexed { index, card ->
        val n = card.matches()
        if (n > 0) {
            for (i in (index + 1)..(index + n)) {
                cards[i].copies += card.copies
            }
        }
    }

    val copies = cards.map { it.copies }

    return copies.sum()
}

fun main(args: Array<String>) {
    val day = "04"
    println("Day ${day}")

    println("Problem 1 Test: ${problem1("day${day}-p1.txt")}")
    println("Problem 1: ${problem1("day${day}.txt")}")

    println("Problem 2 Test: ${problem2("day${day}-p2.txt")}")
    println("Problem 2: ${problem2("day${day}.txt")}")
}