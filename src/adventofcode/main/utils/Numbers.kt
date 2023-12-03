package utils

val numbers = arrayOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")

fun textToNumber(n: String): String {
    return when (n) {
        "one" -> "1"
        "two" -> "2"
        "three" -> "3"
        "four" -> "4"
        "five" -> "5"
        "six" -> "6"
        "seven" -> "7"
        "eight" -> "8"
        "nine" -> "9"
        else -> ""
    }
}

fun replaceNumbers(input: String): String {
    var s = input

    // replace merged numbers
    for (i in numbers) {
        for (j in numbers) {
            if (i.last() == j.first()) {
                s = s.replace("${i}${j.substring(1)}", "${i}${j}")
            }
        }
    }

    numbers.forEach { s = s.replace(it, textToNumber(it)) }

    return s
}