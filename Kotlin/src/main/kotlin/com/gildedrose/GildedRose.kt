package com.gildedrose

class GildedRose(var items: Array<Item>) {
    private val minimumQuality = 0
    private val maximumQuality = 50
    private val betterByAge = listOf("Aged Brie")
    private val legendaries = listOf("Sulfuras, Hand of Ragnaros")
    init {
        require(items.all { it.quality >= minimumQuality }) { "quality must be 0 or over!" }
    }

    private val itemKindToUpdateFunction = mapOf(
            isLegendary() to updateLegendary(),
            isConjured() to updateConjured(),
            isBackstagePass() to updateBackstagePass(),
            isBetterWithAge() to updateBetterWithAge(),
            isDefault() to updateDefault())

    fun updateQuality() = items.map { decreaseSellIn(it) }.forEach { getUpdateFunction(it).invoke(it) }

    private fun getUpdateFunction(item: Item) =
            itemKindToUpdateFunction.filter { it.key.invoke(item) }.values.first()

    private fun updateDefault() = { item: Item ->
        decreaseQuality(item)
        if (sellByHasPassed(item)) decreaseQuality(item)
    }

    private fun updateBetterWithAge() = { item: Item ->
        increaseQuality(item)
        if (sellByHasPassed(item)) increaseQuality(item)
    }

    private fun updateBackstagePass() = { item: Item ->
        when {
            item.sellIn < 1 -> item.quality = minimumQuality
            item.sellIn < 6 -> increaseQuality(item, 3)
            item.sellIn < 11 -> increaseQuality(item, 2)
            item.sellIn < 50 -> increaseQuality(item)
        }
    }

    private fun updateConjured() = { item: Item ->
        decreaseQuality(item, 2)
        if (sellByHasPassed(item)) decreaseQuality(item, 2)
    }

    private fun updateLegendary() = { _: Item -> Unit }

    private fun isLegendary() = { item: Item -> legendaries.contains(item.name) }

    private fun isConjured() = { item: Item -> item.name.startsWith("Conjured") }

    private fun isBackstagePass() = { item: Item -> item.name == "Backstage passes to a TAFKAL80ETC concert" }

    private fun isBetterWithAge() = { item: Item -> betterByAge.contains(item.name) }

    private fun isDefault() = { item: Item -> !(isLegendary()(item) || isBetterWithAge()(item) || isBackstagePass()(item)) }

    private fun decreaseSellIn(item: Item): Item {
        item.sellIn -= 1
        return item
    }

    private fun decreaseQuality(item: Item) {
        if (item.quality > minimumQuality) item.quality -= 1
    }

    private fun increaseQuality(item: Item) {
        if (item.quality < maximumQuality) item.quality += 1
    }

    private fun increaseQuality(item: Item, amount: Int) = repeat(amount) { increaseQuality(item) }

    private fun decreaseQuality(item: Item, amount: Int) = repeat(amount) { decreaseQuality(item) }

    private fun sellByHasPassed(item: Item): Boolean = item.sellIn < minimumQuality

}

