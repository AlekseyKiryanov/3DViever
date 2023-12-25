package com.cgvsu.logger;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class SimpleConsoleLogger implements System.Logger {

    private SimpleConsoleLogger() {
    }

    private static SimpleConsoleLogger INSTANCE;

    public static SimpleConsoleLogger getInstance(){
        if (INSTANCE == null){
            INSTANCE = new SimpleConsoleLogger();
        }
        return INSTANCE;
    }

    private Level sameLevel = Level.OFF;

    public void setSameLevel(Level sameLevel) {
        this.sameLevel = sameLevel;
    }

    public Level getSameLevel() {
        return sameLevel;
    }

    @Override
    public String getName() {
        return "SimpleConsoleLogger";
    }

    @Override
    public boolean isLoggable(Level level) {
        return sameLevel.getSeverity() <= level.getSeverity();
    }

    @Override
    public void log(Level level, String msg) {
        System.out.println(level + " " + msg);
    }

    @Override
    public void log(Level level, Object obj) {
        System.out.println(level + " " + obj.toString());
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String msg, Throwable thrown) {
        System.out.printf("ConsoleLogger [%s]: %s - %s%n", level, msg, thrown);
    }

    @Override
    public void log(Level level, ResourceBundle bundle, String format, Object... params) {
        System.out.printf("ConsoleLogger [%s]: %s%n", level,
                MessageFormat.format(format, params));
    }
}
