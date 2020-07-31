package ru.mrak.rent.config.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Ошибки валидации
 */
@Getter
@NoArgsConstructor
public class ValidationException extends RuntimeException {

    private final Set errors = new LinkedHashSet<>();

    @SuppressWarnings("unchecked")
    public void put(String message) {
        errors.add(message);
    }

    @SuppressWarnings("unchecked")
    public void put(Object error) {
        errors.add(error);
    }

    @SuppressWarnings("unchecked")
    public void putAll(List putErrors) {
        errors.addAll(putErrors);
    }

    @SuppressWarnings("unchecked")
    public void putAll(ValidationException validateException) {
        errors.addAll(validateException.getErrors());
    }

    public void isErrors() throws ValidationException {
        if (!errors.isEmpty()) {
            throw this;
        }
    }
    
    @SuppressWarnings("unchecked")
    public ValidationException(String message) {
        super();
        errors.add(message);
    }
}
