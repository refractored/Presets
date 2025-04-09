package net.refractored.presets.commands

import net.kyori.adventure.text.minimessage.MiniMessage
import net.refractored.presets.presets.Presets
import revxrsal.commands.annotation.AutoComplete
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.bukkit.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.bukkit.player
import revxrsal.commands.exception.CommandErrorException

class GetPreset {
    @CommandPermission("presets.get.preset")
    @Description("Gets the item for the preset.")
    @Command("presets get")
    @AutoComplete("@presets")
    fun presetGet(
        actor: BukkitCommandActor,
        presetName: String,
    ) {
        if (actor.isConsole) {
            throw CommandErrorException(
                "This command can only be run by players.",
            )
        }

        val preset =
            Presets.getPresets()[presetName]
                ?: throw CommandErrorException(
                    "Preset does not exist.",
                )

        val itemNumber = (actor.player.inventory.addItem(preset.item)).values.sumOf { it.amount }

        if (itemNumber != 0) {
            throw CommandErrorException(
                "You do not have enough space in your inventory to get the preset.",
            )
        }

        actor.reply(
            MiniMessage.miniMessage().deserialize(
                "<green>Successfully added preset <white>$presetName<green> to your inventory.",
            ),
        )
    }
}
