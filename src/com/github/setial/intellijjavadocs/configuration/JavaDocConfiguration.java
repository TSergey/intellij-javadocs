package com.github.setial.intellijjavadocs.configuration;

import com.github.setial.intellijjavadocs.model.settings.JavaDocSettings;
import org.jetbrains.annotations.Nullable;

/**
 * The interface Java doc configuration.
 *
 * @author Sergey Timofiychuk
 */
public interface JavaDocConfiguration {

    String COMPONENT_NAME = "JavaDocConfiguration_1.0";

    /**
     * Gets the configuration.
     *
     * @return the Configuration
     */
    @Nullable
    JavaDocSettings getConfiguration();

}
