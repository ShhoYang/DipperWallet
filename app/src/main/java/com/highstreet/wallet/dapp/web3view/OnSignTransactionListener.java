package com.highstreet.wallet.dapp.web3view;


import com.highstreet.wallet.dapp.entity.Transaction;

public interface OnSignTransactionListener {
    void onSignTransaction(Transaction transaction);
}
