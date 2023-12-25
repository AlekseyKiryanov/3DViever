package com.cgvsu.editing_model;

public class BadPolygonException extends RuntimeException {
    public BadPolygonException(String errorMessage, int lineInd) {
        super("Поврежденный полигон в строке - " + lineInd + ": " + errorMessage);
    }
}
