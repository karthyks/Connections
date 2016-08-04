package com.github.karthyks.connections.account;

import android.accounts.AbstractAccountAuthenticator;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.karthyks.connections.login.LoginActivity;
import com.github.karthyks.connections.session.SingleAccountAuthenticator;

/**
 * Created by karthy07 on 7/19/2016.
 */

public class AuthenticationService extends Service {
    private static final String TAG = AuthenticationService.class.getSimpleName();
    private AbstractAccountAuthenticator authenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: Service created");
        authenticator = new SingleAccountAuthenticator(this, null, LoginActivity.class);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
