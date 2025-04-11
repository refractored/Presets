package net.refractored.presets.resolver

import com.willfp.eco.core.integrations.customitems.CustomItemsIntegration
import com.willfp.eco.core.items.CustomItem
import com.willfp.eco.core.items.Items
import com.willfp.eco.core.items.TestableItem
import com.willfp.eco.core.items.provider.ItemProvider
import com.willfp.eco.util.NamespacedKeyUtils
import net.refractored.presets.presets.Presets

class EcoResolver : CustomItemsIntegration {
    override fun getPluginName(): String = "Presets"

    override fun registerProvider() {
        Items.registerItemProvider(Provider())
    }

    private class Provider : ItemProvider("presets") {
        override fun provideForKey(key: String): TestableItem? {
            val item = Presets.getPreset(key) ?: return null
            val namespacedKey = NamespacedKeyUtils.create("presets", key)
            return CustomItem(
                namespacedKey,
                { it.isSimilar(item.item) },
                item.item,
            )
        }
    }
}
