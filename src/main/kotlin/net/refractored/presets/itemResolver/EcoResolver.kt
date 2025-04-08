package net.refractored.presets.itemResolver

import com.willfp.eco.core.integrations.customitems.CustomItemsIntegration
import com.willfp.eco.core.items.CustomItem
import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.items.provider.ItemProvider
import com.willfp.eco.util.NamespacedKeyUtils
import net.refractored.presets.presets.Presets

class EcoResolver : CustomItemsIntegration {
    override fun getPluginName(): String = "Presets"

    private class Provider : ItemProvider("presets") {
        override fun provideForKey(key: String): TestableItem? {
            val item = Presets.getPreset(key) ?: return null
            val namespacedKey = NamespacedKeyUtils.create("nexo", key.toString())
            return CustomItem(
                namespacedKey,
                { true },
                item.item,
            )
        }
    }
}
