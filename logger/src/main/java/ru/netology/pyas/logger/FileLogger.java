package ru.netology.pyas.logger;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileLogger implements Logger, Closeable {

    private final PrintWriter writer;

    public FileLogger(String filename) throws IOException {
        final boolean APPEND = true;
        writer = new PrintWriter(new FileWriter(filename, APPEND));
    }

    @Override
    public void log(String message) {
        writer.println(message);
        writer.flush();
    }

    public void close() {
        writer.close();
    }

}
