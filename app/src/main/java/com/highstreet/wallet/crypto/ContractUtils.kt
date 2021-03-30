package com.highstreet.wallet.crypto

import com.highstreet.wallet.constant.Constant
import com.highstreet.wallet.model.req.Coin
import com.highstreet.wallet.model.req.EstimateGas
import org.ethereum.solidity.Abi
import org.ethereum.solidity.SolidityType
import java.lang.IllegalArgumentException

/**
 * @author Yang Shihao
 * @Date 3/26/21
 */
object ContractUtils {

    private const val NAME = "name"
    private const val SYMBOL = "symbol"
    private const val DECIMALS = "decimals"
    private const val TOTAL_SUPPLY = "totalSupply"
    private const val BALANCE_OF = "balanceOf"
    private const val TRANSFER = "transfer"
    private val EMPTY_LIST = arrayListOf<Abi.Entry.Param>()

    fun balance(fromAddress: String): EstimateGas {
        return generatorParams(
            fromAddress,
            BALANCE_OF,
            arrayListOf(addressType("")),
            convertAddress(fromAddress)
        )
    }

    fun decimals(fromAddress: String): EstimateGas {
        return generatorParams(fromAddress, DECIMALS)
    }

    private fun generatorParams(
        fromAddress: String,
        name: String,
        inputs: List<Abi.Entry.Param> = EMPTY_LIST,
        vararg args: Any
    ): EstimateGas {
        val payload = encode(name, inputs, *args)
        return EstimateGas(fromAddress, Constant.CONTRACT_ADDRESS, payload, Coin.ZERO)
    }

    /**
     * @param tokens Int改为了String
     */
    fun transfer(toAddress: String, tokens: String): String {
        return encode(
            TRANSFER,
            arrayListOf(addressType("to"), numberType("tokens")),
            convertAddress(toAddress),
            tokens
        )
    }

    private fun encode(
        name: String,
        inputs: List<Abi.Entry.Param> = EMPTY_LIST,
        vararg args: Any
    ): String {
        if (inputs.size != args.size) {
            throw  IllegalArgumentException("Arguments: " + args.size + " != " + inputs.size)
        }
        val f = Abi.Function(
            false,
            name,
            inputs,
            EMPTY_LIST,
            false
        )
        return HexUtils.bytesToHexString(f.encode(*args))
    }


    fun convertAddress(originAddress: String): String {
        return if (originAddress.startsWith("dip")) {
            KeyUtils.getEip55Address(originAddress)
        } else {
            originAddress
        }
    }

    fun addressType(name: String): Abi.Entry.Param {
        val param = Abi.Entry.Param()
        param.name = name
        param.type = SolidityType.AddressType()
        return param
    }


    fun decodeString(res: String?): String? {
        return decode(res, stringType(""))
    }

    fun decodeNumber(res: String?): String? {
        return decode(res, numberType(""))
    }

    private fun decode(res: String?, type: Abi.Entry.Param): String? {
        var ret: String? = null
        if (res == null || res == "") {
            return ret
        }
        try {
            var bytes = HexUtils.hexStringToBytes(res)
            var paramsList = Abi.Entry.Param.decodeList(arrayListOf(type), bytes)
            if (paramsList != null && paramsList.isNotEmpty()) {
                var param = paramsList[0]
                if (param is Number) {
                    ret = param.toString()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ret
    }

    private fun stringType(name: String): Abi.Entry.Param {
        val param = Abi.Entry.Param()
        param.name = name
        param.type = SolidityType.StringType()
        return param
    }

    private fun numberType(name: String): Abi.Entry.Param {
        val param = Abi.Entry.Param()
        param.name = name
        param.type = SolidityType.UnsignedIntType("uint")
        return param
    }

    private fun bool(name: String): Abi.Entry.Param {
        val param = Abi.Entry.Param()
        param.name = name
        param.type = SolidityType.BoolType()
        return param
    }
}

fun main() {
    println(ContractUtils.decodeNumber("00000000000000000000000000000000000000000000000000271471148783e8"))
}

