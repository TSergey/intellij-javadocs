package com.github.setial.intellijjavadocs.configuration;

import com.github.setial.intellijjavadocs.model.settings.JavaDocSettings;

/**
 * The interface Java doc configuration.
 *
 * @author Sergey Timofiychuk
 */
public interface JavaDocConfiguration {

    String COMPONENT_NAME = "JavaDocConfiguration";

    /**
     * Gets the configuration.
     *
     * @return the Configuration
     */
    JavaDocSettings getConfiguration();

}
