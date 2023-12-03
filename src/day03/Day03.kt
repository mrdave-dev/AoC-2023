package day03

// Time it took me: 1hr 22m

import org.slf4j.LoggerFactory
import readInput
import java.util.HashMap

const val DAY = 3
val dayZeroPadded = String.format("%02d", DAY)

fun main() {
    val logger = LoggerFactory.getLogger("Day $dayZeroPadded")

    // Checks for digits in a string forward and backward
    // Returns x,y position for start of number and number
    fun getNumberAtPosition(input: List<String>,
                            position: Pair<Int, Int>): Pair<Pair<Int, Int>, Int> {
        val (x, y) = position
        logger.trace("Getting number at position $x, $y")
        if (!input[y][x].isDigit()) {
            throw Error("Expected '${input[y][x]}' at $x, $y to be digit")
        }

        val digits = mutableListOf<Char>()

        var lowestX = x

        logger.trace("Checking backward from position")
        var nX = x
        while (nX > -1 && input[y][nX].isDigit()) {
            logger.trace("Digit found at $nX, $y: ${input[y][nX]}")
            digits.add(0, input[y][nX])
            lowestX = nX
            nX -= 1
        }

        logger.trace("Checking forward from position")
        nX = x + 1
        while (nX < input[y].length && input[y][nX].isDigit()) {
            logger.trace("Digit found at $nX, $y: ${input[y][nX]}")
            digits.add(input[y][nX])
            nX += 1
        }

        val n = digits.joinToString("").toInt()
        logger.debug("Number found from start position $lowestX, $y: $n")
        return Pair(Pair(lowestX, y), n)
    }



    fun findNeighborNumbers(input: List<String>,
                            position: Pair<Int, Int>
        // Return item is a List of Pairs.
        // The first item is the x,y cords of the number's starting digit
        // The second item is the number
    ): List<Pair<Pair<Int, Int>, Int>> {
        val (x, y) = position
        val foundNumbers = mutableListOf<Pair<Pair<Int, Int>, Int>> ()
        // Checking N
        try {
            foundNumbers.add(getNumberAtPosition(input, Pair(x, y-1)))
        } catch (e: Error) {
            logger.trace("Error at $x, ${y-1} swallowed: ${e.message}")
        }

        // Checking NE
        try {
            foundNumbers.add(getNumberAtPosition(input, Pair(x+1, y-1)))
        } catch (e: Error) {
            logger.trace("Error at ${x + 1}, ${y-1} swallowed: ${e.message}")
        }

        // Checking E
        try {
            foundNumbers.add(getNumberAtPosition(input, Pair(x+1, y)))
        } catch (e: Error) {
            logger.trace("Error at ${x + 1}, $y swallowed: ${e.message}")
        }

        // Checking SE
        try {
            foundNumbers.add(getNumberAtPosition(input, Pair(x+1, y+1)))
        } catch (e: Error) {
            logger.trace("Error at ${x + 1}, ${y+1} swallowed: ${e.message}")
        }

        // Checking S
        try {
            foundNumbers.add(getNumberAtPosition(input, Pair(x, y+1)))
        } catch (e: Error) {
            logger.trace("Error at ${x}, ${y+1} swallowed: ${e.message}")
        }

        // Checking SW
        try {
            foundNumbers.add(getNumberAtPosition(input, Pair(x-1, y+1)))
        } catch (e: Error) {
            logger.trace("Error at ${x-1}, ${y+1} swallowed: ${e.message}")
        }

        // Checking W
        try {
            foundNumbers.add(getNumberAtPosition(input, Pair(x-1, y)))
        } catch (e: Error) {
            logger.trace("Error at ${x-1}, $y swallowed: ${e.message}")
        }

        // Checking NW
        try {
            foundNumbers.add(getNumberAtPosition(input, Pair(x-1, y-1)))
        } catch (e: Error) {
            logger.trace("Error at ${x-1}, ${y-1} swallowed: ${e.message}")
        }

        logger.debug("Number neighbors found from position {}, {}: {}", x, y, foundNumbers)

        return foundNumbers
    }

    fun part1(input: List<String>): Int {
        // Visited numbers are kept track by their STARTING digit's position in the x, y cords
        val visitedNumbers = HashMap<Pair<Int, Int>, Int>()

        // Iterate and find each *
        // For each star, check adjacent
        // For each point in the number, add to a map of visited points (x,y)
        // If the number has been visited, don't
        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                logger.trace("At ($x, $y): $char")
                if (!char.isDigit() && char != '.') {
                    logger.debug("Found symbol '$char' at $x, $y")
                    val neighbors = findNeighborNumbers(input, Pair(x, y))
                    neighbors.forEach {
                        val (position, value) = it
                        visitedNumbers[position] = value
                    }
                }
            }
        }

        logger.debug("Visited numbers: {}", visitedNumbers)

        return visitedNumbers.values.sum()
    }



    fun part2(input: List<String>): Int {
        var sum = 0
        input.forEachIndexed { y, line ->
        println("xxx")
            line.forEachIndexed { x, char ->
                logger.trace("At ($x, $y): $char")
                if (!char.isDigit() && char == '*') {
                    logger.debug("Found symbol '$char' at $x, $y")
                    val neighbors = findNeighborNumbers(input, Pair(x, y))
                    val neighborsSet = HashMap<Pair<Int, Int>, Int>()
                    neighbors.forEach {
                        val (position, value) = it
                        neighborsSet[position] = value
                    }
                    if (neighborsSet.size == 2) {
                        sum += neighborsSet.values.reduce { acc, i -> acc * i }
                    }
                }
            }
        }
        return sum
    }

    logger.info("Advent of Code - Day $dayZeroPadded")

    val testInput1 = readInput("day$dayZeroPadded/Part1_test")

    // getNumberAtPosition(testInput1, Pair(1, 0))

    val testInput1Result = part1(testInput1)
    val testInput1Expected = 4361
    logger.info("Part 1 test result: $testInput1Result")
    check(testInput1Result == testInput1Expected) { "Expected $testInput1Result to equal $testInput1Expected"}

    val testInput2 = readInput("day$dayZeroPadded/Part1_test")
    val testInput2Result = part2(testInput2)
    val testInput2Expected = 467835
    logger.info("Part 2 test result: $testInput2Result")
    check(testInput2Result == testInput2Expected) { "Expected $testInput2Result to equal $testInput2Expected"}

    val input = readInput("day$dayZeroPadded/Part1")
    logger.info("Part 1 solution: ${part1(input)}")
    logger.info("Part 2 solution: ${part2(input)}")
}
