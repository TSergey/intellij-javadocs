package com.github.setial.intellijjavadocs.configuration;

import com.github.setial.intellijjavadocs.model.settings.JavaDocSettings;
import org.jetbrains.annotations.Nullable;

/**
 * The interface Java doc configuration.
 *
 * @author Sergey Timofiychuk
 */
public interface JavaDocConfiguration {

    String COMPONENT_VERSION = "1.0.7";
    String COMPONENT_NAME = "JavaDocConfiguration_" + COMPONENT_VERSION;

    /**
     * Gets the configuration.
     *
     * @return the Configuration
     */
    @Nullable
    JavaDocSettings getConfiguration();

}
