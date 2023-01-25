package org.dev.ad.utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class FileUtils {

    public static boolean writeToFile(Path filePath, String fileContent) {
        File file = filePath.toFile();
        if (file.exists() && file.isFile()) {
            writeToFile(file.getPath(), fileContent);
            return true;
        }
        return false;
    }

    public static boolean writeToFile(String filePath, String fileContent) {
        try {
            FileWriter writer = new FileWriter(filePath, false);
            writer.write(fileContent);
            writer.close();
            return true;
        } catch (Exception e) {
            System.err.println("Failed to save last trade prices : {}" + e.getMessage());
            return false;
        }
    }

    public static String readFileContent(Path filePath) {
        try {
            return Files.readAllLines(filePath).stream().collect(Collectors.joining());
        } catch (IOException e) {
            System.err.println("Failed to readFileContent : {}" + e.getMessage());
            return StringUtils.EMPTY;
        }
    }
}
