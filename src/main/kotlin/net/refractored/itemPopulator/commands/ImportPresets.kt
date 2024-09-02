package net.refractored.itemPopulator.commands

import com.willfp.eco.core.items.Items
import dev.lone.itemsadder.api.ItemsAdder
import net.kyori.adventure.text.minimessage.MiniMessage
import net.refractored.itemPopulator.ItemPopulator
import net.refractored.itemPopulator.presets.Presets
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
    @CommandPermission("itempopulator.import")
    @Description("Imports items from other plugins.")
    @Command("itempopulator preset import")
    @AutoComplete("eco|ItemsAdder|enchants")
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
            "eco" -> {
                if (!ItemPopulator.instance.ecoPlugin) {
                    throw CommandErrorException(
                        "Eco is not loaded."
                    )
                }
                actor.reply(
                    MiniMessage.miniMessage().deserialize("Importing Items..."),
                )
                importEcoItems()
            }
            "ItemsAdder" -> {
                if (!ItemPopulator.instance.itemsAdder) {
                    throw CommandErrorException(
                        "Itemsadder is not loaded."
                    )
                }
                actor.reply(
                    MiniMessage.miniMessage().deserialize("Importing Items..."),
                )
                importItemsAdderItems()
            }
            else -> throw CommandErrorException(
                "Eco is not loaded."
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

        for (enchant in enchants){
            for (level in 1..enchant.maxLevel){
                val enchantedBook = ItemStack(Material.ENCHANTED_BOOK)

                val enchantedBookMeta = enchantedBook.itemMeta

                enchantedBookMeta!!.addEnchant(enchant, level, true)

                enchantedBook.itemMeta = enchantedBookMeta

                enchantedBooks["enchanted_book_" + "${enchant.key.key}_$level" ] = enchantedBook
            }

        }

        for ((name, item) in enchantedBooks) {
            ItemPopulator.instance.logger.info("Creating preset for $name")

            try {
                Presets.createPreset(name, item)
            } catch (e: IllegalArgumentException) {
                ItemPopulator.instance.logger.warning("Preset already exists for $name")
                continue
            }
        }
    }

    private fun importEcoItems() {
        val customItems = Items.getCustomItems()

        for (items in customItems) {
            var name = items.key.key
            name = name.removePrefix("set_")

            ItemPopulator.instance.logger.info("Creating preset for $name")

            try {
                Presets.createPreset(name, items.item)
            } catch (e: IllegalArgumentException) {
                ItemPopulator.instance.logger.warning("Preset already exists for $name")
                continue
            }
        }
    }

    private fun importItemsAdderItems() {
        val customItems = ItemsAdder.getAllItems()

        for (items in customItems) {
            val name = items.id

            ItemPopulator.instance.logger.info("Creating preset for $name")

            try {
                Presets.createPreset(name, items.itemStack)
            } catch (e: IllegalArgumentException) {
                ItemPopulator.instance.logger.warning("Preset already exists for $name")
                continue
            }
        }
    }
}