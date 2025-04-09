package net.refractored.presets

import com.earth2me.essentials.Essentials
import net.refractored.presets.commands.*
import net.refractored.presets.presets.Presets
import net.refractored.presets.resolver.EcoResolver
import net.refractored.presets.resolver.EssentialsResolver
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import revxrsal.commands.bukkit.BukkitCommandHandler
import revxrsal.commands.command.CommandActor
import revxrsal.commands.command.ExecutableCommand
import java.io.File

class PresetsPlugin : JavaPlugin() {
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
    var essentials: Essentials? = null
        private set

    /**
     * The command handler
     */
    private lateinit var handler: BukkitCommandHandler

    override fun onEnable() {
        instance = this

        saveDefaultConfig()

        server.pluginManager.getPlugin("Essentials")?.let {
            essentials = (it as? Essentials)
        }

        server.pluginManager.getPlugin("eco")?.let {
            ecoPlugin = true
            if (config.getBoolean("integration.eco.enabled")) {
                EcoResolver().registerProvider()
            }
        }

        server.pluginManager.getPlugin("ItemsAdder")?.let {
            itemsAdder = true
        }

        if (!File(dataFolder, "presets.yml").exists()) {
            saveResource("presets.yml", false)
        }

        presets = YamlConfiguration.loadConfiguration(dataFolder.resolve("presets.yml"))

        Presets.refreshPresets()

        handler = BukkitCommandHandler.create(this)

        handler.autoCompleter.registerSuggestion(
            "presets",
        ) { _: List<String?>?, _: CommandActor?, _: ExecutableCommand? ->
            return@registerSuggestion Presets
                .getPresets()
                .keys
        }

        handler.register(ReloadPlugin())
        handler.register(ImportPresets())
        handler.register(CreatePreset())
        handler.register(GetPreset())
        handler.register(RemovePreset())

        handler.registerBrigadier()

        if (config.getBoolean("integration.essentials.enabled")) {
            essentials?.itemDb?.registerResolver(this, "Presets", EssentialsResolver())
        }
    }

    override fun onDisable() {
        if (this::handler.isInitialized) {
            handler.unregisterAllCommands()
        }
    }

    fun reload() {
        reloadConfig()
        if (config.getBoolean("integration.essentials.enabled")) {
            essentials?.itemDb?.registerResolver(this, "Presets", EssentialsResolver())
        }
        Presets.refreshPresets()
    }

    companion object {
        @JvmStatic
        lateinit var instance: PresetsPlugin
            private set
    }
}
