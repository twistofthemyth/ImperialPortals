package ru.twistofthemyth.imperialportals.helpers;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Messages {
    private Messages() {
        throw new IllegalAccessError();
    }

    public static void activatePortal(Player player) {
        player.sendMessage(Component.text(ChatColor.RED + "Вы чувствуете чье то злобное внимание\n" +
                ChatColor.DARK_RED + "Похоже ваше подношение было принятно...\n" +
                "Золотые врата заработают, если их соединить с другим порталом"));
    }

    public static void linkPortal(Player player) {
        player.sendMessage(Component.text(ChatColor.RED + "Вы чувствуете чье то злобное внимание\n" +
                ChatColor.DARK_RED + "Похоже ваше подношение было принятно...\n" +
                "Древняя магия активирует золотой портал\n"));
    }

    public static void destroyPortal(Player player) {
        player.sendMessage(Component.text(ChatColor.RED + "Вы чувствуете чье то злобное внимание\n" +
                ChatColor.DARK_RED + "Древняя магия, что поддерживала портал, рассеивается..."));
    }
}
