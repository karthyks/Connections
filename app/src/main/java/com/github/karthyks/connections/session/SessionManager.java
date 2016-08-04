package com.github.karthyks.connections.session;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.OnAccountsUpdateListener;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.github.karthyks.connections.localstore.LocalStoreContract;
import com.github.karthyks.connections.model.UserModel;
import com.github.karthyks.connections.utils.AppUtils;

/**
 * Created by karthy07 on 7/19/2016.
 */

public class SessionManager implements OnAccountsUpdateListener {

    public static final String TAG = SessionManager.class.getSimpleName();
    public static final String ACCOUNT_TYPE = "com.github.karthyks.connections.localstore.account";
    private static final String PREF_SESSION_EXPIRED = "pref_session_expired";
    private static final String PREF_USER_NAME = "pref_user_name";
    private static final String ACTION_SESSION_EXPIRED = "action_session_expired";
    private static final long SYNC_FREQUENCY = 2 * 60 * 1000;
    private static SessionManager instance;
    private Context context;

    private UserModel signedInUser;

    private SessionManager(Context context) {
        this.context = context;
    }

    public static SessionManager getUserSession(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    @Override
    public void onAccountsUpdated(Account[] accounts) {

    }

    public UserModel getAuthenticatedUser() {
        if (signedInUser == null) {
            AccountManager accountManager = AccountManager.get(context);
            UserModel.fromAccount(getActiveAccount(), accountManager);
        }
        return null;
    }

    public Account getActiveAccount() {
        AccountManager accountManager = AccountManager.get(context);
        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Account[] accounts = accountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length > 0) {
            return accounts[0];
        }
        return null;
    }

    public boolean onRemoveAuthenticatedUser(Account account) {
        AppUtils.getInstance(context).putInSharedPrefs(PREF_SESSION_EXPIRED, true);
        signedInUser = null;
        return true;
    }

    public void onAddNewAccount() {
        AppUtils.getInstance(context).putInSharedPrefs(PREF_USER_NAME, signedInUser.getAccountName());
        AppUtils.getInstance(context).putInSharedPrefs(PREF_SESSION_EXPIRED, false);
    }

    public boolean isUserSessionExpired() {
        return (getAuthenticatedUser() != null && getAuthenticatedUser().isExpired());
    }

    public boolean isUserAuthenticated() {
        return (getAuthenticatedUser() != null && !getAuthenticatedUser().isExpired());
    }

    public void setUserSessionExpired(boolean isExpired) {
        AccountManager accountManager = AccountManager.get(context);
        if (getActiveAccount() == null) {
            return;
        }
        accountManager.setUserData(getActiveAccount(), UserModel.EXTRAS_SESSION_EXPIRED,
                String.valueOf(isUserSessionExpired()));
        if (signedInUser != null) {
            signedInUser.setIsExpired(isExpired);
        }

        if (isExpired) {
            broadcastSessionExpired();
        }
    }

    public void updateUser(UserModel userModel) {
        Account account = getActiveAccount();
        if (account != null && account.name.equals(userModel.getAccountName())) {
            AccountManager accountManager = AccountManager.get(context);
            accountManager.setPassword(account, userModel.getPassword());
            accountManager.setUserData(account, UserModel.EXTRAS_SESSION_EXPIRED,
                    String.valueOf(userModel.isExpired()));
            this.signedInUser = userModel;
        }
    }

    public void updateUserBundle(UserModel userModel) {
        Account account = getActiveAccount();
        if (account != null && account.name.equals(userModel.getAccountName())) {
            Bundle bundle = userModel.toBundle();
            AccountManager accountManager = AccountManager.get(context);
            for (String key : bundle.keySet()) {
                accountManager.setUserData(account, key, bundle.getString(key));
            }
            signedInUser = userModel;
        }
    }

    private void broadcastSessionExpired() {
        Intent intent = new Intent();
        intent.setAction(ACTION_SESSION_EXPIRED);
        context.sendBroadcast(intent);
    }

    public void setSignedInUser(UserModel userModel) {
        Account account = getActiveAccount();
        if (account != null && account.name.equals(userModel.getAccountName())) {
            throw new UnsupportedOperationException("Only one user is allowed");
        }

        AccountManager accountManager = AccountManager.get(context);
        account = new Account(userModel.getAccountName(), ACCOUNT_TYPE);
        if (accountManager.addAccountExplicitly(account, userModel.getPassword(),
                userModel.toBundle())) {
            ContentResolver.setIsSyncable(account, LocalStoreContract.CONTENT_AUTHORITY, 1);
            ContentResolver.setSyncAutomatically(account, LocalStoreContract.CONTENT_AUTHORITY,
                    true);
            ContentResolver.addPeriodicSync(account, LocalStoreContract.CONTENT_AUTHORITY,
                    new Bundle(), SYNC_FREQUENCY);
        }
        signedInUser = userModel;
    }

    public boolean hasActiveAccount() {
        return getActiveAccount() != null;
    }
}
