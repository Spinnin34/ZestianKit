package p.zestianKits.utils;

import java.io.*;

public class KitIDGenerator {
    private final File file;
    private long lastId;

    public KitIDGenerator(File pluginFolder) {
        this.file = new File(pluginFolder, "kit_counter.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                lastId = line != null ? Long.parseLong(line) : 0;
            } catch (IOException | NumberFormatException e) {
                lastId = 0;
            }
        } else {
            lastId = 0;
        }
    }

    public long getNextId() {
        lastId++;
        save();
        return lastId;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(Long.toString(lastId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

