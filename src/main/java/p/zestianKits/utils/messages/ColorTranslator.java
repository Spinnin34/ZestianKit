package p.zestianKits.utils.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ColorTranslator {

    private static final Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]{6})");
    private static final Pattern removeUnusedDecimalsPattern = Pattern.compile("%!REMOVE_UNUSED_DECIMALS:(\\d+\\.?\\d*)!%");
    private static final Pattern removeAllDecimalsPattern = Pattern.compile("%!REMOVE_ALL_DECIMALS:(\\d+\\.?\\d*)!%");

    public static String toMM(String str) {
        str = str.replace("§", "&")
                .replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<grey>")
                .replace("&8", "<dark_grey>")
                .replace("&9", "<blue>")
                .replace("&a", "<green>")
                .replace("&b", "<aqua>")
                .replace("&c", "<red>")
                .replace("&d", "<light_purple>")
                .replace("&e", "<yellow>")
                .replace("&f", "<white>")
                .replace("&k", "<obf>")
                .replace("&l", "<b>")
                .replace("&m", "<st>")
                .replace("&n", "<u>")
                .replace("&o", "<i>")
                .replace("&r", "<reset>");

        if (str.contains("#")) {
            Matcher matcher = hexPattern.matcher(str);
            StringBuilder buffer = new StringBuilder();
            while (matcher.find()) {
                String replacement = String.format("<#%s>", matcher.group(1));
                matcher.appendReplacement(buffer, replacement);
            }
            matcher.appendTail(buffer);
            str = buffer.toString();
        }

        return str;
    }

    public static String replaceStandardPlaceholders(String message) {
        Matcher matcher = removeUnusedDecimalsPattern.matcher(message);
        while (matcher.find()) {
            String number = matcher.group(1);
            String replacement = number.contains(".") ? number.replaceAll("\\.0+$", "") : number;
            message = message.replace(matcher.group(0), replacement);
        }

        matcher = removeAllDecimalsPattern.matcher(message);
        while (matcher.find()) {
            String number = matcher.group(1);
            String replacement = number.split("\\.")[0];
            message = message.replace(matcher.group(0), replacement);
        }

        return message;
    }

    public static Component translate(String message) {
        return MiniMessage.miniMessage().deserialize(toMM(replaceStandardPlaceholders(message)));
    }
}

