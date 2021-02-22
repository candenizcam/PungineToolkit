package com.pungo.modules.basic.string

object StringOperations {
    /**Returns substring between two chars
     * Returns full string if char is not found
     */
    fun getBetween(s: String, c1: String, c2: String? = null): String {
        splitString(s, c1).also {
            splitString(it.second, c2 ?: c1).also { it2 ->
                return it2.first
            }
        }
    }

    /** Returns list of substrings between each char pair
     */
    fun getBetweenPairs(s: String, c1: String, c2: String? = null): MutableList<String> {
        val l = gbwHelper(s, c1, c2, mutableListOf<String>())
        return l
    }

    private fun gbwHelper(s: String, c1: String, c2: String?, l: MutableList<String>): MutableList<String> {
        try {
            triplitString(s, c1, c2).also {
                l.add(it.second)
                gbwHelper(it.third, c1, c2, l)
            }
        } catch (e: Exception) {
            return l
        }
        return l
    }


    fun splitString(s: String, c: String): Pair<String, String> {
        val s1 = s.substringBefore(c)
        val s2 = s.substringAfter(c)
        if ((s1 == s) || (s2 == s)) {
            throw Exception("no character found in the string")
        }
        return Pair(s1, s2)
    }

    fun triplitString(s: String, c1: String, c2: String? = null): Triple<String, String, String> {
        var t = Triple("", "", "")
        splitString(s, c1).also {
            splitString(it.second, c2 ?: c1).also { it2 ->
                return Triple(it.first, it2.first, it2.second)
            }
        }
    }


}