package com.ds.todo.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by esutedja on 7/9/16.
 */
public class UnsupportedVersion {
    private String specifiedVersion;
    private List<String> supportedVersions;
    private String message;

    public UnsupportedVersion() {
        supportedVersions = new ArrayList<>();
    }

    public String getSpecifiedVersion() {
        return specifiedVersion;
    }

    public void setSpecifiedVersion(String specifiedVersion) {
        this.specifiedVersion = specifiedVersion;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addSupportedVersion(String version) {
        supportedVersions.add(version);
    }

    public List<String> getSupportedVersions() {
        return supportedVersions;
    }

}
