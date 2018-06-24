package com.grabbddemoapp;

import android.app.Application;
import android.content.Context;


import java.lang.ref.WeakReference;

import io.paperdb.Paper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


/**
 * Developer: Sandy
 *
 * The Application class
 */

public class MyApplication extends Application {

    private static WeakReference<Context> mWeakReference;

    /**
     * Getter to access Singleton instance
     *
     * @return instance of MyApplication
     */
    public static Context getAppContext() {
        return mWeakReference.get();
    }

    @Override
    public void onCreate() {

        super.onCreate();
        Paper.init(this);
        mWeakReference = new WeakReference<Context>(this);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}
