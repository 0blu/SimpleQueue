package wtf.blu.simpleQueue.util;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WtfYamlConfiguration extends YamlConfiguration {

    private final File file;
    private final Logger logger;

    public WtfYamlConfiguration(Logger logger, File file, String defaultConfigResourcePath) {
        this(logger, file);

        try (InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream(defaultConfigResourcePath))) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);
            setDefaults(defaultConfig);
            options().copyDefaults(true);
            save();
        }
        catch (Exception e) {
            logException(logger, "An error occurred while setting the defaults in the config.", e);
        }
    }

    public WtfYamlConfiguration(Logger logger, File file) {
        this.file = file;
        this.logger = logger;

        file.getParentFile().mkdirs();
        if(!file.exists()) {
            try {
                file.createNewFile();
            }
            catch (Exception e) {
                logException(logger, "An error occurred while creating the config.", e);
            }
        }
        reload();
    }

    public void reload() {
        try {
            load(file);
        } catch (Exception e) {
            logException(logger, "An error occurred while reloading the config.", e);
        }
    }

    public void save() {
        try {
            save(file);
        } catch (Exception e) {
            logException(logger, "An error occurred while saving the config.", e);
        }
    }

    private static void logException(Logger logger, String message, Exception e) {
        logger.log(Level.WARNING, message, e);
    }
}
