package net.refractored.presets.presets

import com.willfp.eco.core.items.Items
import dev.lone.itemsadder.api.ItemsAdder
import net.ess3.api.TranslatableException
import net.refractored.presets.PresetsPlugin
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

object Presets {
    private val presets: MutableMap<String, Preset> = mutableMapOf()

    /**
     * Gets a preset from the loaded presets
     * @param name The name of the preset
     * @return The itemstack for the preset, or null if it does not exist
     */
    @JvmStatic
    fun getPreset(name: String): Preset? = presets[name]

    /**
     * Gets a preset from the loaded presets
     * @param item The itemstack for the preset
     * @return The itemstack for the preset, or null if it does not exist
     */
    fun getPreset(item: ItemStack): Preset? {
        for (preset in presets) {
            if (preset.value.item == item) {
                return preset.value
            }
        }
        return null
    }

    /**
     * Gets a read-only map of all the presets loaded.
     * @return The mutable preset list
     */
    @JvmStatic
    fun getPresets() = presets.toMap()

    /**
     * Create a new preset and adds it to the config & map
     * @param name The name of the preset
     * @param item The itemstack for the preset
     */
    @JvmStatic
    fun createPreset(
        name: String,
        item: ItemStack,
        saveInConfig: Boolean = true,
    ) {
        if (getPreset(name) != null) {
            throw IllegalArgumentException("Preset already exists.")
        }
        if (Material.entries.any { it.name.equals(name, true) }) {
            throw IllegalArgumentException("Presets cannot be the same name as a existing minecraft material")
        }
        if (PresetsPlugin.instance.config.getBoolean("intergrations.essentials.enabled")) {
            val essentialsStack =
                try {
                    PresetsPlugin.instance.essentials
                        ?.itemDb
                        ?.get(name, false)
                } catch (_: TranslatableException) {
                    null
                }
            if (essentialsStack != null) {
                throw IllegalArgumentException("Presets cannot be the same name as a existing Essentials item")
            }
        }
        PresetsPlugin.instance.presets.set(name, item)
        if (saveInConfig) {
            PresetsPlugin.instance.presets.save(PresetsPlugin.instance.dataFolder.resolve("presets.yml"))
        }
        presets[name] = Preset(item)
    }

    /**
     * Removes a preset from the config & map
     * @return The itemstack that was created
     * @param name The name of the preset
     */
    @JvmStatic
    fun removePreset(name: String) {
        if (presets.contains(name)) {
            throw IllegalArgumentException("Preset does not exist.")
        }
        PresetsPlugin.instance.presets.set(name, null)
        PresetsPlugin.instance.presets.save(PresetsPlugin.instance.dataFolder.resolve("presets.yml"))
        presets.remove(name)
    }

    /**
     * Deletes all presets in the map and populates it with the presets in the config.
     */
    @JvmStatic
    fun refreshPresets() {
        presets.clear()
        val config = PresetsPlugin.instance.presets
        val section = config.getConfigurationSection("")
        val keys = section!!.getKeys(false)
        if (keys.isEmpty()) return
        for (key in keys) {
            try {
                createPreset(key, config.getItemStack(key)!!)
            } catch (exception: Exception) {
                PresetsPlugin.instance.logger.warning("Failed to load preset \"${key}\": ${exception.message}")
                continue
            }
        }

        if (PresetsPlugin.instance.itemsAdder && config.getBoolean("import.ItemsAdder")) {
            importItemsAdderItems()
        }

        if (PresetsPlugin.instance.ecoPlugin && config.getBoolean("import.eco")) {
            importEcoItems()
        }
    }

    private fun importItemsAdderItems() {
        val customItems = ItemsAdder.getAllItems()

        for (items in customItems) {
            val name = items.id

            PresetsPlugin.instance.logger.info("Creating preset for $name")

            try {
                createPreset(name, items.itemStack, false)
            } catch (e: Exception) {
                PresetsPlugin.instance.logger.warning("Failed to create preset \"${name}\": ${e.message}")
                continue
            }
        }
    }

    private fun importEcoItems() {
        val customItems = Items.getCustomItems()

        for (items in customItems) {
            var name = items.key.key
            name = name.removePrefix("set_")

            PresetsPlugin.instance.logger.info("Creating preset for $name")

            try {
                createPreset(name, items.item, false)
            } catch (e: Exception) {
                PresetsPlugin.instance.logger.warning("Failed to create preset \"${name}\": ${e.message}")
                continue
            }
        }
    }
}
