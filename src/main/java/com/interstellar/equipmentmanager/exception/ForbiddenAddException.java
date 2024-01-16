package com.interstellar.equipmentmanager.exception;

public class ForbiddenAddException extends RuntimeException {
    public ForbiddenAddException(String userId, String teamId) {
        super(String.format("User with id %s cannot be add into team with id %s, because he/she is already member of the team.", userId, teamId));
    }
}
