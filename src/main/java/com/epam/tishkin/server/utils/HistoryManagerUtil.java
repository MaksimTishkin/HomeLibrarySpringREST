package com.epam.tishkin.server.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HistoryManagerUtil {
    private final Logger logger = LogManager.getLogger("HistoryWriterUtil.class");
    private final Properties properties;

    {
        properties = new Properties();
        try (FileReader readerForProperties = new FileReader("src/main/resources/config.properties")) {
            properties.load(readerForProperties);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void write(String message) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        try(FileWriter fileWriter = new FileWriter(properties.getProperty("pathToHistoryFile"), true)) {
            fileWriter.write(currentUsername + " - " + message + "\r\n");
        } catch(IOException e) {
            logger.error(e.getMessage());
        }
    }

    public List<String> read() {
        List<String> history = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(properties.getProperty("pathToHistoryFile")))) {
            String line;
            while((line = reader.readLine()) != null) {
                history.add(line);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return history;
    }
}
