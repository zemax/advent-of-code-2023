package utils

fun getResourceAsText(path: String): String = object {}.javaClass.getResource("/${path}")?.readText() ?: ""

fun getResourceAsLines(path: String): List<String> = getResourceAsText(path).split("\n")
