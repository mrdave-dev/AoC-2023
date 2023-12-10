package day07

import org.slf4j.LoggerFactory
import readInput

const val DAY = 7
val dayZeroPadded = String.format("%02d", DAY)

fun main() {
    val logger = LoggerFactory.getLogger("Day $dayZeroPadded")

    val cards = listOf('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2')
    val wildcardCards = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    class Hand(
        val hand: String,
        val bid: Int,
    ) {
        /**
         * 7 - Five of a kind
         * 6 - Four of a kind
         * 5 - Full house
         * 4 - Three of a kind
         * 3 - Two pair
         * 2 - One pair
         * 1 - High card
         */
        fun getStrength(): Int {
            val cardMap = mutableMapOf<Char, Int>()
            hand.forEach { cardMap[it] = cardMap.getOrDefault(it, 0) + 1 }

            logger.trace("Card map: $cardMap")

            // Five of a kind
            if (cardMap.size == 1) { return 7 }

            val cardsSortedByFrequency = cardMap.entries
                .sortedBy { it.value }
                .reversed()

            val mostFrequentCardEntry = cardsSortedByFrequency.first()

            // Four of a kind
            if (mostFrequentCardEntry.value == 4) {
                return 6
            }

            // Full house
            if (mostFrequentCardEntry.value == 3 && cardMap.size == 2) {
                return 5
            }

            // Three of a kind
            if (mostFrequentCardEntry.value == 3) {
                return 4
            }

            if (cardsSortedByFrequency[0].value == 2 && cardsSortedByFrequency[1].value == 2) {
                return 3
            }

            if (cardsSortedByFrequency[0].value == 2) {
                return 2
            }

            return 1
        }

        // TODO May be unnecessary
        fun getHighCard(): Char {
            return hand.reduce { acc, c ->
                return if (cards.indexOf(acc) > cards.indexOf(c)) {
                    acc
                } else {
                    c
                }
            }
        }

        /**
         * Returns true if this hand beats the input
         */
        fun winsAgainst(opposingHand: Hand): Boolean {
            val thisHandStrength = getStrength()
            val opposingHandStrength = opposingHand.getStrength()
            if (thisHandStrength > opposingHandStrength) {
                return true
            } else if (thisHandStrength < opposingHandStrength) {
                return false
            } else {
                // Hand strengths are equal
                for (n in hand.indices) {
                    val thisCardIndex = cards.indexOf(hand[n])
                    val opposingCardIndex = cards.indexOf(opposingHand.hand[n])
                    if (thisCardIndex > opposingCardIndex) {
                        return true
                    } else if (thisCardIndex < opposingCardIndex) {
                        return false
                    }
                }
            }
            throw Error("Hand did not fall in to decisive category")
        }

        override fun toString(): String {
            return "Hand(hand=$hand, bid=$bid, strength=${getStrength()})"
        }
    }

    class WildcardHand(
        val hand: String,
        val bid: Int,
    ) {
        /**
         * 7 - Five of a kind
         * 6 - Four of a kind
         * 5 - Full house
         * 4 - Three of a kind
         * 3 - Two pair
         * 2 - One pair
         * 1 - High card
         */
        fun getStrength(): Int {
            val cardMap = mutableMapOf<Char, Int>()
            hand.forEach { cardMap[it] = cardMap.getOrDefault(it, 0) + 1 }

            val numberOfWildcards = cardMap.getOrDefault('J', 0 )

            logger.trace("Card map: $cardMap")

            logger.trace("xxx 1")
            // Five of a kind
            if (cardMap.size == 1) { return 7 }


            val cardsSortedByFrequency = cardMap.entries
                .sortedBy { it.value }
                .reversed()
                .toList()

            val mostFrequentCardEntry = cardsSortedByFrequency.first { it.key != 'J' }

            logger.trace("xxx ${mostFrequentCardEntry.value} + $numberOfWildcards = ${mostFrequentCardEntry.value + numberOfWildcards}" )
            if (mostFrequentCardEntry.value + numberOfWildcards == 5) {
                return 7
            }

            // Four of a kind:
            if (mostFrequentCardEntry.value == 4) {
                return 6
            }
            if (mostFrequentCardEntry.value + numberOfWildcards == 4) {
                return 6
            }

            // Full house
            if (mostFrequentCardEntry.value == 3 && cardMap.size == 2) {
                return 5
            }
            val secondMostFrequentCardEntry = cardsSortedByFrequency.getOrNull(1)?.value ?: 0
            if (mostFrequentCardEntry.value + secondMostFrequentCardEntry + numberOfWildcards == 5) {
                return 5
            }

            // Three of a kind
            if (mostFrequentCardEntry.value == 3) {
                return 4
            }
            if (mostFrequentCardEntry.value + numberOfWildcards == 3) {
                return 4
            }

            // Two pair
            if (cardsSortedByFrequency[0].value == 2 && cardsSortedByFrequency[1].value == 2) {
                return 3
            }
            if (mostFrequentCardEntry.value + numberOfWildcards == 4) {
                return 3
            }

            // One pair
            if (cardsSortedByFrequency[0].value == 2) {
                return 2
            }
            if (numberOfWildcards == 1) {
                return 2
            }

            return 1
        }

        // TODO May be unnecessary
        fun getHighCard(): Char {
            return hand.reduce { acc, c ->
                return if (wildcardCards.indexOf(acc) > wildcardCards.indexOf(c)) {
                    acc
                } else {
                    c
                }
            }
        }

        /**
         * Returns true if this hand beats the input
         */
        fun winsAgainst(opposingHand: WildcardHand): Boolean {
            val thisHandStrength = getStrength()
            val opposingHandStrength = opposingHand.getStrength()
            if (thisHandStrength > opposingHandStrength) {
                return true
            } else if (thisHandStrength < opposingHandStrength) {
                return false
            } else {
                // Hand strengths are equal
                for (n in hand.indices) {
                    val thisCardIndex = wildcardCards.indexOf(hand[n])
                    val opposingCardIndex = wildcardCards.indexOf(opposingHand.hand[n])
                    if (thisCardIndex > opposingCardIndex) {
                        return true
                    } else if (thisCardIndex < opposingCardIndex) {
                        return false
                    }
                }
            }
            throw Error("Hand did not fall in to decisive category")
        }

        override fun toString(): String {
            return "WildcardHand(hand=$hand, bid=$bid, strength=${getStrength()})"
        }
    }

    fun part1(input: List<String>): Int {
        val hands = input.map {
            val (hand, bid) = it.split(" ")
            Hand(hand, bid.toInt())
        }
            .toMutableList()
        hands.sortWith { h1, h2 ->
                if (h1.getStrength() != h2.getStrength()) {
                    h1.getStrength().compareTo(h2.getStrength())
                } else {
                    if (h1.winsAgainst(h2)) -1 else 1
                }
            }

        logger.debug("Hands: $hands")

        return hands.foldIndexed(0) { i, acc, h ->
            acc + ((i+1) * h.bid)
        }
    }

    fun part2(input: List<String>): Int {
        val hands = input.map {
            val (hand, bid) = it.split(" ")
            WildcardHand(hand, bid.toInt())
        }
            .toMutableList()
        hands.sortWith { h1, h2 ->
            if (h1.getStrength() != h2.getStrength()) {
                h1.getStrength().compareTo(h2.getStrength())
            } else {
                if (h1.winsAgainst(h2)) -1 else 1
            }
        }

        logger.debug("Hands: $hands")

        return hands.foldIndexed(0) { i, acc, h ->
            acc + ((i+1) * h.bid)
        }
    }

    logger.info("Advent of Code - Day $dayZeroPadded")

    val testInput1 = readInput("day$dayZeroPadded/Part1_test")
    val testInput1Result = part1(testInput1)
    val testInput1Expected = 6440
    logger.info("Part 1 test result: $testInput1Result")
    check(testInput1Result == testInput1Expected) { "Expected $testInput1Result to equal $testInput1Expected"}

//    val testInput2 = readInput("day$dayZeroPadded/Part2_test")
    val testInput2Result = part2(testInput1)
    val testInput2Expected = 5905
    logger.info("Part 2 test result: $testInput2Result")
    check(testInput2Result == testInput2Expected) { "Expected $testInput2Result to equal $testInput2Expected"}

    val additionalTestCases = listOf(
        Pair(Hand("AAAAA", 2), 7),
        Pair(Hand("22222", 3), 7),
        Pair(Hand("AAAAK", 5), 6),
        Pair(Hand("22223", 7), 6),
        Pair(Hand("AAAKK", 11), 5),
        Pair(Hand("22233", 13), 5),
        Pair(Hand("AAAKQ", 17), 4),
        Pair(Hand("22234", 19), 4),
        Pair(Hand("AAKKQ", 23), 3),
        Pair(Hand("22334", 29), 3),
        Pair(Hand("AAKQ2", 31), 2),
        Pair(Hand("22345", 37), 2),
        Pair(Hand("AKQJT", 41), 1),
        Pair(Hand("23456", 43), 1),
    )

    val additionalWildcardTestCases = listOf(
        Pair(WildcardHand("AAAAJ", 2), 7),
        Pair(WildcardHand("222JJ", 3), 7),
        Pair(WildcardHand("AAJAK", 5), 6),
        Pair(WildcardHand("JJ223", 7), 6),
        Pair(WildcardHand("AAAKK", 11), 5),
//        Pair(WildcardHand("22JJ3", 13), 5),
        Pair(WildcardHand("AAJKQ", 17), 4),
        Pair(WildcardHand("2JJ34", 19), 4),
        Pair(WildcardHand("AAKQJ", 31), 4),
        Pair(WildcardHand("AAKKQ", 23), 3),
        Pair(WildcardHand("22334", 29), 3),
        Pair(WildcardHand("AJKQ2", 31), 2),
        Pair(WildcardHand("2J345", 37), 2),
        Pair(WildcardHand("AKQ3T", 41), 1),
        Pair(WildcardHand("23456", 43), 1),

        Pair(WildcardHand("AAAAA", 2), 7),
        Pair(WildcardHand("22222", 3), 7),
        Pair(WildcardHand("AAAAK", 5), 6),
        Pair(WildcardHand("22223", 7), 6),
        Pair(WildcardHand("AAAKK", 11), 5),
        Pair(WildcardHand("22233", 13), 5),
        Pair(WildcardHand("AAAKQ", 17), 4),
        Pair(WildcardHand("22234", 19), 4),
        Pair(WildcardHand("AAKKQ", 23), 3),
        Pair(WildcardHand("22334", 29), 3),
        Pair(WildcardHand("AAKQ2", 31), 2),
        Pair(WildcardHand("22345", 37), 2),
        Pair(WildcardHand("AKQ3T", 41), 1),
        Pair(WildcardHand("23456", 43), 1),
    )

    additionalTestCases.forEach {
        check(it.first.getStrength() == it.second) {
            "Expected Hand ${it.first.hand} to have strength ${it.second}, not ${it.first.getStrength()}"
        }
    }

    additionalWildcardTestCases.forEach {
        check(it.first.getStrength() == it.second) {
            "Expected Hand ${it.first.hand} to have strength ${it.second}, not ${it.first.getStrength()}"
        }
    }

    val input = readInput("day$dayZeroPadded/Part1")
    logger.info("Part 1 solution: ${part1(input)}")
    logger.info("Part 2 solution: ${part2(input)}")
}

// 246153451 too low
// 245982489
// 245643600
// 245982489 too low
// 246285222