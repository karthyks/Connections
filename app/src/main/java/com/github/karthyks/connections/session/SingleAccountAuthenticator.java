package com.github.karthyks.connections.session;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresPermission;
import android.widget.Toast;

import com.github.karthyks.connections.R;

import static android.Manifest.permission.ACCOUNT_MANAGER;
import static android.Manifest.permission.GET_ACCOUNTS;

/**
 * Created by karthy07 on 7/19/2016.
 */

public class SingleAccountAuthenticator extends AbstractAccountAuthenticator {

    private final Context context;
    private final SessionManager sessionManager;
    private final Class<? extends Activity> classLoginActivity;

    public SingleAccountAuthenticator(Context context,
                                      SessionManager sessionManager,
                                      Class<? extends Activity> classLoginActivity) {
        super(context);
        this.context = context;
        this.sessionManager = sessionManager;
        this.classLoginActivity = classLoginActivity;
    }

    @RequiresPermission(allOf = {GET_ACCOUNTS, ACCOUNT_MANAGER})
    @Override
    public Bundle addAccount(
            AccountAuthenticatorResponse authenticatorResponse, String accountType,
            String authTokenType, String[] requiredFeatures,
            Bundle options) throws NetworkErrorException {
        if (sessionManager.hasActiveAccount()) {
            final Bundle bundle = new Bundle();
            bundle.putInt(AccountManager.KEY_ERROR_CODE, 1);
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE,
                    context.getString(R.string.one_account_allowed));
            new Handler().post(new Runnable() {

                @Override public void run() {
                    Toast.makeText(context, R.string.one_account_allowed, Toast.LENGTH_SHORT)
                            .show();
                }
            });
            return bundle;
        }

        final Intent intent = new Intent(context, classLoginActivity);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, authenticatorResponse);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override public Bundle getAccountRemovalAllowed(
            AccountAuthenticatorResponse response,
            Account account) throws NetworkErrorException {
        Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT,
                sessionManager.onRemoveAuthenticatedUser(account));
        return result;
    }

    @Override public Bundle editProperties(
            AccountAuthenticatorResponse accountAuthenticatorResponse, String accountType) {
        return null;
    }

    @Override public Bundle confirmCredentials(
            AccountAuthenticatorResponse accountAuthenticatorResponse,
            Account account, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override public Bundle getAuthToken(
            AccountAuthenticatorResponse accountAuthenticatorResponse,
            Account account, String s, Bundle bundle) throws NetworkErrorException {
        return null;
    }

    @Override public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override public Bundle updateCredentials(
            AccountAuthenticatorResponse accountAuthenticatorResponse, Account account,
            String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override public Bundle hasFeatures(
            AccountAuthenticatorResponse accountAuthenticatorResponse,
            Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
