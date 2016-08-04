package com.github.karthyks.connections;

import android.app.Application;

import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by karthy07 on 7/18/2016.
 */

public class ConnectionsApp extends Application {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "zos9WA8mvAkicfNtH0g68rhVH";
    private static final String TWITTER_SECRET = "4mGkMAgWMXutrmNXsI9kXqIA0uIQFgfscP35XGSxSWXH1iBEGJ";

    private AuthCallback authCallback;

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());

        authCallback = new AuthCallback() {
            @Override
            public void success(DigitsSession session, String phoneNumber) {

            }

            @Override
            public void failure(DigitsException error) {

            }
        };
    }

    public AuthCallback getAuthCallback() {
        return authCallback;
    }
}
