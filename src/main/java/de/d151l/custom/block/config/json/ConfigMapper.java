package de.d151l.custom.block.config.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.d151l.custom.block.CustomBlocks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;

/*
* This class is copied from https://github.com/BALTIC-GALAXY/utilities/blob/master/src/main/java/de/baltic/utilities/file/ConfigMapper.java
 */
public class ConfigMapper {

    private static final Gson gson = new Gson();
    private static final GsonBuilder gsonBuilder = new GsonBuilder();

    public static  <T> T readJson(File file, String fileName, Class<T> clazz) throws IOException {
        var targetFile = new File(file, fileName);
        if (!targetFile.exists()) throw new NullPointerException("Required file was not created yet!");
        var serialized = new String(Files.readAllBytes(targetFile.toPath()));
        return gson.fromJson(serialized, clazz);
    }

    public static void writeJson(File parent, String fileName, Object object) throws IOException {
        if (!parent.exists()) parent.mkdirs();
        var config = new File(parent, fileName);
        if (!config.exists()) {
            config.createNewFile();
        }
        try {
            var fileWriter = new FileWriter(config);
            fileWriter.write(
                    gsonBuilder.disableHtmlEscaping().setPrettyPrinting().serializeNulls().create().toJson(object)
            );
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException exception) {
            CustomBlocks.getInstance().getLogger().log(Level.ALL, "Could not write to file " + fileName + "!", exception);
        }
    }

    public static <T> T getOrCreate(File file, String fileName, Object object, Class<T> clazz) throws IOException {
        var targetFile = new File(file, fileName);
        if (!targetFile.exists()) {
            writeJson(file, fileName, object);
        }
        return readJson(file, fileName, clazz);
    }

}