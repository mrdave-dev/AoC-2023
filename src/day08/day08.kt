package day08

import org.slf4j.LoggerFactory
import readInput

const val DAY = 8
val dayZeroPadded = String.format("%02d", DAY)

fun main() {
    val logger = LoggerFactory.getLogger("Day $dayZeroPadded")

    class Node(
        val name: String,
        var left: String,
        var right: String,
    ) {
        override fun toString(): String {
            return "Node($name, L: $left, R: $right)"
        }
    }

    fun createNodes(nodeList: List<String>): HashMap<String, Node> {
        val nodeMap = HashMap<String, Node>()
        for (line in nodeList) {
            val nodePieces = line.split("=")
            val name = nodePieces[0].trim()
            val left = nodePieces[1].split(",")[0].substring(2)
            val right = nodePieces[1].split(",")[1]
                .let { it.substring(1, it.length - 1) }
            val node = Node(name, left, right)
            logger.trace("Node: $node")
            nodeMap[node.name] = node
        }

        return nodeMap
    }

    fun greatestCommonDivisor(a: Long, b: Long): Long {
        return if (b == 0L) a else greatestCommonDivisor(b, a % b)
    }

    fun leastCommonMultiple(a: Long, b: Long): Long {
        return a / greatestCommonDivisor(a, b) * b
    }

    fun leastCommonMultipleOfList(numbers: List<Long>): Long {
        return numbers.fold(1L) { lcm, number -> leastCommonMultiple(lcm, number) }
    }

    fun part1(input: List<String>): Int {
        val navigation = input[0].toList()
        logger.trace("Navigation: $navigation")

        val nodeMap = createNodes(input.slice(2 until input.size))

        val start = "AAA"
        val end = "ZZZ"

        var current = nodeMap[start]
        var stepsTaken = 0
        while (current!!.name != end) {
            navigation.forEach { step ->
                logger.trace("Current: $current, going $step")
                current = if (step == 'L') nodeMap[current!!.left] else nodeMap[current!!.right]
                stepsTaken += 1
                if (current!!.name == end) {
                    return stepsTaken
                }
            }
        }

        return stepsTaken
    }

    fun part2(input: List<String>): Int {
        val navigation = input[0].toList()
        logger.trace("Navigation: $navigation")

        val nodeMap = createNodes(input.slice(2 until input.size))

        val aNodes = nodeMap.keys.filter { it.endsWith("A") }
            .map { nodeMap[it]!! }
        val zNodes = nodeMap.keys.filter { it.endsWith("Z") }
            .map { nodeMap[it]!! }

        logger.trace("Nodes that end in A (${aNodes.size}): $aNodes")
        logger.trace("Nodes that end in Z (${zNodes.size}): $zNodes")

        // LCM approach:
        // get the distance to each end
        // find the lcm

        val distancesToEnd = mutableListOf<Long>()
        aNodes.forEach { startNode ->
            var stepsTaken = 0L
            var currentNode = startNode

            while (!currentNode.name.endsWith('Z')) {
                navigation.forEach { step ->
                    logger.trace("Current: $currentNode, going $step")
                    currentNode = if (step == 'L') {
                        nodeMap[currentNode.left]!!
                    } else {
                        nodeMap[currentNode.right]!!
                    }
                    stepsTaken += 1
                    if (currentNode.name.endsWith('Z')) {
                        distancesToEnd.add(stepsTaken)
                        return@forEach
                    }
                }
            }
        }
        logger.debug("Distances to each end: $distancesToEnd")
        logger.debug("Long value: ${leastCommonMultipleOfList(distancesToEnd)}")

        return leastCommonMultipleOfList(distancesToEnd).toInt()
    }

    logger.info("Advent of Code - Day $dayZeroPadded")

    val testInput1 = readInput("day$dayZeroPadded/Part1_test")
    val testInput1Result = part1(testInput1)
    val testInput1Expected = 2
    logger.info("Part 1 test result: $testInput1Result")
    check(testInput1Result == testInput1Expected) { "Expected $testInput1Result to equal $testInput1Expected"}

    val testInput2 = readInput("day$dayZeroPadded/Part2_test")
    val testInput2Result = part2(testInput2)
    val testInput2Expected = 6
    logger.info("Part 2 test result: $testInput2Result")
    check(testInput2Result == testInput2Expected) { "Expected $testInput2Result to equal $testInput2Expected"}

    val input = readInput("day$dayZeroPadded/Part1")
//    logger.info("Part 1 solution: ${part1(input)}")
    logger.info("Part 2 solution: ${part2(input)}")

    // too low: 2115497795
}
