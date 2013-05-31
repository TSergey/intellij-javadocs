package com.github.setial.intellijjavadocs.model.settings;

import java.util.Set;

/**
 * The type General settings.
 *
 * @author Sergey Timofiychuk
 */
public class GeneralSettings {

    private Mode mode;
    private Set<Level> levels;
    private Set<Visibility> visibilities;
    private boolean overriddenMethods;
    private boolean splittedClassName;

    /**
     * Gets javadoc update mode.
     *
     * @return the mode
     */
    public Mode getMode() {
        return mode;
    }

    /**
     * Sets javadoc update mode.
     *
     * @param mode the mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    /**
     * Gets javadoc generate levels.
     *
     * @return the levels
     */
    public Set<Level> getLevels() {
        return levels;
    }

    /**
     * Sets javadoc generate levels.
     *
     * @param levels the levels
     */
    public void setLevels(Set<Level> levels) {
        this.levels = levels;
    }

    /**
     * Gets javadoc generate visibilities.
     *
     * @return the visibilities
     */
    public Set<Visibility> getVisibilities() {
        return visibilities;
    }

    /**
     * Sets javadoc generate visibilities.
     *
     * @param visibilities the visibilities
     */
    public void setVisibilities(Set<Visibility> visibilities) {
        this.visibilities = visibilities;
    }

    /**
     * Gets flag that shows whether javadoc should be generated on overridden methods.
     *
     * @return the flag value
     */
    public boolean isOverriddenMethods() {
        return overriddenMethods;
    }

    /**
     * Set flag that shows whether javadoc should be generated on overridden methods.
     *
     * @param overriddenMethods the overridden methods
     */
    public void setOverriddenMethods(boolean overriddenMethods) {
        this.overriddenMethods = overriddenMethods;
    }

    public boolean isSplittedClassName() {
        return splittedClassName;
    }

    public void setSplittedClassName(boolean splittedClassName) {
        this.splittedClassName = splittedClassName;
    }

}
