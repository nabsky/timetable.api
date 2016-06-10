package ru.nabsky.models;

import lombok.Data;

@Data
public class ValidationResult {
    private String errorMessage;
    private boolean valid;

    public ValidationResult(String errorMessage){
        valid = false;
        this.errorMessage = errorMessage;
    }

    public ValidationResult(){
        valid = true;
    }
}
