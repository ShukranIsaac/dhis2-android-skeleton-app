package com.example.android.androidskeletonapp.data;

import android.content.Context;

import org.hisp.dhis.android.core.d2manager.D2Configuration;

import java.util.Collections;

public class Sdk {

    public static D2Configuration getD2Configuration(Context context) {
        // TODO Add configuration
        return D2Configuration.builder()
                .appName("Dhis2 Skeleton App").appVersion("1.0.0")
                .context(context)
                .readTimeoutInSeconds(30).connectTimeoutInSeconds(30).writeTimeoutInSeconds(30)
                //.networkInterceptors(Collections.singletonList(new StethoInterceptor()))
                .build();
    }
}