package com.blockchain.wallet.api.wallet;

import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class BtcSend {

    public static String signRawTx(String recipientAddress, long satoshi, String privateKey) {

        String signed = "";
        try {
            String pyPath = BtcSend.class.getClassLoader().getResource("py-module/btc-send.py").getPath();
            //设置命令行传入参数
            String[] args = new String[]{"python", pyPath, recipientAddress, satoshi+"", privateKey};
            Process pr = Runtime.getRuntime().exec(args);

            InputStreamReader ir = new InputStreamReader(pr.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            signed = input.readLine();

            input.close();
            ir.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return signed;
    }
}
