package day10

import utils.getResourceAsLines

class Vector(val x: Int, val y: Int) {
    override fun toString(): String {
        return "$x,$y"
    }
}

class Cell(val position: Vector, var symbol: Char) {
    var passes = 0
    var isIn = false
    var isMainLoop = false
    fun next(from: Vector): Vector {
        val dx = position.x - from.x
        val dy = position.y - from.y

//        print("($dx, $dy)")

        if (symbol == '|') {
            if (dx == 0) return Vector(position.x, position.y + dy)
        }
        if (symbol == '-') {
            if (dy == 0) return Vector(position.x + dx, position.y)
        }
        if (symbol == 'L') {
            if ((dx == 0) && (dy == 1)) return Vector(position.x + 1, position.y)
            if ((dx == -1) && (dy == 0)) return Vector(position.x, position.y - 1)
        }
        if (symbol == 'J') {
            if ((dx == 0) && (dy == 1)) return Vector(position.x - 1, position.y)
            if ((dx == 1) && (dy == 0)) return Vector(position.x, position.y - 1)
        }
        if (symbol == '7') {
            if ((dx == 0) && (dy == -1)) return Vector(position.x - 1, position.y)
            if ((dx == 1) && (dy == 0)) return Vector(position.x, position.y + 1)
        }
        if (symbol == 'F') {
            if ((dx == 0) && (dy == -1)) return Vector(position.x + 1, position.y)
            if ((dx == -1) && (dy == 0)) return Vector(position.x, position.y + 1)
        }

        return from
    }

    override fun toString(): String {
        if (passes > 0) return passes.toString()
        if (!isMainLoop) return " "

        return when (symbol) {
            '|' -> "║"
            '-' -> "═"
            'L' -> "╚"
            'J' -> "╝"
            '7' -> "╗"
            'F' -> "╔"
            'S' -> "S"
            else -> " "
        }
    }
}

fun hasNorth(start: Vector, cells: List<List<Cell>>): Boolean {
    return start.y > 0 && listOf<Char>(
        '|',
        '7',
        'F'
    ).contains(cells[start.y - 1][start.x].symbol)
}

fun hasEast(start: Vector, cells: List<List<Cell>>): Boolean {
    return start.x < cells[0].size - 1 && listOf<Char>(
        'J',
        '7',
        '-'
    ).contains(cells[start.y][start.x + 1].symbol)
}

fun hasSouth(start: Vector, cells: List<List<Cell>>): Boolean {
    return start.y < cells.size - 1 && listOf<Char>(
        '|',
        'L',
        'J'
    ).contains(cells[start.y + 1][start.x].symbol)
}

fun hasWest(start: Vector, cells: List<List<Cell>>): Boolean {
    return start.x > 0 && listOf<Char>(
        'L',
        'F',
        '-'
    ).contains(cells[start.y][start.x - 1].symbol)
}

fun firstDestination(start: Vector, cells: List<List<Cell>>): Cell {
    return when {
        hasNorth(start, cells) -> cells[start.y - 1][start.x]
        hasEast(start, cells) -> cells[start.y][start.x + 1]
        hasSouth(start, cells) -> cells[start.y + 1][start.x]
        else -> cells[start.y][start.x - 1]
    }
}

fun walkthrough(start: Vector, cells: List<List<Cell>>): Int {
    var currentCell = cells[start.y][start.x]
    var destinationCell = firstDestination(start, cells)
    var distance = 0

    do {
        currentCell.isMainLoop = true
        distance++
//        print("${currentCell.position} -> ${destinationCell.position} ")
        val nextPosition = destinationCell.next(currentCell.position)
        currentCell = destinationCell
        destinationCell = cells[nextPosition.y][nextPosition.x]
//        println(" -> ${destinationCell.position} ")
    } while (currentCell.symbol != 'S')

    return distance
}

fun problem1(file: String): Int {
    var start: Vector = Vector(0, 0)
    val cells = getResourceAsLines(file).mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c == 'S') start = Vector(x, y)
            Cell(Vector(x, y), c)
        }
    }

    val distance = walkthrough(start, cells)
    println(cells.map { l -> l.map { c -> c.toString() }.joinToString("") }.joinToString("\n"))

    return distance / 2
}

fun problem2(file: String): Int {
    var start: Vector = Vector(0, 0)
    val cells = getResourceAsLines(file).mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c == 'S') start = Vector(x, y)
            Cell(Vector(x, y), c)
        }
    }

    val distance = walkthrough(start, cells)

    when {
        hasNorth(start, cells) && hasSouth(start, cells) -> cells[start.y][start.x].symbol = '|'
        hasEast(start, cells) && hasWest(start, cells) -> cells[start.y][start.x].symbol = '-'
        hasNorth(start, cells) && hasEast(start, cells) -> cells[start.y][start.x].symbol = 'L'
        hasNorth(start, cells) && hasWest(start, cells) -> cells[start.y][start.x].symbol = 'J'
        hasSouth(start, cells) && hasWest(start, cells) -> cells[start.y][start.x].symbol = '7'
        else -> cells[start.y][start.x].symbol = 'F'
    }

    var cellsIn = 0
    for (y in cells.indices) {
        for (x in cells[y].indices) {
            val start = cells[y][x]
            if (start.isMainLoop) {
                continue
            }

            val passes = cells[y].subList(x, cells[y].size)
                .filter { it.isMainLoop }
                .map { cell -> cell.symbol }
                .joinToString("")
                .replace("-", "")
                .replace("F7", "")
                .replace("LJ", "")
                .replace("L7", "|")
                .replace("FJ", "|")
                .length

            cells[y][x].passes = passes

            if (passes % 2 == 1) {
                cellsIn++
            }
        }
    }

//    println(cells.map { l -> l.map { c -> c.toString() }.joinToString("") }.joinToString("\n"))

    return cellsIn
}

fun main(args: Array<String>) {
    val day = "10"
    println("Day ${day}")

    println("Problem 1 Test 1: ${problem1("day${day}-p1.txt")}")
    println("Problem 1 Test 2: ${problem1("day${day}-p2.txt")}")
    println("Problem 1: ${problem1("day${day}.txt")}")
    println("Problem 2 Test: ${problem2("day${day}-p3.txt")}")
    println("Problem 2 Test: ${problem2("day${day}-p4.txt")}")
    println("Problem 2: ${problem2("day${day}.txt")}")
}
