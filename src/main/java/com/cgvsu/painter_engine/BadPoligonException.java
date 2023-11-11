package com.cgvsu.painter_engine;

public class BadPoligonException extends RuntimeException {
    public BadPoligonException(String errorMessage, int lineInd) {
        super("Поврежденный полигон:" + lineInd + ". " + errorMessage);
    }
}
