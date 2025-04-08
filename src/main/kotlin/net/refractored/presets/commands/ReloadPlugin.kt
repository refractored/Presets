package net.refractored.presets.commands

import net.refractored.presets.PresetsPlugin
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.bukkit.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission

class ReloadPlugin {
    @CommandPermission("presets.reload")
    @Description("Reloads plugin configuration")
    @Command("presets reload")
    fun reloadCommand(actor: BukkitCommandActor) {
        actor.reply("Reloading plugin...")
        PresetsPlugin.instance.reload()
        actor.reply("Plugin reloaded!")
    }
}
