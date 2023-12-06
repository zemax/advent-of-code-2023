package day06

import utils.getResourceAsText

class Race(val time: Long, val best: Long) {
    override fun toString(): String {
        return "Time: ${time}, Best: ${best}"
    }
}

class Boat {
    fun getDistance(chargeDuration: Long, raceDuration: Long): Long {
        val runDuration = raceDuration - chargeDuration
        val speed = chargeDuration
        val distance = speed * runDuration

        return distance
    }
}

fun problem1(file: String): Long {
    val lines = getResourceAsText(file)
        .split("\n")
        .map { line -> line.split(" ") }
        .map { array ->
            array.slice(1 until array.size)
                .filter { value -> value.isNotEmpty() }
        }

    val races = arrayListOf<Race>()
    for (i in lines.first().indices) {
        races.add(Race(lines.first()[i].toLong(), lines.last()[i].toLong()))
    }

    val winnings = races
        .map { race ->
            (0..race.time).filter { chargeDuration -> Boat().getDistance(chargeDuration, race.time) > race.best }
        }
        .map { it.size.toLong() }

    return winnings.reduce { acc, i -> i * acc }
}

fun problem2(file: String): Long {
    val lines = getResourceAsText(file)
        .split("\n")
        .map { line -> line.split(" ") }
        .map { array ->
            array.slice(1 until array.size)
                .filter { value -> value.isNotEmpty() }
                .joinToString("")
        }

    val race = Race(lines.first().toLong(), lines.last().toLong())

    println(race)

    val winnings = (0..race.time)
        .filter { chargeDuration -> Boat().getDistance(chargeDuration, race.time) > race.best }
        .size.toLong()

    return winnings
}

fun main(args: Array<String>) {
    val day = "06"
    println("Day ${day}")

    println("Problem 1 Test: ${problem1("day${day}-p1.txt")}")
    println("Problem 1: ${problem1("day${day}.txt")}")

    println("Problem 2 Test: ${problem2("day${day}-p2.txt")}")
    println("Problem 2: ${problem2("day${day}.txt")}")
}