import utils.IoHelper

class D07 {
    fun getSolution1(): Int {
        val bagCompositions = getInputs()
        val virtualRoot = Node("root", mutableListOf())
        getEmptyBags().forEach { buildTree(virtualRoot, it, bagCompositions) }
        val allLevelsOuterNodes = mutableSetOf<Node>()
        getAllLevelsOuterNodes(virtualRoot, "shiny gold bag", allLevelsOuterNodes)
        return allLevelsOuterNodes.size
    }

    fun getSolution2(): Int {
        val counts = mutableListOf<Int>()
        getCount("shiny gold bag", 1, getInputs(), counts)
        return counts.sum() - 1
    }

    fun getSolution1Alt(): Int {
        val bagCompositions = getInputs()
        val allOuterBagNames = mutableSetOf<String>()
        var currentOuterBagNames = bagCompositions.filter { "shiny gold bag" in it.value }.map { it.key }.toMutableSet()

        while (currentOuterBagNames.isNotEmpty()) {
            var newLayerOuterBagNames = mutableSetOf<String>()

            for (outerName in currentOuterBagNames) {
                allOuterBagNames.add(outerName)

                newLayerOuterBagNames = bagCompositions.filter { outerName in it.value }.map { it.key }.toMutableSet()
                    .union(newLayerOuterBagNames) as MutableSet<String>
            }

            currentOuterBagNames = newLayerOuterBagNames
        }
        return allOuterBagNames.size
    }

    private fun getCount(
        bagName: String,
        myCount: Int,
        bagCompositions: Map<String, Map<String, Int>>,
        counts: MutableList<Int>
    ) {
        counts.add(myCount)
        if (bagCompositions[bagName].orEmpty().isEmpty()) {
            return
        }

        for (it in bagCompositions[bagName].orEmpty()) {
            getCount(it.key, it.value * myCount, bagCompositions, counts)
        }
    }

    private fun getAllLevelsOuterNodes(startingNode: Node, startingBagName: String, outerNodes: MutableSet<Node>) {
        for (it in startingNode.outerBags) {
            if (it.name == startingBagName) {
                getAllLevelsOuterNodes(it, outerNodes)
                return
            }
            getAllLevelsOuterNodes(it, startingBagName, outerNodes)
        }
    }

    private fun getAllLevelsOuterNodes(startingNode: Node, outerNodes: MutableSet<Node>) {
        for (node in startingNode.outerBags) {
            outerNodes.add(node)
            getAllLevelsOuterNodes(node, outerNodes)
        }
    }

    private fun buildTree(innerBagNode: Node, currentBagName: String, bagCompositions: Map<String, Map<String, Int>>) {
        val currentNode = Node(currentBagName, mutableListOf())
        innerBagNode.outerBags.add(currentNode)
        for (outerBagName in getOuterBagNames(currentBagName, bagCompositions)) {
            buildTree(currentNode, outerBagName, bagCompositions)
        }
    }

    private fun getOuterBagNames(currentBagName: String, bagCompositions: Map<String, Map<String, Int>>): List<String> {
        return bagCompositions.filter { currentBagName in it.value }.map { it.key }
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

fun main() {
    D07().getSolution1Alt()
}
