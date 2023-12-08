package day07

import utils.getResourceAsText
import kotlin.math.max
import kotlin.math.pow

class Hand(private val input: String, private val withJoker: Boolean = false) {
    val types = listOf<String>("FIVE", "FOUR", "FULL", "THREE", "TWOPAIRS", "PAIR", "HIGHCARD").reversed()
    val cardsOrdered = if (withJoker) {
        listOf<String>("A", "K", "Q", "T", "9", "8", "7", "6", "5", "4", "3", "2", "J").reversed()
    } else {
        listOf<String>("A", "K", "Q", "J", "T", "9", "8", "7", "6", "5", "4", "3", "2").reversed()
    }
    val scoreBasis = max(cardsOrdered.size, types.size)

    private val cards: List<String> = input.split("")
    private var type: String = ""
    private var score: Long = -1

    override fun toString(): String {
        return "${input} (${type()}:${score()})"
    }

    fun occurences(card: String): Int {
        return cards.filter { it == card }.size
    }

    fun type(): String {
        if (type == "") {
            if (!withJoker) {
                val o = cardsOrdered.map { occurences(it) }
                type = when {
                    o.contains(5) -> "FIVE"
                    o.contains(4) -> "FOUR"
                    o.contains(3) && o.contains(2) -> "FULL"
                    o.contains(3) -> "THREE"
                    (o.filter { it == 2 }.size == 2) -> "TWOPAIRS"
                    o.contains(2) -> "PAIR"
                    else -> "HIGHCARD"
                }
            } else {
                var o = cardsOrdered.map { occurences(it) }
                val jokers = o.first()
                o = o.slice(1 until o.size)

                type = when {
                    o.contains(5) -> "FIVE"
                    o.contains(4) && (jokers == 1) -> "FIVE"
                    o.contains(3) && (jokers == 2) -> "FIVE"
                    o.contains(2) && (jokers == 3) -> "FIVE"
                    o.contains(1) && (jokers == 4) -> "FIVE"
                    (jokers == 5) -> "FIVE"

                    o.contains(4) -> "FOUR"
                    o.contains(3) && (jokers == 1) -> "FOUR"
                    o.contains(2) && (jokers == 2) -> "FOUR"
                    o.contains(1) && (jokers == 3) -> "FOUR"

                    o.contains(3) && o.contains(2) -> "FULL"
                    (o.filter { it == 2 }.size == 2) && (jokers == 1) -> "FULL"

                    o.contains(3) -> "THREE"
                    o.contains(2) && (jokers == 1) -> "THREE"
                    o.contains(1) && (jokers == 2) -> "THREE"

                    (o.filter { it == 2 }.size == 2) -> "TWOPAIRS"
                    o.contains(2) && (jokers == 1) -> "TWOPAIRS"

                    o.contains(2) -> "PAIR"
                    o.contains(1) && (jokers == 1) -> "PAIR"

                    else -> "HIGHCARD"
                }
            }
        }

        return type
    }

    fun score(): Long {
        if (score < 0) {
            val typeScore = types.indexOf(type())

            score = typeScore * scoreBasis.toDouble().pow((cards.size + 1).toDouble()).toLong()

            for (i in cards.indices) {
                val cardScore = cardsOrdered.indexOf(cards[i])
                score += cardScore * scoreBasis.toDouble().pow((cards.size - i).toDouble()).toLong()
            }
        }

        return score
    }
}

class Input(val hand: Hand, val bid: Int) {
    override fun toString(): String {
        return "${hand} : ${bid}"
    }
}

fun problem1(file: String): Long {
    var s: Long = 0
    val inputs = getResourceAsText(file)
        .split("\n")
        .map { input ->
            val a = input.split(" ")
            Input(Hand(a[0]), a[1].toInt())
        }
        .sortedWith(Comparator<Input> { input1, input2 ->
            when {
                input1.hand.score() > input2.hand.score() -> 1
                input1.hand.score() < input2.hand.score() -> -1
                else -> 0
            }
        })
        .forEachIndexed { index, input -> s += (index + 1) * input.bid }

    return s
}

fun problem2(file: String): Long {
    var s: Long = 0
    val inputs = getResourceAsText(file)
        .split("\n")
        .map { input ->
            val a = input.split(" ")
            Input(Hand(a[0], true), a[1].toInt())
        }
        .sortedWith(Comparator<Input> { input1, input2 ->
            when {
                input1.hand.score() > input2.hand.score() -> 1
                input1.hand.score() < input2.hand.score() -> -1
                else -> 0
            }
        })
        .forEachIndexed { index, input -> s += (index + 1) * input.bid }

    return s
}

fun main(args: Array<String>) {
    val day = "07"
    println("Day ${day}")

    println("Problem 1 Test: ${problem1("day${day}-p1.txt")}")
    println("Problem 1: ${problem1("day${day}.txt")}")

    println("Problem 2 Test: ${problem2("day${day}-p2.txt")}")
    println("Problem 2: ${problem2("day${day}.txt")}")
}