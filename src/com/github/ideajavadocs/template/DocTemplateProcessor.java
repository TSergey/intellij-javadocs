package com.github.ideajavadocs.template;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface DocTemplateProcessor {

    @NotNull
    String merge(@NotNull String template, @NotNull Map<String, String> params);

    @NotNull
    String buildDescription(@NotNull String description);

    @NotNull
    String buildRawDescription(@NotNull String description);

}
