package ru.netology.pyas.logger;

public class FileLogger implements Logger {

    @Override
    public void log(String message) {
        System.out.println(message);
    }


}
