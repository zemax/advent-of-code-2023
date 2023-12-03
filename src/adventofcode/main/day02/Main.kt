package day02

import utils.getResourceAsText
import kotlin.math.max

class SubSet(input: String) {
    val number = input.split(" ").first().toInt()
    val color = input.split(" ").last()
}

class Game(input: String) {
    val id = input.split(": ").first()
        .split(" ").last()
        .toInt()

    val sets = input.split(": ").last()
        .split("; ")
        .map { it.split(", ").map { s -> SubSet(s) } }

    val maxCubes: HashMap<String, Int> = hashMapOf()

    init {
        sets.forEach { s ->
            s.forEach {
                maxCubes[it.color] = max(maxCubes[it.color] ?: 0, it.number)
            }
        }
    }

    fun isPossibleWith(elfSet: ArrayList<SubSet>): Boolean {
        for (cube in elfSet) {
            if (maxCubes[cube.color]!! > cube.number) {
                return false
            }
        }

        return true
    }

    fun power(): Int {
        return maxCubes.values.reduce { acc, i -> acc * i }
    }
}

fun problem1(file: String): Int {
    return getResourceAsText(file)
        .split("\n")
        .map { Game(it) }
        .filter {
            it.isPossibleWith(
                arrayListOf(
                    SubSet("12 red"),
                    SubSet("13 green"),
                    SubSet("14 blue")
                )
            )
        }
        .map { it.id }
        .sum()

}

fun problem2(file: String): Int {
    return getResourceAsText(file)
        .split("\n")
        .map { Game(it) }
        .map { it.power() }
        .sum()
}

fun main(args: Array<String>) {
    println("Day 02")

    println("Problem 1 Test: ${problem1("day02-p1.txt")}")
    println("Problem 1: ${problem1("day02.txt")}")

    println("Problem 2 Test: ${problem2("day02-p2.txt")}")
    println("Problem 2: ${problem2("day02.txt")}")
}