package net.refractored.itemPopulator.commands

import net.kyori.adventure.text.minimessage.MiniMessage
import net.refractored.itemPopulator.presets.Presets
import org.bukkit.Material
import revxrsal.commands.bukkit.BukkitCommandActor
import revxrsal.commands.bukkit.player
import revxrsal.commands.exception.CommandErrorException
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.bukkit.annotation.CommandPermission

class CreatePreset {

    @CommandPermission("joblistings.admin.create.preset")
    @Description("Adds a preset to the config and memory.")
    @Command("itempopulator preset create")
    fun createPreset(
        actor: BukkitCommandActor,
        presetName: String,
    ) {
        if (actor.isConsole) {
            throw CommandErrorException(
                "This command can only be run by players.",
            )
        }
        if (Material.entries.any { it.name.equals(presetName, true) }) {
            throw CommandErrorException(
                "Presets cannot be the same name as a existing minecraft material! ($presetName)",
            )
        }
        if (Presets.getPresets()[presetName] != null) {
            throw CommandErrorException(
                "Preset already exists.",
            )
        }
        val item =
            actor.player.inventory.itemInMainHand
                .clone()

        if (item.type == Material.AIR) {
            throw CommandErrorException(
                "You must be holding an item to create a preset.",
            )
        }

        item.amount = 1

        Presets.createPreset(presetName, item)
        actor.reply(
            MiniMessage.miniMessage().deserialize(
                "<green>Successfully created preset <white>$presetName<green>.",
            ),
        )
    }


}