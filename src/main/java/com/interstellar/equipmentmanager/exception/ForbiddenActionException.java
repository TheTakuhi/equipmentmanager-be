package com.interstellar.equipmentmanager.exception;

public class ForbiddenActionException extends RuntimeException {
    public ForbiddenActionException(String massage) {
        super(massage);
    }


}
