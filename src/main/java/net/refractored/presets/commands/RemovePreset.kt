package net.refractored.presets.commands

import net.kyori.adventure.text.minimessage.MiniMessage
import net.refractored.presets.presets.Presets
import revxrsal.commands.annotation.AutoComplete
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.bukkit.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.exception.CommandErrorException

class RemovePreset {
    @CommandPermission("presets.remove.preset")
    @Description("Removes a preset from the config and memory.")
    @Command("presets remove")
    @AutoComplete("@presets")
    fun removePreset(
        actor: BukkitCommandActor,
        presetName: String,
    ) {
        if (actor.isConsole) {
            throw CommandErrorException(
                "This command can only be run by players.",
            )
        }
        if (Presets.getPreset(presetName) == null) {
            throw CommandErrorException(
                "Preset does not exist.",
            )
        }
        Presets.removePreset(presetName)
        actor.reply(
            MiniMessage.miniMessage().deserialize(
                "<green>Successfully removed preset <white>$presetName<green>.",
            ),
        )
    }
}
