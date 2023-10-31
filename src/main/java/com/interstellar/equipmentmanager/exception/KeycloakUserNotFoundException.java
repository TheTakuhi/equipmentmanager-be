package com.interstellar.equipmentmanager.exception;

public class KeycloakUserNotFoundException extends RuntimeException {

    public KeycloakUserNotFoundException(String entityName, String parameterName, String parameterValue) {
        super(String.format("%s searched by %s with value: '%s' could not be found", entityName, parameterName, parameterValue));
    }
}
