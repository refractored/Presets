package net.refractored.presets.commands

import net.kyori.adventure.text.minimessage.MiniMessage
import net.refractored.presets.PresetsPlugin
import net.refractored.presets.presets.Presets
import org.bukkit.Material
import org.bukkit.Registry
import org.bukkit.inventory.ItemStack
import revxrsal.commands.annotation.AutoComplete
import revxrsal.commands.annotation.Command
import revxrsal.commands.annotation.Description
import revxrsal.commands.bukkit.BukkitCommandActor
import revxrsal.commands.bukkit.annotation.CommandPermission
import revxrsal.commands.exception.CommandErrorException

class ImportPresets {
    @CommandPermission("presets.import")
    @Description("Imports items from other plugins.")
    @Command("presets import")
    @AutoComplete("enchants")
    fun importItems(
        actor: BukkitCommandActor,
        import: String,
    ) {
        when (import) {
            "enchants" -> {
                actor.reply(
                    MiniMessage.miniMessage().deserialize("Importing Items..."),
                )
                importEnchantmentBooks()
            }

            else -> throw CommandErrorException(
                "Not a valid option.",
            )
        }
        actor.reply(
            MiniMessage.miniMessage().deserialize("Done Importing Items!"),
        )
    }

    private fun importEnchantmentBooks() {
//        if (MessageUtil.getMessageUnformatted("ImportItems.EnchantmentBookPrefix") == "ImportItems.EnchantmentBookPrefix") throw IllegalStateException("Book prefix is not setup!")

        val enchants = Registry.ENCHANTMENT.iterator()

        val enchantedBooks = mutableMapOf<String, ItemStack>()

        for (enchant in enchants) {
            for (level in 1..enchant.maxLevel) {
                val enchantedBook = ItemStack(Material.ENCHANTED_BOOK)

                val enchantedBookMeta = enchantedBook.itemMeta

                enchantedBookMeta!!.addEnchant(enchant, level, true)

                enchantedBook.itemMeta = enchantedBookMeta

                enchantedBooks["enchanted_book_" + "${enchant.keyOrThrow.key.lowercase()}_$level"] = enchantedBook
            }
        }

        for ((name, item) in enchantedBooks) {
            PresetsPlugin.instance.logger.info("Creating preset for $name")

            try {
                Presets.createPreset(name, item)
            } catch (e: Exception) {
                PresetsPlugin.instance.logger.warning("Failed to create preset \"${name}\": ${e.message}")
                continue
            }
        }
    }
}
