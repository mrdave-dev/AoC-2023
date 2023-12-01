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


    val testInput1 = readInput("day$dayZeroPadded/Part1_test")
    check(part1(testInput1) == 1)


    val testInput2 = readInput("day$dayZeroPadded/Part2_test")
    check(part2(testInput2) == 2)

    val input = readInput("day$dayZeroPadded/Part1")
    logger.info("Part 1 solution: ${part1(input)}")
    logger.info("Part 2 solution: ${part2(input)}")
}
