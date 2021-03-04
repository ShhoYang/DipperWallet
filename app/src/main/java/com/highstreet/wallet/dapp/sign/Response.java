package com.highstreet.wallet.dapp.sign;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.highstreet.wallet.dapp.Trust;

public final class Response<T extends Request> {

    @Nullable
    public final T request;
    @Nullable
    public final String result;
    public final int error;

    public Response(@Nullable T request, @Nullable String result, int error) {
        this.request = request;
        this.result = result;
        this.error = error;
    }

    public boolean isSuccess() {
        return !TextUtils.isEmpty(result) && error == Trust.ErrorCode.NONE;
    }

    public boolean isAvailable() {
        return request != null && (!TextUtils.isEmpty(result) || error > Trust.ErrorCode.NONE);
    }
}
