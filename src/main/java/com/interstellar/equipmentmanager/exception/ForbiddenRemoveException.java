package com.interstellar.equipmentmanager.exception;

public class ForbiddenRemoveException extends RuntimeException {
    public ForbiddenRemoveException(String userId, String teamId) {
        super(String.format("User with id %s cannot be removed from team with id %s, because he/she is not member of the team.", userId, teamId));
    }
}
