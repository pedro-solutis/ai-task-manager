package br.com.solutis.backend.exception;

public class InvalidTaskTransitionException extends BusinessRuleException {
    public InvalidTaskTransitionException(String message) {
        super(message);
    }
}
