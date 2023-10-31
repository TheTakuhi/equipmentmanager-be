package com.interstellar.equipmentmanager.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String entityName, String parameterName, String parameterValue) {
        super(String.format("%s searched by %s with value: '%s' could not be found.", entityName, parameterName, parameterValue));
    }

    public ResourceNotFoundException(String entityName) {
        super(String.format("%s searched could not be found.", entityName));
    }
}