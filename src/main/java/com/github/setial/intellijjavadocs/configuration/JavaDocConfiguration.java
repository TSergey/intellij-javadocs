package com.github.setial.intellijjavadocs.configuration;

import com.github.setial.intellijjavadocs.model.settings.JavaDocSettings;
import org.jetbrains.annotations.Nullable;

/**
 * The interface Java doc configuration.
 *
 * @author Sergey Timofiychuk
 */
public interface JavaDocConfiguration {


    /**
     * The constant COMPONENT_NAME.
     */
    String COMPONENT_NAME = "JavaDocConfiguration";

    /**
     * The constant COMPONENT_VERSION.
     */
    String COMPONENT_CONFIG_VERSION = "4.0.1";

    /**
     * The constant COMPONENT_CONFIG_PATH.
     */
    String COMPONENT_CONFIG_PATH = "intellij-javadocs-" + COMPONENT_CONFIG_VERSION + ".xml";

    /**
     * Gets the configuration.
     *
     * @return the Configuration
     */
    @Nullable
    JavaDocSettings getConfiguration();

    void setupTemplates();

}
