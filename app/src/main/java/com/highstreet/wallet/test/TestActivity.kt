package com.highstreet.wallet.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.highstreet.wallet.R
import java.io.File

/**
 * @author Yang Shihao
 * @Date 3/30/21
 *
 * 使用ViewBinding库后使用Lint会认为布局文件是无用资源，这里引用一下，以便使用Lint删除其它无用资源
 */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_token)
        setContentView(R.layout.activity_wallet_manage)
        setContentView(R.layout.activity_tx_detail)
        setContentView(R.layout.item_menu)
        setContentView(R.layout.include_input_line)
        setContentView(R.layout.activity_scan)
        setContentView(R.layout.activity_transaction)
        setContentView(R.layout.activity_add_token)
        setContentView(R.layout.activity_validator_choose)
        setContentView(R.layout.item_wallet_manage_left)
        setContentView(R.layout.item_my_validator)
        setContentView(R.layout.fragment_viewpager)
        setContentView(R.layout.activity_backup_verify)
        setContentView(R.layout.item_wallet_manage_left_all)
        setContentView(R.layout.activity_wallet_detail)
        setContentView(R.layout.fragment_list)
        setContentView(R.layout.activity_native_token_detail)
        setContentView(R.layout.dialog_bottom_menu)
        setContentView(R.layout.activity_undelegate)
        setContentView(R.layout.item_menu_group)
        setContentView(R.layout.item_menu_space)
        setContentView(R.layout.dialog_center_menu_item2)
        setContentView(R.layout.fragment_all_validator)
        setContentView(R.layout.include_toolbar)
        setContentView(R.layout.dialog_fingerprint)
        setContentView(R.layout.activity_import_wallet)
        setContentView(R.layout.activity_delegation_detail)
        setContentView(R.layout.activity_web)
        setContentView(R.layout.activity_backup)
        setContentView(R.layout.dialog_bottom_menu_item)
        setContentView(R.layout.dialog_center_menu_item)
        setContentView(R.layout.activity_create_password)
        setContentView(R.layout.include_button)
        setContentView(R.layout.activity_create_wallet)
        setContentView(R.layout.item_wallet_manage_right)
        setContentView(R.layout.item_dapp)
        setContentView(R.layout.activity_list)
        setContentView(R.layout.activity_carsh)
        setContentView(R.layout.activity_redelegate)
        setContentView(R.layout.dialog_input)
        setContentView(R.layout.item_common)
        setContentView(R.layout.item_proposal)
        setContentView(R.layout.dialog_center_menu)
        setContentView(R.layout.activity_redeem_reward)
        setContentView(R.layout.include_line)
        setContentView(R.layout.dialog_qr)
        setContentView(R.layout.item_mnemonic)
        setContentView(R.layout.dialog_app_loading)
        setContentView(R.layout.item_token)
        setContentView(R.layout.fragment_tokens)
        setContentView(R.layout.app_toolbar)
        setContentView(R.layout.activity_lock)
        setContentView(R.layout.activity_proposal_detail)
        setContentView(R.layout.activity_main)
        setContentView(R.layout.activity_init_wallet)
        setContentView(R.layout.include_block_line)
        setContentView(R.layout.activity_staking)
        setContentView(R.layout.activity_viewpager)
        setContentView(R.layout.item_wallet_manage_right_add)
        setContentView(R.layout.activity_token_detail)
        setContentView(R.layout.include_arrow_right)
        setContentView(R.layout.item_my_token)
        setContentView(R.layout.item_history)
        setContentView(R.layout.activity_validator_detail)
        setContentView(R.layout.item_validator)
        setContentView(R.layout.activity_delegate)
        setContentView(R.layout.activity_dapp)
        setContentView(R.layout.fragment_wallet)
        setContentView(R.layout.activity_welcome)
    }
}

fun main() {
    val path = "/Users/hao/Gaojie/Wallet/app/src/main/res/layout"
    val file = File(path)
    val listFiles = file.listFiles()
    listFiles.forEach {
        println("setContentView(R.layout.${it.name.replace(".xml", "")})")
    }
}