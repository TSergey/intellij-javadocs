package com.github.setial.intellijjavadocs.configuration;

import com.github.setial.intellijjavadocs.model.settings.JavaDocSettings;
import com.intellij.openapi.components.ApplicationComponent;
import org.jetbrains.annotations.Nullable;

/**
 * The interface Java doc configuration.
 *
 * @author Sergey Timofiychuk
 */
public interface JavaDocConfiguration extends ApplicationComponent {

    String COMPONENT_VERSION = "2.0.1";
    String COMPONENT_NAME = "JavaDocConfiguration_" + COMPONENT_VERSION;

    /**
     * Gets the configuration.
     *
     * @return the Configuration
     */
    @Nullable
    JavaDocSettings getConfiguration();

}
