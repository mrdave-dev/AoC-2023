package day06

import org.slf4j.LoggerFactory
import readInput
import java.math.BigInteger
import kotlin.time.times

const val DAY = 6
val dayZeroPadded = String.format("%02d", DAY)

fun main() {
    val logger = LoggerFactory.getLogger("Day $dayZeroPadded")

    data class Record (
        val time: Int,
        val distance: Int,
    )

    data class BigIntRecord (
        val time: BigInteger,
        val distance: BigInteger,
    )

    class BigIntegerRange(private val endInclusive: BigInteger): Iterable<BigInteger> {
        override fun iterator(): Iterator<BigInteger> = object : Iterator<BigInteger> {
            var current = BigInteger.ZERO

            override fun hasNext(): Boolean = current <= endInclusive
            override fun next(): BigInteger {
                if (!hasNext()) throw NoSuchElementException()
                return current++
            }
        }
    }

    fun getDistanceForTime(secondsPressed: Int, timeAvailable: Int): Int {
        return secondsPressed * (timeAvailable - secondsPressed)
    }

    fun getDistanceForTime(secondsPressed: BigInteger, timeAvailable: BigInteger): BigInteger {
        return secondsPressed.times(timeAvailable.minus(secondsPressed))
    }

    fun getPossibleWinScenarios(timeAvailable: Int, distanceToBeat: Int): List<Int> {
        val buttonPressTimeForWin = mutableListOf<Int>()
        for (buttonPressTime in 0..timeAvailable) {
            val distanceForTime = getDistanceForTime(buttonPressTime, timeAvailable)
            logger.trace("Button press for $buttonPressTime with $timeAvailable available: $distanceForTime")
            if (distanceForTime > distanceToBeat) {
                buttonPressTimeForWin.add(buttonPressTime)
            }
        }
        return buttonPressTimeForWin.toList()
    }

    fun getPossibleWinScenarios(timeAvailable: BigInteger, distanceToBeat: BigInteger): List<BigInteger> {
        val buttonPressTimeForWin = mutableListOf<BigInteger>()
        for (buttonPressTime in BigIntegerRange(timeAvailable)) {
            val distanceForTime = getDistanceForTime(buttonPressTime, timeAvailable)
            logger.trace("Button press for $buttonPressTime with $timeAvailable available: $distanceForTime")
            if (distanceForTime > distanceToBeat) {
                buttonPressTimeForWin.add(buttonPressTime)
            }
        }
        return buttonPressTimeForWin.toList()
    }

    fun part1(input: List<String>): Int {
        val recordPieces = input.map {
            it.split(":")[1]
                .split("\\s+".toRegex())
                .filter { it.isNotBlank() }
        }

        val records = recordPieces.let {
            val acc = mutableListOf<Record>()
            for (i in 0 until it[0].size) {
                acc.add(Record(it[0][i].toInt(), it[1][i].toInt()))
            }
            acc
        }

        logger.trace("Records: $records")

        return records.fold(1) { acc, record ->
            logger.trace("Processing $record")
            acc * getPossibleWinScenarios(record.time, record.distance).size
        }
    }

    fun part2(input: List<String>): Int {
        val recordPieces = input.map {
            it.split(":")[1]
                .split("\\s+".toRegex())
                .filter { it.isNotBlank() }
        }

        val records = recordPieces.let {
            val acc = mutableListOf<BigIntRecord>()
            for (i in 0 until it[0].size) {
                acc.add(BigIntRecord(BigInteger(it[0][i]), BigInteger(it[1][i])))
            }
            acc
        }

        logger.debug("Records: $records")

        return getPossibleWinScenarios(records[0].time, records[0].distance).size
    }

    logger.info("Advent of Code - Day $dayZeroPadded")

//    val testInput1 = readInput("day$dayZeroPadded/Part1_test")
//    val testInput1Result = part1(testInput1)
//    val testInput1Expected = 288
//    logger.info("Part 1 test result: $testInput1Result")
//    check(testInput1Result == testInput1Expected) { "Expected $testInput1Result to equal $testInput1Expected"}

//    val testInput2 = readInput("day$dayZeroPadded/Part2_test")
//    val testInput2Result = part2(testInput2)
//    val testInput2Expected = 2
//    logger.info("Part 2 test result: $testInput2Result")
//    check(testInput2Result == testInput2Expected) { "Expected $testInput2Result to equal $testInput2Expected"}

    val input = readInput("day$dayZeroPadded/Part1")
    val input2 = readInput("day$dayZeroPadded/Part2")
    logger.info("Part 1 solution: ${part1(input)}")
    logger.info("Part 2 solution: ${part2(input2)}")
}
