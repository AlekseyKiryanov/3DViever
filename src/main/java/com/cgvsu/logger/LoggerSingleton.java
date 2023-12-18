package com.cgvsu.logger;

public class LoggerSingleton {
    private static SimpleConsoleLogger INSTANCE;

    public static SimpleConsoleLogger getInstance(){
        if (INSTANCE == null){
            INSTANCE = new SimpleConsoleLogger();
        }
        return INSTANCE;
    }
}
