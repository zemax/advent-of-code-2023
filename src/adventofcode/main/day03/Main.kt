package day03

import utils.getResourceAsText
import kotlin.math.max
import kotlin.math.min

class Position(
    val x: Int,
    val y: Int
) {
    override fun toString(): String {
        return "${x},${y}"
    }
}

class NumberInSchematic(
    val number: Int,
    val position: Position,
    val size: Int
) {
    override fun toString(): String {
        return "${number}(${position})(${size})"
    }
}

class Surroundings(
    val number: NumberInSchematic,
    val position: Position,
    val portions: ArrayList<String>
) {
    fun getGears(): ArrayList<Position> {
        val gears = arrayListOf<Position>()
        for (y in portions.indices) {
            val x = portions[y].indexOf("*")
            if (x >= 0) {
                gears.add(Position(position.x + x, position.y + y))
            }
        }

        return gears
    }
}

fun parseLine(line: String, y: Int): ArrayList<NumberInSchematic> {
    val numbers: ArrayList<NumberInSchematic> = arrayListOf()
    var x = 0
    while (x < line.length) {
        if (line[x].isDigit()) {
            var j = x
            do {
                j++
            } while (j < line.length && line[j].isDigit())
            val size = j - x
            val number = line.substring(x, j)
            numbers.add(NumberInSchematic(number.toInt(), Position(x, y), size))
            x = j
        }
        x++
    }

    return numbers
}

fun surroundings(number: NumberInSchematic, lines: List<String>): Surroundings {
    val yMin = max(0, number.position.y - 1)
    val yMax = min(lines.size - 1, number.position.y + 1)
    val xMin = max(0, number.position.x - 1)
    val xMax = min(lines[yMin].length, number.position.x + number.size + 1)

    val portions = arrayListOf<String>()

    for (y in yMin..yMax) {
        val portion = lines[y].substring(xMin, xMax)
        portions.add(portion)
    }

    return Surroundings(number, Position(xMin, yMin), portions)
}

fun problem1(file: String): Int {
    val lines = getResourceAsText(file)
        .split("\n")

    val numbers: ArrayList<NumberInSchematic> = arrayListOf()

    for (y in lines.indices) {
        val line = lines[y]
        val lineNumbers = parseLine(line, y)
        numbers.addAll(lineNumbers)
    }

    val parts = numbers.filter { number ->
        val surroundings = surroundings(number, lines).portions.joinToString("")
        surroundings.replace("[0-9.]".toRegex(), "").isNotEmpty()
    }

    return parts
        .map { it.number }
        .sum()
}

fun problem2(file: String): Int {
    val lines = getResourceAsText(file)
        .split("\n")

    val numbers: ArrayList<NumberInSchematic> = arrayListOf()

    for (y in lines.indices) {
        val line = lines[y]
        val lineNumbers = parseLine(line, y)
        numbers.addAll(lineNumbers)
    }

    val possibleGears = hashMapOf<String, ArrayList<NumberInSchematic>>()
    numbers.map { surroundings(it, lines) }
        .forEach { surrounding ->
            val gears = surrounding.getGears()
            if (gears.isNotEmpty()) {
                gears.forEach { gear ->
                    if (possibleGears[gear.toString()].isNullOrEmpty()) {
                        possibleGears[gear.toString()] = arrayListOf(surrounding.number)
                    } else {
                        possibleGears[gear.toString()]?.add(surrounding.number)
                    }
                }
            }
        }

    val gears = possibleGears.filter { (key, value) -> value.size == 2 }

    return gears.map { (key, value) -> value.first().number * value.last().number }
        .sum()
}

fun main(args: Array<String>) {
    val day = "03"
    println("Day ${day}")

    println("Problem 1 Test: ${problem1("day${day}-p1.txt")}")
    println("Problem 1: ${problem1("day${day}.txt")}")

    println("Problem 2 Test: ${problem2("day${day}-p2.txt")}")
    println("Problem 2: ${problem2("day${day}.txt")}")
}