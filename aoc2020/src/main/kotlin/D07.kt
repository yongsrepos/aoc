import utils.IoHelper

class D07 {
    fun getSolution1(): Int {
        val allBags = getInputs()
        val root = Node("root", mutableListOf())
        getEmptyBags().forEach { getAllOuterBagNames(root, it, allBags) }
        val allParents = mutableSetOf<Node>()
        getMyOuterBags(root, "shiny gold bag", allParents)
        return allParents.size
    }

    fun getSolution2(): Int {
        val counts = mutableListOf<Int>()
        getCount("shiny gold bag", 1, getInputs(), counts)
        return counts.sum() - 1
    }

    private fun getCount(
        bagName: String,
        myCount: Int,
        allBags: Map<String, Map<String, Int>>,
        counts: MutableList<Int>
    ) {
        counts.add(myCount)
        if (allBags[bagName].orEmpty().isEmpty()) {
            return
        }

        for (it in allBags[bagName].orEmpty()) {
            getCount(it.key, it.value * myCount, allBags, counts)
        }
    }

    private fun getAllParents(root: Node, parentNodes: MutableSet<Node>) {
        for (node in root.outerBags) {
            parentNodes.add(node)
            getAllParents(node, parentNodes)
        }
    }

    private fun getMyOuterBags(root: Node, expectedBagName: String, parentNodes: MutableSet<Node>) {
        for (it in root.outerBags) {
            if (it.name == expectedBagName) {
                getAllParents(it, parentNodes)
                return
            }
            getMyOuterBags(it, expectedBagName, parentNodes)
        }
    }

    private fun getAllOuterBagNames(parentNode: Node, bagName: String, allBags: Map<String, Map<String, Int>>) {
        val myNode = Node(bagName, mutableListOf())
        parentNode.outerBags.add(myNode)
        val outerBagNames = getOuterBagNames(bagName, allBags)
        for (outerBagName in outerBagNames) {
            getAllOuterBagNames(myNode, outerBagName, allBags)
        }
    }

    private fun getOuterBagNames(bagName: String, allBags: Map<String, Map<String, Int>>): List<String> {
        return allBags.filter { bagName in it.value }.map { it.key }
    }

    private fun getEmptyBags(): List<String> {
        return getInputs().filter { it.value.isEmpty() }.map { it.key }
    }

    private fun getInputs(): Map<String, Map<String, Int>> {
        return IoHelper().getLines("d07.in").map { toRecord(it).name to toRecord(it).innerBags }.toMap()
    }

    private fun toRecord(rawLine: String): Bag {
        val outerAndInners = rawLine.replace("bags", "bag").dropLast(1).split(" contain ")
        return if ("no other bag" == outerAndInners[1]) Bag(outerAndInners[0], mapOf()) else {
            val innerBags = outerAndInners[1].split(", ").map { it.drop(2) to it[0].toString().toInt() }.toMap()
            Bag(outerAndInners[0], innerBags)
        }
    }
}

data class Bag(val name: String, val innerBags: Map<String, Int>)

data class Node(val name: String, val outerBags: MutableList<Node>)
