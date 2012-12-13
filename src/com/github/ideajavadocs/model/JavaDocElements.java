package com.github.ideajavadocs.model;

import org.jetbrains.annotations.NotNull;

public enum JavaDocElements {

    STARTING_ASTERISK("/**"),
    ENDING_ASTERISK("*/"),
    NEW_LINE("\n"),
    TAG_START("@"),
    LINE_START("*");

    private String presentation;

    JavaDocElements(String s) {
        presentation = s;
    }

    @NotNull
    public String getPresentation() {
        return presentation;
    }

}