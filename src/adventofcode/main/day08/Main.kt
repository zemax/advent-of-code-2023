package day08

import utils.getResourceAsLines
import utils.lcm

fun problem1(file: String): Int {
    val lines = getResourceAsLines(file)
    val directions = lines.first().map { if (it == 'L') 0 else 1 }
    val nodes = lines.drop(2).associate { line ->
        val (key, value) = line.split(" = ")
        val values = value.substring(1 until value.length - 1).split(", ")
        key to values
    }

//    println("Directions: $directions")

    var steps: Int = 0
    var current = "AAA"
    while (current != "ZZZ") {
        val direction = directions[steps % directions.size]
        val next = nodes[current]!![direction]
//        println("$steps : from $current to $next following $direction")
        current = next
        steps++
    }

    return steps
}

fun problem2(file: String): Long {
    val lines = getResourceAsLines(file)
    val directions = lines.first().map { if (it == 'L') 0 else 1 }
    val nodes = lines.drop(2).associate { line ->
        val (key, value) = line.split(" = ")
        val values = value.substring(1 until value.length - 1).split(", ")
        key to values
    }

//    println("Directions: $directions")

    val currents = nodes.keys.filter { it[2] == 'A' }.toMutableList()

//    println("Currents: $currents")

    val steps = currents.map { start ->
        var steps: Long = 0
        var current = start
        var found = 0
        while (found < 1) {
            val direction = directions[(steps % directions.size).toInt()]
            val next = nodes[current]!![direction]
            current = next
            steps++

            if (current[2] == 'Z') {
                found++
//                println("$found found for $start at $current in $steps")
            }
        }

        steps
    }

//    println("Steps: $steps")

    return lcm(steps)
}

fun main(args: Array<String>) {
    val day = "08"
    println("Day ${day}")

    println("Problem 1 Test 1: ${problem1("day${day}-p1.txt")}")
    println("Problem 1 Test 2: ${problem1("day${day}-p2.txt")}")
    println("Problem 1: ${problem1("day${day}.txt")}")

    println("Problem 2 Test: ${problem2("day${day}-p3.txt")}")
    println("Problem 2: ${problem2("day${day}.txt")}")
}
