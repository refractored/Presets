package net.refractored.itemPopulator.itemResolver

import net.ess3.api.IItemDb.ItemResolver
import net.refractored.itemPopulator.presets.Presets
import org.bukkit.inventory.ItemStack

class ItemResolver : ItemResolver {
    override fun getNames(): MutableCollection<String> = Presets.getPresets().keys.toMutableList()

    override fun apply(name: String): ItemStack? = Presets.getPreset(name)
}
