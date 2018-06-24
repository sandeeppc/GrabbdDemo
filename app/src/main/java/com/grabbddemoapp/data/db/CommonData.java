package com.grabbddemoapp.data.db;

import io.paperdb.Paper;

/**
 * Developer: Sandy
 */
public final class CommonData {

    private static final String PAPER_DEVICE_TOKEN = "paper_device_token";
    private static final String PAPER_ACCESS_TOKEN = "paper_access_token";

    private static final String clientId = "5HXLUCALMHL1DPYNVECTD5CVKP5E2MKGHWGH1DRTYJRV0LCA";
    private static final String clientSecret = "PHMYSOEFUQ2PAADLNCLZREAHB3A5HQNZO00V4NA5HKIMLDAX";

    public static String getClientId() {
        return clientId;
    }

    public static String getClientSecret() {
        return clientSecret;
    }

    /**
     * Prevent instantiation
     */
    private CommonData() {
    }

    //=================================== FCM Token ==================================
    /**
     * Update fcm token.
     *
     * @param token the fcm token
     */
    public static void updateFcmToken(final String token) {
        Paper.book().write(PAPER_DEVICE_TOKEN, token);
    }

    /**
     * Gets fcm token.
     *
     * @return the fcm token
     */
    public static String getFcmToken() {
        return Paper.book().read(PAPER_DEVICE_TOKEN);
    }


    //=================================== Access Token ===============================
    /**
     * Save access token.
     *
     * @param accessToken the access token
     */
    public static void saveAccessToken(final String accessToken) {
        Paper.book().write(PAPER_ACCESS_TOKEN, "bearer " + accessToken);
    }

    /**
     * Gets access token.
     *
     * @return the access token
     */
    public static String getAccessToken() {
        return Paper.book().read(PAPER_ACCESS_TOKEN);
    }


    /**
     * Clear data.
     */
    public static void clearData() {
        String fcmToken = getFcmToken();
        Paper.book().destroy();
        updateFcmToken(fcmToken);
    }

}
