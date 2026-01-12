package io.github.mabid.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.JOptionPane;

public class GameProperties {

    private GameProperties() {
    }

    public static void saveConfig(String gameName, String location, String launchCommands) {
        try {
            File dir = new File("saved_games");
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File configFile = new File(dir, gameName + ".gc");

            Properties props = new Properties();
            props.setProperty("game.name", gameName);
            props.setProperty("game.location", location);
            props.setProperty("game.launchCommands", launchCommands);

            try (FileOutputStream out = new FileOutputStream(configFile)) {
                props.store(out, "Game Configuration");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                "Failed to save configuration:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static GameConfig readConfig(String gameName) {
        File configFile = new File("saved_games", gameName + ".gc");
        if (!configFile.exists()) {
            JOptionPane.showMessageDialog(
                null,
                "Configuration file not found for game: " + gameName,
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        try (FileInputStream in = new FileInputStream(configFile)) {
            Properties props = new Properties();
            props.load(in);

            String name = props.getProperty("game.name");
            String location = props.getProperty("game.location");
            String launchCommands = props.getProperty("game.launchCommands");

            return new GameConfig(name, location, launchCommands);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(
                null,
                "Failed to read configuration:\n" + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return null;
        }
    }
    
    public static List<String> listSavedGames() {
        List<String> games = new ArrayList<>();
        File dir = new File("saved_games");

        if (!dir.exists() || !dir.isDirectory()) {
            return games; 
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".gc"));
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                // Remove ".gc" extension
                games.add(fileName.substring(0, fileName.length() - 3));
            }
        }

        return games;
    }

    public static List<GameConfig> listSavedGameConfigs() {
        List<GameConfig> configs = new ArrayList<>();
        List<String> gameNames = listSavedGames();

        for (String name : gameNames) {
            GameConfig config = readConfig(name);
            if (config != null) {
                configs.add(config);
            }
        }

        return configs;
    }

    public static class GameConfig {
        public String name;
        public String location;
        public String launchCommands;

        public GameConfig(String name, String location, String launchCommands) {
            this.name = name;
            this.location = location;
            this.launchCommands = launchCommands;
        }
    }
    
}