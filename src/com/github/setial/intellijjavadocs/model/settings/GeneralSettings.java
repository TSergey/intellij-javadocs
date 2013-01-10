package com.github.setial.intellijjavadocs.model.settings;

import java.util.Set;

public class GeneralSettings {

    private Mode mode;
    private Set<Level> levels;
    private Set<Visibility> visibilities;
    private boolean overriddenMethods;

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public Set<Level> getLevels() {
        return levels;
    }

    public void setLevels(Set<Level> levels) {
        this.levels = levels;
    }

    public Set<Visibility> getVisibilities() {
        return visibilities;
    }

    public void setVisibilities(Set<Visibility> visibilities) {
        this.visibilities = visibilities;
    }

    public Boolean isOverriddenMethods() {
        return overriddenMethods;
    }

    public void setOverriddenMethods(Boolean overriddenMethods) {
        this.overriddenMethods = overriddenMethods;
    }

}
