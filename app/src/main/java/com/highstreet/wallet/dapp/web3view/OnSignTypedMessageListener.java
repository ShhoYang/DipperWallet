package com.highstreet.wallet.dapp.web3view;


import com.highstreet.wallet.dapp.entity.Message;
import com.highstreet.wallet.dapp.entity.TypedData;

public interface OnSignTypedMessageListener {
    void onSignTypedMessage(Message<TypedData[]> message);
}
