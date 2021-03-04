package com.highstreet.wallet.dapp.sign;

public interface OnCompleteListener<T extends Request> {
    void onComplete(Response<T> response);
}
