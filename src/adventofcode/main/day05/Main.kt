package day05

import utils.getResourceAsText
import kotlin.math.min

class MappingRange(val sourceIndex: Long, val destinationIndex: Long, val length: Long)

class Mapping(val source: String, val destination: String) {
    var ranges = arrayListOf<MappingRange>()

    fun addRange(line: String) {
        val mappings = line.split(" ").map { it.toLong() }
        ranges.add(MappingRange(mappings[1], mappings[0], mappings[2]))
    }

    fun getNext(sourceId: Long): Long {
        ranges.forEach { range ->
            if (sourceId >= range.sourceIndex && sourceId < range.sourceIndex + range.length) {
                return sourceId + range.destinationIndex - range.sourceIndex
            }
        }

        return sourceId
    }
}

fun process(seed: Long, mappings: HashMap<String, Mapping>, end: String = "location"): Long {
    var id = seed
    var current = "seed"
    while (current != end) {
        val mapping = mappings[current]!!
        id = mapping.getNext(id)
        current = mapping.destination
    }

    return id
}

fun problem1(file: String): Long {
    var currentMapping = listOf<String>()
    val seeds = arrayListOf<Long>()
    val mappings = hashMapOf<String, Mapping>()

    getResourceAsText(file)
        .split("\n")
        .forEach { line ->
            if (line.startsWith("seeds: ")) {
                seeds.addAll(line.substring("seeds: ".length).split(" ").map { it.toLong() })
            } else if (line.contains(" map")) {
                currentMapping = line.split(" ").first().split("-to-")
                mappings[currentMapping.first()] = Mapping(currentMapping.first(), currentMapping.last())
            } else if (line.isNotEmpty()) {
                mappings[currentMapping.first()]!!.addRange(line)
            }
        }

    val locations = seeds.map { process(it, mappings) }

    return locations.min()
}

fun problem2(file: String): Long {
    var currentMapping = listOf<String>()
    val seedsRanges = arrayListOf<List<Long>>()
    val mappings = hashMapOf<String, Mapping>()

    getResourceAsText(file)
        .split("\n")
        .forEach { line ->
            if (line.startsWith("seeds: ")) {
                val p = line.substring("seeds: ".length).split(" ").map { it.toLong() }
                for (i in p.indices step 2) {
                    seedsRanges.add(listOf(p[i], p[i + 1]))
                }
            } else if (line.contains(" map")) {
                currentMapping = line.split(" ").first().split("-to-")
                mappings[currentMapping.first()] = Mapping(currentMapping.first(), currentMapping.last())
            } else if (line.isNotEmpty()) {
                mappings[currentMapping.first()]!!.addRange(line)
            }
        }

    var m: Long = 0
    var n: Long = 0
    println("Processing ${seedsRanges.size} ranges")
    seedsRanges.forEach { range ->
        println("... Starting range ${range.first()} to ${range.first() + range.last() - 1} of ${range.last()}")
        for (seed in range.first()..<range.first() + range.last()) {
            n++
            val location = process(seed, mappings)
            m = if (m == 0.toLong()) location else min(m, location)
        }
        println("... Processed ${n} seeds, current min ${m}")
    }

    return m
}

fun main(args: Array<String>) {
    val day = "05"
    println("Day ${day}")

    println("Problem 1 Test: ${problem1("day${day}-p1.txt")}")
    println("Problem 1: ${problem1("day${day}.txt")}")

    println("Problem 2 Test: ${problem2("day${day}-p2.txt")}")
    println("Problem 2: ${problem2("day${day}.txt")}")
}