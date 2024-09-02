package net.refractored.itemPopulator.itemResolver

import org.bukkit.inventory.ItemRarity
import org.bukkit.inventory.ItemStack
import net.ess3.api.IItemDb.ItemResolver
import net.refractored.itemPopulator.presets.Presets

class ItemResolver: ItemResolver {

    override fun getNames(): MutableCollection<String> {
        return Presets.getPresets().keys.toMutableList()
    }

    override fun apply(name: String): ItemStack? {
       return Presets.getPreset(name)
    }
}