package day05

// Part 1 done in 1hr 10m

import org.slf4j.LoggerFactory
import readInput

const val DAY = 5
val dayZeroPadded = String.format("%02d", DAY)

fun main() {
    val logger = LoggerFactory.getLogger("Day $dayZeroPadded")
    fun part1(input: List<String>): Long {

        /**
         * Order:
         * 1. Seed to soil
         * 2. Soil to fert
         * 3. Fert to water
         * 4. Water to light
         * 5. Light to temp
         * 6. Temp to hum
         * 7. Hum to location
         *
         * [destination, source, range]
         */
        val almanacPieces = input.fold(mutableListOf<MutableList<MutableList<Long>>>()) { acc, line ->
            if (line.isEmpty()) {
                acc.add(mutableListOf())
            } else if (line[0].isDigit()) {
                acc.last()
                    .add(line.split(" ")
                        .map(String::toLong)
                        .toMutableList())
            }
            acc
        }

        for (i in 1 until almanacPieces.size) {
            almanacPieces[i].sortBy{ it.getOrNull(1) }
        }

        logger.trace("Almanac pieces: $almanacPieces")

        val seeds = "\\d+".toRegex()
            .findAll(input[0])
            .map { it.value.toLong() }
            .toList()

        logger.trace("Seeds: $seeds")

        val seedConfigurations = seeds.map { seed ->
            val config = mutableListOf(seed)
            for (i in 0 until almanacPieces.size) {
                logger.debug("Seed $seed source $i: ${config.last()}")
                val rangeMatch = almanacPieces[i].firstOrNull {
                    val rangeStartsBeforeSource = it[1] <= config.last()
                    val rangeEndsAfterSource = it[1] + it[2] > config.last()
                    logger.trace("$it: $rangeStartsBeforeSource && $rangeEndsAfterSource")
                    rangeStartsBeforeSource && rangeEndsAfterSource
                }
                if (rangeMatch != null) {
                    // TODO offset
                    logger.trace("Range match found: $rangeMatch")
                    val rangeOffset = config.last() - rangeMatch[1]
                    logger.trace("Range offset: $rangeOffset")
                    config.add(rangeMatch[0] + rangeOffset)
                } else {
                    logger.trace("No range match found")
                    config.add(config.last())
                }
            }
            config
        }.sortedBy {
            it.last()
        }

        logger.debug("Seed configurations: $seedConfigurations")

        return seedConfigurations.first().last()
    }

    fun part2(input: List<String>): Long {
        val almanacPieces = input.fold(mutableListOf<MutableList<MutableList<Long>>>()) { acc, line ->
            if (line.isEmpty()) {
                acc.add(mutableListOf())
            } else if (line[0].isDigit()) {
                acc.last()
                    .add(line.split(" ")
                        .map(String::toLong)
                        .toMutableList())
            }
            acc
        }

        for (i in 1 until almanacPieces.size) {
            almanacPieces[i].sortBy{ it.getOrNull(1) }
        }

        logger.trace("Almanac pieces: $almanacPieces")

        val seedRanges = "\\d+".toRegex()
            .findAll(input[0])
            .windowed(2, 2, partialWindows = false)
            .map { listOf(it[0].value.toLong(), it[1].value.toLong()) }
            .toMutableList()

        /**
         * @STARTHERE
         * Ok idk wtf to do here. I'm going to save this work for now.
         * I think I need to restart this code and make better data classes
         * to make it more obvious what I'm doing.
         */

        /**
         * Get the seed ranges
         * Get the location groups
         * Calculate the location for each seed range:
         * * for each garden range
         *   * if no intersection, return input
         *   * if intersection, return [intersection start + offset, intersection end + offset], start intersection, end intersection
         *
         */

        logger.trace("Seeds ranges: $seedRanges")

        fun getRanges(almanacGroups: MutableList<MutableList<MutableList<Long>>>,
                      range: List<Long>): List<List<List<Long>>> {
            val config = mutableListOf(range)
            // For each almanac group
            for (i in 0 until almanacGroups.size) {
                val lastConfig = config.last()
                // for each range in the group
                val additionalRanges = mutableListOf<List<Long>>()
                val hasIntersection = almanacGroups[i].firstOrNull {
                    // If the almanac range starts after the range ends
                    val rangeStartsAfterEnd = lastConfig[0] >= it[1] + it[2]
                    // Or the almanac range ends before the range starts
                    val rangeEndsBeforeStart = lastConfig[0] + lastConfig[1] <= it[1]
                    // There's no intersection
                    if (rangeStartsAfterEnd || rangeEndsBeforeStart) {
                        false
                    } else {
                        true
//
                    }
                }

                if (hasIntersection != null) {
                    val intersection = listOf(
                        Math.max(lastConfig[0], hasIntersection[1]),
                        Math.min(lastConfig[0] + lastConfig[1], hasIntersection[1] + hasIntersection[2])
                    )
                    val rangeOffset = config.last()[0] - hasIntersection[1]
                    config.add(listOf(hasIntersection[0] + rangeOffset, hasIntersection[1] + rangeOffset))
                    logger.trace("Range intersection: $intersection")
                    if (lastConfig[0] < intersection[0]) {
                        val startIntersection = listOf(lastConfig[0], intersection[0])
                        logger.trace("Start intersection: $startIntersection")
                        additionalRanges.add(startIntersection)
                        getRanges(almanacGroups.subList(i+1, almanacGroups.size), startIntersection)
                    }
                    if (lastConfig[0] + lastConfig[1] > intersection[1]) {
                        val endIntersection = listOf(intersection[1], lastConfig[1] + lastConfig[1])
                        logger.trace("End intersection: $endIntersection")
                        additionalRanges.add(endIntersection)
                        getRanges(almanacGroups.subList(i+1, almanacGroups.size), endIntersection)
                    }
                } else {
                    config.add(range)
                }
            }

            logger.debug("Config: $config")
            return listOf(config)
        }

        seedRanges.map { seedRange ->
            logger.trace("Seed range: ${seedRange[0]} ${seedRange[1]}")
            val x = getRanges(almanacPieces, listOf(seedRange[0], seedRange[0]+seedRange[1]))
            logger.trace("x: $x")
        }

        while (seedRanges.isNotEmpty()) {
            val seedRange = seedRanges.removeAt(0)
            // Start with the initial seed range; source is same as dest
            val seedRangesToProcess = mutableListOf(listOf(seedRange[0], seedRange[1]))

            while (seedRangesToProcess.isNotEmpty()) {
                var range = seedRangesToProcess.removeAt(0)
                logger.trace("Processing range $range")

                // For each almanac group
                for (i in 0 until almanacPieces.size) {
                     // for each range in the group
                    for (j in 0 until almanacPieces[i].size) {
                        val intersection = almanacPieces[i][j].let {
                            // If the almanac range starts after the range ends
                            val rangeStartsAfterEnd = range[0] >= it[1] + it[2]
                            // Or the almanac range ends before the range starts
                            val rangeEndsBeforeStart = range[0] + range[1] <= it[1]
                            // There's no intersection
                            if (rangeStartsAfterEnd || rangeEndsBeforeStart) {
                                null
                            } else {
                                listOf(Math.max(range[0], it[1]), Math.min(range[0] + range[1], it[1] + it[2]))
                            }
                        }

                        logger.trace("Range intersection: $intersection")

                        if (intersection != null) {
                            // Check the start intersection
                            if (range[0] < intersection[0]) {
                                val x = listOf(range[0], intersection[0])
                                logger.trace("Start intersection: $x")
                            }
                            if (range[0] + range[1] > intersection[1]) {
                                val x = listOf(intersection[1], range[1] + range[1])
                                logger.trace("End intersection: $x")
                            }
                        }
                    }


//                    val rangeMatch = almanacPieces[i].firstOrNull {
//                        val startInRange = it[1] <= range[1] && range[1] < (it[1] + it[2])
//                        val endInRange = it[1] <= (range[1] + range[2]) && (range[1] + range[2]) < (it[1] + it[2])
//                        val rangeSurrounds = range[1] <= it[1] && (it[1] + it[2]) < (range[1] + range[2])
//                        logger.trace("Start, end, surrounds $it: $startInRange $endInRange $rangeSurrounds")
//                        startInRange || endInRange || rangeSurrounds
//                    }
//
//                    logger.trace("Range match found: $rangeMatch")
//
//                    if (rangeMatch != null) {
//                        val startInRange = rangeMatch[1] <= range[1] && range[1] < (rangeMatch[1] + rangeMatch[2])
//                        val endInRange = rangeMatch[1] <= (range[1] + range[2]) && (range[1] + range[2]) < (rangeMatch[1] + rangeMatch[2])
//
//                        if (startInRange && endInRange) {
//                            logger.trace("Full match found")
//                            config.add(rangeMatch)
//                        }
//
//                        // Split the ranges
//
//                        // e.g.
//                        // range:   [10, 20, 10] (ends at 30)
//                        // match:   [15, 15, 10] (ends at 25)
//                        // result:  [15, 20, 5]  (ends at 25)
//                        // added:   [25, 25, 5]  (ends at 30)
//
//
//
//                        if (!startInRange && endInRange) {
//                            logger.trace("Range starts before match, but ends in match")
//                            val rangeToSplit = config.removeAt(config.size - 1)
//                            val startsDifference = range[1] - rangeMatch[1]
//                            config.add(listOf(
//                                rangeMatch[0] + startsDifference,
//                                rangeMatch[1] + startsDifference,
//                                startsDifference))
//                            logger.trace("Added to config: ${config.last()}")
//                            seedRangesToProcess.add(listOf(
//                                range[0] + startsDifference,
//                                range[1] + startsDifference,
//                                startsDifference))
//                            logger.trace("Added to seed ranges to process: ${seedRangesToProcess.last()}")
//
//                        }
//
//                        if (startInRange && !endInRange) {
//                            logger.trace("Range starts in match, but goes beyond")
//
//                        }
//
//                        logger.trace("Range surrounds")
//
//                    } else {
//                        config.add(range)
//                    }
//
//                    logger.trace("Config: $config")
                }
            }
            // e.g. [79, 79, 14]
            // For each map


        }

        /**
        val seedConfigurations = seedRanges.map { seedRange ->
            logger.trace("Seed range: $seedRange")
            for (i in 0 until almanacPieces.size) {
                // If the first number is in range
                  // Get last number in range
                    // If fully in range, add to config
                  // Create new seed mapping with first number not in range and remaining range
                // Add config for straight crossover


                logger.debug("Seed $seed source $i: ${config.last()}")
                val rangeMatch = almanacPieces[i].firstOrNull {
                    val rangeStartsBeforeSource = it[1] <= config.last()
                    val rangeEndsAfterSource = it[1] + it[2] > config.last()
                    logger.trace("$it: $rangeStartsBeforeSource && $rangeEndsAfterSource")
                    rangeStartsBeforeSource && rangeEndsAfterSource
                }
                if (rangeMatch != null) {
                    // TODO offset
                    logger.trace("Range match found: $rangeMatch")
                    val rangeOffset = config.last() - rangeMatch[1]
                    logger.trace("Range offset: $rangeOffset")
                    config.add(rangeMatch[0] + rangeOffset)
                } else {
                    logger.trace("No range match found")
                    config.add(config.last())
                }
            }
        }

        val sortedConfigurations = seedConfigurations.sortedBy {
            it.last()
        }

        logger.debug("Seed configurations: ${sortedConfigurations.subList(0, 10)}")
        */

        return 0L
    }

    logger.info("Advent of Code - Day $dayZeroPadded")

    val testInput1 = readInput("day$dayZeroPadded/Part1_test")
//    val testInput1Result = part1(testInput1)
//    val testInput1Expected = 35.toLong()
//    logger.info("Part 1 test result: $testInput1Result")
//    check(testInput1Result == testInput1Expected) { "Expected $testInput1Result to equal $testInput1Expected"}

//    val testInput2 = readInput("day$dayZeroPadded/Part2_test")
    val testInput2Result = part2(testInput1)
    val testInput2Expected = 46.toLong()
    logger.info("Part 2 test result: $testInput2Result")
    check(testInput2Result == testInput2Expected) { "Expected $testInput2Result to equal $testInput2Expected"}

    val input = readInput("day$dayZeroPadded/Part1")
//    logger.info("Part 1 solution: ${part1(input)}")
//    logger.info("Part 2 solution: ${part2(input)}")
}
