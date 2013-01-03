package com.github.ideajavadocs.configuration;

import com.github.ideajavadocs.model.settings.JavaDocSettings;

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
