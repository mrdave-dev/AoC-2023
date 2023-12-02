package day02

import org.slf4j.LoggerFactory
import readInput

const val DAY = 2
val dayZeroPadded = String.format("%02d", DAY)

fun main() {
    val logger = LoggerFactory.getLogger("Day $dayZeroPadded")
    fun part1(input: List<String>, contains: Map<String, Int>): Int {
        return input.map {
            val (gameString, setsString) = it.split(":")
            logger.debug("Game string: $gameString")
            logger.debug("Sets string: $setsString")

            val game = gameString.substring(5, gameString.length).toInt()
            logger.debug("Game int parsed: $game")

            val sets = setsString.split(";")
                .map {
                    set -> set.split(",")
                    .map { amtAndColorString ->
                        val (amt, color) = amtAndColorString.trim().split(" ")
                        Pair(amt.toInt(), color)
                    }
                }
            logger.debug("$sets")

            // Iterate over each set and each peek
            // If the amount of any color exceeds the contains color, return 0
            // Otherwise, return the game index
            sets.forEach {
                set -> set.forEach {(amt, color) ->
                    if (amt > contains[color]!!) {
                        return@map 0
                    }
                }
            }
            game
        }.sum()
    }

    fun part2(input: List<String>): Int {
        return input.map {
            val (gameString, setsString) = it.split(":")
            logger.debug("Game string: $gameString")
            logger.debug("Sets string: $setsString")

            val game = gameString.substring(5, gameString.length).toInt()
            logger.debug("Game int parsed: $game")

            val sets = setsString.split(";")
                .map {
                        set -> set.split(",")
                    .map { amtAndColorString ->
                        val (amt, color) = amtAndColorString.trim().split(" ")
                        Pair(amt.toInt(), color)
                    }
                }
            logger.debug("$sets")

            val highestAmountEncountered = mutableMapOf(
                "red"   to Int.MIN_VALUE,
                "green" to Int.MIN_VALUE,
                "blue"  to Int.MIN_VALUE,
            )

            sets.forEach {
                set -> set.forEach {
                    (amt, color) ->
                    if (amt > highestAmountEncountered[color]!!) {
                        highestAmountEncountered[color] = amt
                    }
                }
            }

            logger.debug("Highest amounts encountered: $highestAmountEncountered")
            highestAmountEncountered
        }.map { encounterMap ->
            encounterMap["red"]!!.coerceAtLeast(1) *
                    encounterMap["green"]!!.coerceAtLeast(1) *
                    encounterMap["blue"]!!.coerceAtLeast(1)
        }.sum()
    }

    logger.info("Advent of Code - Day $dayZeroPadded")

    val testInput1 = readInput("day$dayZeroPadded/Part1_test")
    val testInputContains1 = mapOf(
        "red"   to 12,
        "green" to 13,
        "blue"  to 14,
    )
    val testInput1Result = part1(testInput1, testInputContains1)
    val testInput1Expected = 8
    logger.info("Part 1 test result: $testInput1Result")
    check(testInput1Result == testInput1Expected) { "Expected $testInput1Result to equal $testInput1Expected"}

    val testInput2 = readInput("day$dayZeroPadded/Part2_test")
    val testInput2Result = part2(testInput2)
    val testInput2Expected = 2286
    logger.info("Part 2 test result: $testInput2Result")
    check(testInput2Result == testInput2Expected) { "Expected $testInput2Result to equal $testInput2Expected"}

    val input = readInput("day$dayZeroPadded/Part1")
    val inputContains = mapOf(
        "red"   to 12,
        "green" to 13,
        "blue"  to 14,
    )
    logger.info("Part 1 solution: ${part1(input, inputContains)}")
    logger.info("Part 2 solution: ${part2(input)}")
}
