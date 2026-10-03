package br.com.solutis.backend.exception;

public class CircularHierarchyException extends BusinessRuleException {
    public CircularHierarchyException(String message) {
        super(message);
    }
}
