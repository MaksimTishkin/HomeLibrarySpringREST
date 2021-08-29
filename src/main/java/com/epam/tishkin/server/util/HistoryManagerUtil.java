package com.epam.tishkin.server.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HistoryManagerUtil {
    @Value("${pathToHistoryFile}")
    private String historyFile;
    private final Logger logger = LogManager.getLogger(HistoryManagerUtil.class);

    public void write(String message) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        try(FileWriter fileWriter = new FileWriter(historyFile, true)) {
            fileWriter.write(currentUsername + " - " + message + "\r\n");
        } catch(IOException e) {
            logger.error(e.getMessage());
        }
    }

    public List<String> read() {
        List<String> history = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(historyFile))) {
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