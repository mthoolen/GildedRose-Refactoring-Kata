package com.gildedrose

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GildedRoseTest {

    private fun createFoo() = Item("foo", 0, 0)

    @Test
    internal fun `added item should be found in store`() {
        val foo = createFoo()
        createRoseAndUpdate(foo)
        assertThat(foo.name).isEqualTo("foo")
    }

    @Test
    internal fun `quality can't be negative`() {
        val foo = createFoo()
        createRoseAndUpdate(foo)
        assertThat(foo.quality).isEqualTo(0)
    }

    @Test
    internal fun `quality can't be more than 50`() {
        val brie = Item("Aged Brie", 0,50)
        createRoseAndUpdate(brie)
        assertThat(brie.quality).isEqualTo(50)
    }

    @Test
    internal fun `quality degrades with each passing day`() {
        val bar = Item("bar", 2, 1)
        createRoseAndUpdate(bar)
        assertThat(bar.quality).isEqualTo(0)
    }

    @Test
    internal fun `aged brie increases in quality the older it gets`() {
        val brie = Item("Aged Brie", 2, 0)
        createRoseAndUpdate(brie)
        assertThat(brie.quality).isEqualTo(1)
    }

    @Test
    internal fun `after sell by date has passed, quality should degrade twice as fast`() {
        val eww = Item("eww", 1, 10)
        val rose = createRoseAndUpdate(eww)
        assertThat(eww.quality).isEqualTo(9)

        rose.updateQuality()
        assertThat(eww.quality).isEqualTo(7)
    }

    @Test
    internal fun `legendary items dont decrease in quality`() {
        val sulfuras = Item("Sulfuras, Hand of Ragnaros", 0, 50)
        createRoseAndUpdate(sulfuras)
        assertThat(sulfuras.quality).isEqualTo(50)
    }

    @Test
    internal fun `backstage pass quality increases twice as fast with sell in date within 10 days`() {
        val pass = Item("Backstage passes to a TAFKAL80ETC concert", 10, 40)
        createRoseAndUpdate(pass)
        assertThat(pass.quality).isEqualTo(42)
    }

    @Test
    internal fun `backstage pass quality increases three times as fast with sell in date within 5 days`() {
        val pass = Item("Backstage passes to a TAFKAL80ETC concert", 5, 40)
        createRoseAndUpdate(pass)
        assertThat(pass.quality).isEqualTo(43)
    }

    @Test
    internal fun `backstage pass quality quality is void after sell by date passes`() {
        val pass = Item("Backstage passes to a TAFKAL80ETC concert", 0, 50)
        createRoseAndUpdate(pass)
        assertThat(pass.quality).isEqualTo(0)
    }

    @Test
    internal fun `conjured items degrade twice as fast`() {
        val conjured = Item("Conjured sword", 10, 40)
        createRoseAndUpdate(conjured)
        assertThat(conjured.quality).isEqualTo(38)
    }

    @Test
    internal fun `conjured items degrade four times as fast after expiring`() {
        val conjured = Item("Conjured sword", 0, 40)
        createRoseAndUpdate(conjured)
        assertThat(conjured.quality).isEqualTo(36)
    }

    private fun createRoseAndUpdate(item: Item): GildedRose {
        val items = arrayOf(item)
        val app = GildedRose(items)
        app.updateQuality()
        return app
    }
}


