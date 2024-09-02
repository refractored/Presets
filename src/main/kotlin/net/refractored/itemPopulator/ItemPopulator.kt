package net.refractored.itemPopulator

import com.earth2me.essentials.Essentials
import net.refractored.itemPopulator.commands.CreatePreset
import net.refractored.itemPopulator.commands.ImportPresets
import net.refractored.itemPopulator.commands.ReloadPlugin
import net.refractored.itemPopulator.itemResolver.ItemResolver
import net.refractored.itemPopulator.presets.Presets
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler
import java.io.File

class ItemPopulator : JavaPlugin() {
    /**
    * The preset configuration
    */
    lateinit var presets: FileConfiguration
        private set

    /**
     * Returns true if eco is loaded
     */
    var ecoPlugin: Boolean = false
        private set

    /**
     * Returns true if ItemsAdder is loaded
     */
    var itemsAdder: Boolean = false
        private set


    /**
     * Essentials
     */
    lateinit var essentials: Essentials
        private set

    /**
     * The command handler
     */
    private lateinit var handler: BukkitCommandHandler

    override fun onEnable() {
        instance = this

        server.pluginManager.getPlugin("Essentials").let {
            essentials = (it as Essentials)
            logger.info("Hooked into Essentials")
        }

        server.pluginManager.getPlugin("eco")?.let {
            ecoPlugin = true
            logger.info("Hooked into eco")
        }

        server.pluginManager.getPlugin("ItemsAdder")?.let {
            itemsAdder = true
            logger.info("Hooked into ItemsAdder")
        }

        if (!File(dataFolder, "presets.yml").exists()) {
            saveResource("presets.yml", false)
        }

        // Load messages config
        presets = YamlConfiguration.loadConfiguration(dataFolder.resolve("presets.yml"))

        Presets.refreshPresets()

        handler = BukkitCommandHandler.create(this)

        handler.register(ReloadPlugin())
        handler.register(ImportPresets())
        handler.register(CreatePreset())

        handler.registerBrigadier()

        essentials.itemDb.registerResolver(this, "ItemPopulator", ItemResolver())

    }

    override fun onDisable() {
        if (this::handler.isInitialized) {
            handler.unregisterAllCommands()
        }
    }

    fun reload(){
        essentials.itemDb.unregisterResolver(this, "ItemPopulator")
        Presets.refreshPresets()

    }

    companion object {
        @JvmStatic
        lateinit var instance: ItemPopulator
            private set
    }
}
