package day00

import org.slf4j.LoggerFactory
import readInput

const val DAY = 0
val dayZeroPadded = String.format("%02d", DAY)

fun main() {
    val logger = LoggerFactory.getLogger("Day $dayZeroPadded")
    fun part1(input: List<String>): Int {
        return 1
    }

    fun part2(input: List<String>): Int {
        return 2
    }

    logger.info("Advent of Code - Day $dayZeroPadded")

    val testInput1 = readInput("day$dayZeroPadded/Part1_test")
    val testInput1Result = part1(testInput1)
    val testInput1Expected = 1
    logger.info("Part 1 test result: $testInput1Result")
    check(testInput1Result == testInput1Expected) { "Expected $testInput1Result to equal $testInput1Expected"}

    val testInput2 = readInput("day$dayZeroPadded/Part2_test")
    val testInput2Result = part2(testInput2)
    val testInput2Expected = 2
    logger.info("Part 2 test result: $testInput2Result")
    check(testInput2Result == testInput2Expected) { "Expected $testInput2Result to equal $testInput2Expected"}

    val input = readInput("day$dayZeroPadded/Part1")
    logger.info("Part 1 solution: ${part1(input)}")
    logger.info("Part 2 solution: ${part2(input)}")
}
