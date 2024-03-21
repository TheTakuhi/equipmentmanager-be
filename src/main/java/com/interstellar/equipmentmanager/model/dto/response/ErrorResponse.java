package com.interstellar.equipmentmanager.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
public class ErrorResponse {
    private final int status;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ValidationError> errors;
    private record ValidationError(String field, String message) { }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) errors = new ArrayList<>();

        errors.add(new ValidationError(field, message));
    }
}
