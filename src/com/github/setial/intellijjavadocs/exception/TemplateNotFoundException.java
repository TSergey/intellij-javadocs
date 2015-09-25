package com.github.setial.intellijjavadocs.exception;

/**
 * The type Template not found exception.
 *
 * @author Sergey Timofiychuk
 */
public class TemplateNotFoundException extends RuntimeException {

    /**
     * Instantiates a new Template not found exception.
     *
     * @param message the message
     */
    public TemplateNotFoundException(String message) {
        super(message);
    }
}
