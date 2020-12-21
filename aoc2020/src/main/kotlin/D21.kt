import utils.IoHelper

class D21 {
    fun getSolution1(): Int {
        return getSolution().first
    }

    fun getSolution2(): String {
        return getSolution().second
    }

    private fun getSolution(): Pair<Int, String> {
        val allergensToFood = mutableMapOf<String, MutableSet<Food>>()
        val allergensToIngredients = mutableMapOf<String, MutableSet<String>>()
        val foods = getInputs()
        foods.forEach { food ->
            food.allergens.forEach {
                if (it !in allergensToFood) {
                    allergensToFood[it] = mutableSetOf(food)
                } else {
                    allergensToFood[it]!!.add(food)
                }
            }
        }
        allergensToFood.forEach { allergenToFoods ->
            val potentialIngredients =
                allergenToFoods.value.map { it.ingredients }.reduce { acc, set -> acc.intersect(set) }
            if (allergenToFoods.key !in allergensToIngredients) {
                allergensToIngredients[allergenToFoods.key] = potentialIngredients.toMutableSet()
            } else {
                allergensToIngredients[allergenToFoods.key] =
                    allergensToIngredients[allergenToFoods.key]!!.intersect(potentialIngredients.toMutableSet())
                        .toMutableSet()
            }
        }
        doPruning(allergensToIngredients)
        val foodIngredients = foods.map { it.ingredients.toMutableSet() }
        removeAllergens(foodIngredients, allergensToIngredients)

        val s1 = foodIngredients.map { it.count() }.sum()
        val s2 = allergensToIngredients.map { it.key to it.value.toList()[0] }.sortedBy { it.first }.map { it.second }
            .joinToString(",")

        return s1 to s2
    }

    private fun removeAllergens(
        foodIngredients: List<MutableSet<String>>,
        allergensToIngredients: MutableMap<String, MutableSet<String>>
    ) {
        allergensToIngredients.values.forEach { allergicIngredients ->
            foodIngredients.forEach { fi -> allergicIngredients.forEach { fi.remove(it) } }
        }
    }

    private fun doPruning(allergensToIngredients: MutableMap<String, MutableSet<String>>) {
        var newlyResolved = allergensToIngredients.filter { it.value.size == 1 }.toMutableMap()
        var allResolvedAllergens = newlyResolved.keys.toMutableSet()
        while (newlyResolved.isNotEmpty()) {
            newlyResolved.forEach { current ->
                allergensToIngredients.forEach {
                    if (it.key != current.key) {
                        allergensToIngredients[it.key] = it.value.subtract(current.value).toMutableSet()
                    }
                }
            }

            val resolvedNew = allergensToIngredients.filter { it.value.size == 1 }.toMutableMap()
            allResolvedAllergens.forEach { resolvedNew.remove(it) }
            allResolvedAllergens.addAll(resolvedNew.keys)
            newlyResolved = resolvedNew
        }
    }

    private fun getInputs(): Set<Food> {
        return IoHelper().getLines("d21.in").map { parseFood(it) }.toSet()
    }

    private fun parseFood(raw: String): Food {
        val splits = raw.split(" (")
        val ingredients = splits[0]!!.split(" ").toSet()
        val allergens = if (splits.size == 1) setOf<String>()
        else splits[1]!!.drop(9).dropLast(1).split(", ").toSet()
        return Food(ingredients, allergens)
    }
}

data class Food(val ingredients: Set<String>, val allergens: Set<String>)
