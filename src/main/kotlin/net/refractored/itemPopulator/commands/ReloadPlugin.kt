package net.refractored.itemPopulator.commands

import net.refractored.itemPopulator.ItemPopulator
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.bukkit.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission

class ReloadPlugin {
    @CommandPermission("itempopulator.reload")
    @Description("Reloads plugin configuration")
    @Command("itempopulator reload")
    fun reloadCommand(actor: BukkitCommandActor) {
        actor.reply("Reloading plugin...")
        ItemPopulator.instance.reload()
        actor.reply("Plugin reloaded!")
    }
}