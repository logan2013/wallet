package com.blockchain.wallet.api.internal.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;

/**
 * 读取json字符串
 */
public class JsonConvert {

	/**
	 * @param
	 *
	 */
	public static void saveJson() {
		// 读取原始json文件并进行操作和输出
			String filePath = JsonConvert.class.getClassLoader().getResource("txt/TransactionModel.txt").getPath();
			System.out.println(filePath);
			String filePathNew = JsonConvert.class.getClassLoader().getResource("txt/TransactionModelNew.txt").getPath();
			String filePathTxt = JsonConvert.class.getClassLoader().getResource("txt/address.txt").getPath();
			// 读取nameID.txt文件中的NAMEID字段（key）对应值（value）并存储
			ArrayList<String> list = new ArrayList<String>();
			BufferedReader brname;
			try {
				brname = new BufferedReader(new FileReader(filePathTxt));// 读取NAMEID对应值
				String sname = null;
				while ((sname = brname.readLine()) != null) {
					JSONObject dataJson = new JSONObject(sname);
					JSONArray infoArray = dataJson.getJSONArray("address");
					for (int i = 0; i < infoArray.length(); i++) {
						String address=infoArray.get(i).toString();
						list.add(address);
						//System.out.println(address+"\n");
					}
				}
				brname.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));// 读取原始json文件
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePathNew));// 输出新的json文件
			String s = null, ws = null;
			while ((s = br.readLine()) != null) {
				try {
					JSONObject dataJson = new JSONObject(s);// 创建一个包含原始json串的json对象
					JSONArray infoArray = dataJson.getJSONArray("data");// 找到data json数组
					JSONObject infoObj = infoArray.getJSONObject(0);
					JSONArray receiveaddressData = infoObj.getJSONArray("receiveaddress");//找到sendaddress的json对象
					JSONArray unspent_outputsData = infoObj.getJSONArray("unspent_outputs");//找到sendaddress的json对象
					JSONObject uspObj = unspent_outputsData.getJSONObject(0);// 获取features数组的第i个json对象
					String script = uspObj.getString("script");// 读取receiveaddress对象里的address字段值
					String tx_output_n = uspObj.getString("tx_output_n");// 读取receiveaddress对象里的address字段值
					String value1 = uspObj.getString("value");// 读取receiveaddress对象里的address字段值
					String checkvalue1= uspObj.getString("checkvalue");
					String  sendaddress=infoObj.getString("sendaddress");
					for (int i = 0; i < receiveaddressData.length(); i++) {
						JSONObject info = receiveaddressData.getJSONObject(i);// 获取receiveaddressData数组的第i个json对象
						info.put("id",System.currentTimeMillis());
						info.put("address",list.get(i));
						info.put("value", Math.random());
						info.put("checkvalue",1000);
						String address = info.getString("address");// 读取receiveaddress对象里的address字段值
						String value = info.getString("value");// 读取receiveaddress对象里的value字段值
						String checkvalue = info.getString("checkvalue");// 读取receiveaddress对象里的checkvalue字段值
					}
					ws = dataJson.toString();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(ws);
			bw.write(ws);
			// bw.newLine();
			bw.flush();
			br.close();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 读取地址
	 */
/*	public static void readJson() {
		String filePath = BtcSend.class.getClassLoader().getResource("txt/address.txt").getPath();
		// 读取原始json文件并进行操作和输出
		ArrayList<String> list = new ArrayList<String>();
		BufferedReader brname;
		try {
			brname = new BufferedReader(new FileReader(filePath));// 读取NAMEID对应值
			String sname = null;
			while ((sname = brname.readLine()) != null) {
				JSONObject dataJson = new JSONObject(sname);
				JSONArray infoArray = dataJson.getJSONArray("address");
				for (int i = 0; i < infoArray.length(); i++) {
					String address=infoArray.get(i).toString();
					list.add(address);
					//System.out.println(address+"\n");
				}
			}
			brname.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}*/


}
