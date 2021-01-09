package pl.com.weddingPlanner.validator;

import lombok.Getter;

@Getter
public abstract class AbstractValidator {

    protected String errorMsg;

    public abstract boolean isValid(String input);
}
