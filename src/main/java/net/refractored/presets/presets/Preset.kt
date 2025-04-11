package net.refractored.presets.presets

import org.bukkit.inventory.ItemStack

data class Preset(
    val item: ItemStack,
    val fromConfig: Boolean = true,
)
