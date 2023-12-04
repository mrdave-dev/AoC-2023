package day04

// Part 1: 24m


import org.slf4j.LoggerFactory
import readInput

const val DAY = 4
val dayZeroPadded = String.format("%02d", DAY)

fun main() {
    val logger = LoggerFactory.getLogger("Day $dayZeroPadded")
    fun part1(input: List<String>): Int {
        return input.map { line ->
            val cardPieces = line.split(":", "|")
            logger.trace("Card pieces: ${cardPieces}")
            check(cardPieces.size == 3) { "Expected card pieces to equal exactly 3" }
            val winningNumbers = cardPieces[1].split(" ")
                .filter { it.trim().isNotEmpty() }
                .map(String::toInt)
            logger.trace("Winning numbers: $winningNumbers")
            val cardNumbers = cardPieces[2].split(" ")
                .filter { it.trim().isNotEmpty() }
                .map(String::toInt)
            logger.trace("Card numbers: $cardNumbers")

            cardNumbers.fold(0) { acc, n ->
                if (winningNumbers.contains(n)) {
                    logger.trace("Found winning number $n")
                    if (acc == 0) {
                        logger.trace("Accumulator is 0, return 1")
                        return@fold 1
                    } else {
                        logger.trace("Accumulator is $acc, returning ${acc * 2}")
                        return@fold acc * 2
                    }
                }

                return@fold acc
            }
        }.sum()
    }

    fun part2(input: List<String>): Int {
        // Number of winners for each card, 0-indexed
        val numberOfCardWinners = mutableMapOf<Int, Int>()

        input.forEachIndexed { cardIndex, line ->
            val cardPieces = line.split(":", "|")
            logger.trace("Card pieces: ${cardPieces}")
            check(cardPieces.size == 3) { "Expected card pieces to equal exactly 3" }
            val winningNumbers = cardPieces[1].split(" ")
                .filter { it.trim().isNotEmpty() }
                .map(String::toInt)
            logger.trace("Winning numbers: $winningNumbers")
            val cardNumbers = cardPieces[2].split(" ")
                .filter { it.trim().isNotEmpty() }
                .map(String::toInt)
            logger.trace("Card numbers: $cardNumbers")

            val numberOfWinners = cardNumbers.fold(0) { acc, n ->
                if (winningNumbers.contains(n)) {
                    logger.trace("Found winning number $n")
                    return@fold acc + 1
                }
                return@fold acc
            }

            numberOfCardWinners[cardIndex] = numberOfWinners
        }

        logger.trace("Number of card winners: $numberOfCardWinners")

        val cardInstancesWon = mutableMapOf<Int, Int>()
        val copiesToProcess = mutableListOf<Int>()

        for (cardIndex in 0 until input.size) {
            val cardWinningNumbers = numberOfCardWinners[cardIndex] ?: 0
            if (cardWinningNumbers > 0) {
                logger.debug("Original card ${cardIndex+1} won $cardWinningNumbers")
                cardInstancesWon[cardIndex] = (cardInstancesWon[cardIndex] ?: 0) + 1

                for (cardCopyIndex in cardIndex+1..cardIndex+cardWinningNumbers) {
                    logger.trace("Adding copy of ${cardCopyIndex+1}")
                    copiesToProcess.add(cardCopyIndex)
                    cardInstancesWon[cardCopyIndex] = (cardInstancesWon[cardCopyIndex] ?: 0) + 1
                }
            } else {
                logger.debug("Original card ${cardIndex+1} did not win")
                cardInstancesWon[cardIndex] = (cardInstancesWon[cardIndex] ?: 0) + 1
            }


            while (copiesToProcess.size > 0) {
                val cardCopyIndex = copiesToProcess.removeAt(0)
                val cardCopyWinners = numberOfCardWinners[cardCopyIndex] ?: 0
                logger.trace("Card copy ${cardCopyIndex+1} won $cardCopyWinners")
                if (cardCopyWinners > 0) {
                    for (n in cardCopyIndex+1..cardCopyIndex+cardCopyWinners) {
                        logger.trace("Adding card ${n+1}")
                        copiesToProcess.add(n)
                        cardInstancesWon[n] = (cardInstancesWon[n] ?: 0) + 1
                    }
                }
            }
        }

        logger.debug("Card instances map: $cardInstancesWon")

        return cardInstancesWon.values.sum()
    }

    logger.info("Advent of Code - Day $dayZeroPadded")

    val testInput1 = readInput("day$dayZeroPadded/Part1_test")
//    val testInput1Result = part1(testInput1)
//    val testInput1Expected = 13
//    logger.info("Part 1 test result: $testInput1Result")
//    check(testInput1Result == testInput1Expected) { "Expected $testInput1Result to equal $testInput1Expected"}

    val testInput2Result = part2(testInput1)
    val testInput2Expected = 30
    logger.info("Part 2 test result: $testInput2Result")
    check(testInput2Result == testInput2Expected) { "Expected $testInput2Result to equal $testInput2Expected"}

    val input = readInput("day$dayZeroPadded/Part1")
    logger.info("Part 1 solution: ${part1(input)}")
    logger.info("Part 2 solution: ${part2(input)}")
}
