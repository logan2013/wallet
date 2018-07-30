package com.blockchain.wallet.api.wallet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 生成钱包信息
 */
public class BtcWallet {

    public static Map<String, String> produceAddress() {
        String result = "";
        try {
            String pythonFile = BtcWallet.class.getClassLoader().getResource("py-module/btc-address.py").getPath();
            Process process = Runtime.getRuntime().exec("python " + pythonFile);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            result = input.readLine();
            input.close();
            ir.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        if(result == null){
            System.out.println("生成钱包信息失败");
            return new HashMap<String, String>();
        }
        String[] addressInfo = result.split(";");
        Map<String, String> address = new HashMap<String, String>();
        address.put("private-key", addressInfo[0]);
        address.put("wif", addressInfo[1]);
        address.put("public-key", addressInfo[2]);
        address.put("address", addressInfo[3]);

        return address;
    }
}
