package com.example.demowebflux.errors;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ApiValidationErrorResponse extends ApiResponse{
    private List<String> errors;

    public ApiValidationErrorResponse(List<String> errors, String message) {
        super(400, message);
        this.errors = errors;
    }
}
