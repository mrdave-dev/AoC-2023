import org.slf4j.LoggerFactory

fun main() {
    val logger = LoggerFactory.getLogger("Day 01")
    fun part1(input: List<String>): Int {
        // Get all the numeric characters in the line
        // Pick the first and the last, make a concat number
        // Sum all numbers

        return input.map { line ->
            val lineNumbers = line.filter { it.isDigit() }
            logger.trace("Line numbers: $lineNumbers")
            val concatNumber = "${lineNumbers.first()}${lineNumbers.last()}".toInt()
            logger.debug("Number created: $concatNumber")
            concatNumber
        }.sum()
    }

    fun part2(input: List<String>): Int {
        // Create a map of words to digits
        // Iterate through each line and character
        // If character is a number, add it to the list
        // If character starts with any of the first letters from the map...
          // iterate over the map and check if the next substring is a match for the word
            // if true, add digit
        // take first and last digits
        // sum

        val DIGIT_WORDS = mapOf(
            "one"       to 1,
            "two"       to 2,
            "three"     to 3,
            "four"      to 4,
            "five"      to 5,
            "six"       to 6,
            "seven"     to 7,
            "eight"     to 8,
            "nine"      to 9,
        )

        val startingLetters = DIGIT_WORDS.keys.map { it.first() }.toSet()

        return input.sumOf { line ->
            val lineNumbers = line.mapIndexed { idx, c ->
                 logger.trace("c: $c")
                if (c.isDigit()) {
                    c - '0'
                } else if (startingLetters.contains(c)) {
                    logger.debug("Starting letter found $c")
                    DIGIT_WORDS.entries.firstOrNull { (word, digit) ->
                        word == line.substring(idx, Math.min(idx + word.length, line.length))
                    }.let { it?.value } // do this better
                } else {
                    null
                }
            }.filterNotNull()

            val concatNumber = "${lineNumbers.first()}${lineNumbers.last()}".toInt()
            logger.debug("Line numbers $lineNumbers")
            concatNumber
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)


    val testInput2 = readInput("Day01_Part02_test")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    logger.info("Part 1 solution: ${part1(input)}")
    logger.info("Part 2 solution: ${part2(input)}")
}
