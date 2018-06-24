package com.grabbddemoapp.util;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class Utils {
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 531;

    public static void searchPlace(Activity activity, int mode, LatLng currentLatLng) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(mode).setBoundsBias(toBounds(new LatLng(currentLatLng.latitude, currentLatLng.longitude))).build(activity);
            activity.startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException ignored) {
        }
    }

    public static LatLngBounds toBounds(LatLng center) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        builder.include(center);
        return builder.build();
    }

}
