package com.highstreet.wallet.dapp.web3view;

import android.net.Uri;

public interface UrlHandler {

    String getScheme();

    String handle(Uri uri);
}