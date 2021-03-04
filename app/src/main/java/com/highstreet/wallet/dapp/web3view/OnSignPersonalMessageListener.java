package com.highstreet.wallet.dapp.web3view;

import com.highstreet.wallet.dapp.entity.Message;

public interface OnSignPersonalMessageListener {
    void onSignPersonalMessage(Message<String> message);
}
