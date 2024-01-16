package com.interstellar.equipmentmanager.exception;

public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String entityId, String entityCount) {
        super(String.format("User with id %s cannot be removed because he/she is owner of number of teams (%s).", entityId, entityCount));
    }
}
