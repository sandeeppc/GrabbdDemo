package com.grabbddemoapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.Task;
import com.grabbddemoapp.fragment.ExploreFragment;
import com.grabbddemoapp.util.Utils;
import com.grabbddemoapp.fragment.SearchFragment;


import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements ExploreFragment.LocationAndAddress , SearchFragment.LocationAndAddress{

    private static final int LOCATION_SERVICE_CALLBACK = 11;
    private static final int LOCATION_SERVICE_RESOLUTION = 12;
    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener pageChangeListener;
    private MenuItem prevMenuItem;
    private ExploreFragment mExploreFragment;
    private SearchFragment mSearchFragment;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Activity mActivity;
    private Location currentLocation;
    private Double latitude = 0.0;
    private Double longitude = 0.0;

    private boolean isExplore = true;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_explore:
                    viewPager.setCurrentItem(0, true);
                    return true;
                case R.id.navigation_search:
                    viewPager.setCurrentItem(1, true);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mActivity = this;
        viewPager = findViewById(R.id.pager);
        mExploreFragment = new ExploreFragment();
        mSearchFragment = new SearchFragment();

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    navigation.getMenu().getItem(0).setChecked(false);
                }
                navigation.getMenu().getItem(position).setChecked(true);
                prevMenuItem = navigation.getMenu().getItem(position);

                isExplore = position == 0;
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        };

        checkLocationPermission();

    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPager.addOnPageChangeListener(pageChangeListener);
        startLocationUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewPager.removeOnPageChangeListener(pageChangeListener);
        stopLocationUpdate();
    }

    @NotNull
    @Override
    public Location getCurrentLocation() {
        if (currentLocation == null) {
            currentLocation = new Location("LOCATION");
            currentLocation.setLatitude(0.0);
            currentLocation.setLongitude(0.0);
        }
        return currentLocation;
    }


    private class SimpleFragmentPagerAdapter extends FragmentStatePagerAdapter {

        SimpleFragmentPagerAdapter(final FragmentManager mFragmentManager) {
            super(mFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return mExploreFragment;
            } else {
                return mSearchFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

    private void locationInit() {

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(final LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    currentLocation = location;
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();

                    stopLocationUpdate();
                }
            }
        };

        createLocationRequest();
        startLocationUpdate();
    }

    /* ============================== For location permission ============================= */

    private void checkLocationPermission() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_SERVICE_CALLBACK);
        } else {
            locationInit();
        }

    }

    /* ============================== For location request ============================= */

    private void createLocationRequest() {
        try {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(3000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this)
                    .checkLocationSettings(new LocationSettingsRequest.Builder().addLocationRequest(locationRequest).build());

            task.addOnFailureListener(e -> {
                if (e instanceof ResolvableApiException) {
                    try {
                        ((ResolvableApiException) e).startResolutionForResult(this, LOCATION_SERVICE_RESOLUTION);
                    } catch (IntentSender.SendIntentException e1) {
                        e1.printStackTrace();
                    }
                }
            });
        } catch (Exception ignored) {
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }

    }

    private void stopLocationUpdate() {
        if (fusedLocationProviderClient != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        switch (requestCode) {
            case LOCATION_SERVICE_CALLBACK:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        locationInit();
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.ACCESS_FINE_LOCATION)) {
                            checkLocationPermission();
                        } else {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, LOCATION_SERVICE_CALLBACK);
                        }
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /* Code to analyse the User action on asking to enable gps */
        switch (requestCode) {
            case Utils.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Place place = PlaceAutocomplete.getPlace(this, data);
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    if (isExplore)
                        mExploreFragment.setAddress(latitude, longitude);
                    else
                        mSearchFragment.setAddress(latitude, longitude);
                }
                break;

            case LOCATION_SERVICE_RESOLUTION:
                createLocationRequest();
                break;
            case LOCATION_SERVICE_CALLBACK:
                Toast.makeText(this, R.string.msg_location, Toast.LENGTH_LONG).show();
                checkLocationPermission();
                break;
            default:
                break;
        }
    }

}