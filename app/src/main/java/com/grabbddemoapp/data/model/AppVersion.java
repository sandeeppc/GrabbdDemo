package com.grabbddemoapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Developer: Sandy
 */
public class AppVersion {
    @SerializedName("latestAndroidVersion")
    @Expose
    private int latestAndroidVersion;
    @SerializedName("criticalAndroidVersion")
    @Expose
    private int criticalAndroidVersion;
    @SerializedName("updateMessageAtPopup")
    @Expose
    private String updateMessageAtPopup;
    @SerializedName("updateTitleAtPopup")
    @Expose
    private String updateTitleAtPopup;

    /**
     * Gets latest android version.
     *
     * @return the latest android version
     */
    public int getLatestAndroidVersion() {
        return latestAndroidVersion;
    }

    /**
     * Gets critical android version.
     *
     * @return the critical android version
     */
    public int getCriticalAndroidVersion() {
        return criticalAndroidVersion;
    }

    /**
     * Gets update message at popup.
     *
     * @return the update message at popup
     */
    public String getUpdateMessageAtPopup() {
        return updateMessageAtPopup;
    }

    /**
     * Gets update title at popup.
     *
     * @return the update title at popup
     */
    public String getUpdateTitleAtPopup() {
        return updateTitleAtPopup;
    }

}