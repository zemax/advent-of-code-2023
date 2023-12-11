package day11

import utils.Vector
import utils.getResourceAsLines
import kotlin.math.max
import kotlin.math.min

val VOID = '.'
val VOID_XP = 'x'

fun display(map: List<String>) {
    map.forEach { println(it) }
}

fun expand(map: List<String>): List<String> {
    var mapExpanded = mutableListOf<String>()

    map.forEach { line ->
        if (line.none { it != VOID }) {
            mapExpanded.add(VOID_XP.toString().repeat(line.length))
        } else {
            mapExpanded.add(line)
        }
    }

    for (i in mapExpanded.first().indices) {
        val column = mapExpanded.map { it[i] }

        if (column.none { it != VOID && it != VOID_XP }) {
            mapExpanded = mapExpanded
                .map { s -> s.substring(0, i) + VOID_XP + s.substring(i + 1, s.length) }
                .toMutableList()
        }
    }

    return mapExpanded
}

fun getGalaxies(map: List<String>): List<Vector> {
    val galaxies = mutableListOf<Vector>()

    map.forEachIndexed { y, line ->
        line.forEachIndexed { x, char ->
            if (char == '#') {
                galaxies.add(Vector(x, y))
            }
        }
    }

    return galaxies
}

fun distance(v1: Vector, v2: Vector, map: List<String>, xp: Long = 2): Long {
    val xMin = min(v1.x, v2.x)
    val xMax = max(v1.x, v2.x)

    val yMin = min(v1.y, v2.y)
    val yMax = max(v1.y, v2.y)

    val horizontalPath = map[yMin].substring(xMin, xMax)
    val verticalPath = map.map { it[xMin].toString() }.joinToString("").substring(yMin, yMax)

    return horizontalPath.length.toLong() + horizontalPath.filter { it == VOID_XP }.length * (xp - 1) + verticalPath.length.toLong() + verticalPath.filter { it == VOID_XP }.length * (xp - 1)
}

fun problem1(file: String): Long {
    val map = getResourceAsLines(file)
    val expanded = expand(map)

//    display(expanded)

    val galaxies = getGalaxies(expanded)

    var distance: Long = 0
    for (i in 0..galaxies.size - 2) {
        for (j in i + 1..galaxies.size - 1) {
            val d = distance(galaxies[i], galaxies[j], expanded)
//            println("Between galaxy ${i + 1} and galaxy ${j + 1}: $d")
            distance += d
        }
    }

    return distance
}

fun problem2(file: String): Long {
    val map = getResourceAsLines(file)
    val expanded = expand(map)

//    display(expanded)

    val galaxies = getGalaxies(expanded)

    var distance: Long = 0
    for (i in 0..galaxies.size - 2) {
        for (j in i + 1..galaxies.size - 1) {
            val d = distance(galaxies[i], galaxies[j], expanded, 1000000)
//            println("Between galaxy ${i + 1} and galaxy ${j + 1}: $d")
            distance += d
        }
    }

    return distance
}

fun main(args: Array<String>) {
    val day = "11"
    println("Day ${day}")

    println("Problem 1 Test: ${problem1("day${day}-p1.txt")}")
    println("Problem 1: ${problem1("day${day}.txt")}")
    println("Problem 2 Test: ${problem2("day${day}-p1.txt")}")
    println("Problem 2: ${problem2("day${day}.txt")}")
}
