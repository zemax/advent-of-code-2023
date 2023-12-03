package day01

import utils.getResourceAsText
import utils.replaceNumbers

fun problem1(file: String): Int {
    return getResourceAsText(file)
        .split("\n")
        .map { it.replace("[^0-9]".toRegex(), "") }
        .filter { it.isNotEmpty() }
        .map { "${it.first()}${it.last()}" }
        .map { it.toInt() }
        .sum()
}

fun problem2(file: String): Int {
    return getResourceAsText(file)
        .split("\n")
        .map { replaceNumbers(it) }
        .map { it.replace("[^0-9]".toRegex(), "") }
        .filter { it.isNotEmpty() }
        .map { "${it.first()}${it.last()}" }
        .map { it.toInt() }
        .sum()
}

fun main(args: Array<String>) {
    println("Day 01")

    println("Problem 1 Test: ${problem1("day01-p1.txt")}")
    println("Problem 1: ${problem1("day01.txt")}")

    println("Problem 2 Test: ${problem2("day01-p2.txt")}")
    println("Problem 2: ${problem2("day01.txt")}")
}