package ru.mrak.rent.config.exception;

/**
 * Ошика обработкисобытия not found (404)
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
    }
    
    public NotFoundException(String message) {
        super(message);
    }
}
