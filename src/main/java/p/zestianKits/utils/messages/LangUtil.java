package p.zestianKits.utils.messages;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import p.zestianKits.ZestianKits;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class LangUtil {


    private static FileConfiguration langConfig = null;
    private static File langFile = null;

    public LangUtil() {
        saveDefaultLang();
    }

    public static void reloadLang() {
        if (langFile == null) {
            langFile = new File(ZestianKits.getInstance().getDataFolder(), "lang.yml");
        }
        langConfig = YamlConfiguration.loadConfiguration(langFile);

        InputStream defaultLangStream = ZestianKits.getInstance().getResource("lang.yml");
        if (defaultLangStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultLangStream));
            langConfig.setDefaults(defaultConfig);
        }
    }

    public static FileConfiguration getLang() {
        if (langConfig == null) {
            reloadLang();
        }
        return langConfig;
    }

    public void saveDefaultLang() {
        if (langFile == null) {
            langFile = new File(ZestianKits.getInstance().getDataFolder(), "lang.yml");
        }
        if (!langFile.exists()) {
            ZestianKits.getInstance().saveResource("lang.yml", false);
        }
    }

    public static Component getString(String path, boolean usePrefix) {
        String rawString = getLang().getString(path);

        if (rawString == null) {
            return Component.text("Texto por defecto para " + path);
        }

        if (usePrefix) {
            String prefix = getLang().getString("messages.prefix", "");
            rawString = prefix + rawString;
        }

        return ColorTranslator.translate(rawString);
    }

    public static Component getString(String path, boolean usePrefix, Map<String, String> placeholders) {
        String rawString = getLang().getString(path);

        if (rawString == null) {
            return Component.text("Texto por defecto para " + path);
        }

        if (usePrefix) {
            String prefix = getLang().getString("messages.prefix", "");
            rawString = prefix + rawString;
        }

        Component component = ColorTranslator.translate(rawString);

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            component = component.replaceText(TextReplacementConfig.builder()
                    .matchLiteral(entry.getKey())
                    .replacement(Component.text(entry.getValue()))
                    .build());
        }

        return component;
    }

    public static Component getString(Player player, String path, boolean usePrefix) {
        String rawString = getLang().getString(path);

        if (rawString == null) {
            return Component.text("Texto por defecto para " + path);
        }

        if (usePrefix) {
            String prefix = getLang().getString("messages.prefix", "");
            rawString = prefix + rawString;
        }

        return ColorTranslator.translate(PlaceholderAPI.setPlaceholders(player, rawString));
    }

    public static Component getMessageList(String path) {
        List<String> messages = getLang().getStringList(path);
        Component formattedMessages = Component.empty();

        for (String message : messages) {
            Component coloredMessage = ColorTranslator.translate(message);

            formattedMessages = formattedMessages.append(coloredMessage).append(Component.newline());
        }

        return formattedMessages;
    }

    public static Component getMessageList(String path, Map<String, String> placeholders) {
        List<String> messages = getLang().getStringList(path);
        Component result = Component.empty();

        for (String message : messages) {
            Component coloredMessage = LegacyComponentSerializer.legacyAmpersand().deserialize(message);

            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                coloredMessage = coloredMessage.replaceText(
                        TextReplacementConfig.builder()
                                .matchLiteral(entry.getKey())
                                .replacement(Component.text(entry.getValue()))
                                .build()
                );
            }

            result = result.append(coloredMessage).append(Component.newline());
        }

        return result;
    }
}
