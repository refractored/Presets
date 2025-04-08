package net.refractored.presets.itemResolver

import net.ess3.api.IItemDb.ItemResolver
import net.refractored.presets.PresetsPlugin
import net.refractored.presets.presets.Presets
import org.bukkit.inventory.ItemStack

class EssentialsResolver : ItemResolver {
    override fun getNames(): MutableCollection<String> = Presets.getPresets().keys.toMutableList()

    override fun apply(name: String): ItemStack? {
        val preset = Presets.getPreset(name) ?: return null
        if (!PresetsPlugin.instance.config.getBoolean("integration.essentials.other-plugins") && !preset.fromConfig) {
            return null
        }
        return preset.item
    }
}
