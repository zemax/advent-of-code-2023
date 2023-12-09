package day09

import utils.getResourceAsLines

fun derive(input: List<Long>): MutableList<Long> {
    val o = arrayListOf<Long>()

    for (i in 0..<input.size - 1) {
        o.add(input[i + 1] - input[i])
    }

    return o
}

fun isZeroes(input: List<Long>): Boolean {
    if (input.size <= 1) {
        return true
    }

    return input.filter { it != 0.toLong() }.isEmpty()
}

class History(line: String) {
    val values = line.split(" ").map { it.toLong() }
    val sequences = arrayListOf<MutableList<Long>>()

    override fun toString(): String {
        return "${sequences}"
    }

    init {
        sequences.add(values.toMutableList())
        while (!isZeroes(sequences.last())) {
            sequences.add(derive(sequences.last()))
        }

        sequences.last().add(0, 0)
        sequences.last().add(0)
        for (i in sequences.size - 2 downTo 0) {
            sequences[i].add(0, sequences[i].first() - sequences[i + 1].first())
            sequences[i].add(sequences[i].last() + sequences[i + 1].last())
        }
    }

    fun prevValue(): Long {
        return sequences.first().first()
    }

    fun nextValue(): Long {
        return sequences.first().last()
    }
}

fun problem1(file: String): Long {
    val lines = getResourceAsLines(file)
    val histories = lines.map { History(it) }

//    histories.forEach { println(it) }

    return histories.map { it.nextValue() }.sum()
}

fun problem2(file: String): Long {
    val lines = getResourceAsLines(file)
    val histories = lines.map { History(it) }

//    histories.forEach { println(it) }

    return histories.map { it.prevValue() }.sum()
}

fun main(args: Array<String>) {
    val day = "09"
    println("Day ${day}")

    println("Problem 1 Test 1: ${problem1("day${day}-p1.txt")}")
    println("Problem 1: ${problem1("day${day}.txt")}")

    println("Problem 2 Test: ${problem2("day${day}-p1.txt")}")
    println("Problem 2: ${problem2("day${day}.txt")}")
}
