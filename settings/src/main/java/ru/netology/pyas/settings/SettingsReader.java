package ru.netology.pyas.settings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SettingsReader {

    private final String file;

    public SettingsReader(String file) {
        this.file = file;
    }

    public Settings read() throws IOException {
        final Settings settings = new Settings();
        final BufferedReader reader = new BufferedReader(new FileReader(file));

        String line;
        while (null != (line = reader.readLine())) {
            String parts[] = line.split("\s*=\s*");
            switch (parts[0].toLowerCase()) {
                case "port":
                    settings.port = Integer.parseInt(parts[1]);
                    break;

                case "logfile":
                    settings.logfile = parts[1];
                    break;

                case "host":
                    settings.host = parts[1];
                    break;
            }
        }

        reader.close();

        return settings;
    }
}
