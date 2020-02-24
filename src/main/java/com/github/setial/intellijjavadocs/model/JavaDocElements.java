package com.github.setial.intellijjavadocs.model;

import org.jetbrains.annotations.NotNull;

/**
 * The enum Java doc elements.
 *
 * @author Sergey Timofiychuk
 */
public enum JavaDocElements {

    STARTING("/*"),
    ENDING("/"),
    NEW_LINE("\n"),
    TAG_START("@"),
    LINE_START("*"),
    WHITE_SPACE(" ");

    private String presentation;

    /**
     * Instantiates a new Java doc elements.
     *
     * @param value the value
     */
    JavaDocElements(String value) {
        presentation = value;
    }

    /**
     * Gets the presentation.
     *
     * @return the Presentation
     */
    @NotNull
    public String getPresentation() {
        return presentation;
    }

}
