package com.blockchain.wallet.api.internal.util;

/**
 * Created by kai on 17-4-17.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * <pre>
 * 功能：httpUrlConnection访问远程接口工具
 * 日期：Created by kai on 17-4-17.
 * </pre>
 */
public class HttpUrlConnectionUtil {
    private static Logger log = LoggerFactory.getLogger(HttpUrlConnectionUtil.class);
    /**
     * <pre>
     * 方法体说明：向远程接口发起请求，返回字符串类型结果
     * @param url 接口地址
     * @param requestMethod 请求方式
     * @param params 传递参数     重点：参数值需要用Base64进行转码
     * @return String 返回结果
     * </pre>
     */
    public static String httpRequestToString(String url, String requestMethod,
                                             Map<String, String> params){

        String result ="";
        BufferedReader is =null;

        try {
            is = httpRequestToStream(url, requestMethod, params);

            //避免分段传输
            Thread.sleep(2000);
           // int size = is.available();
           // byte[] b = new byte[size];

            //is.read()是一个阻塞方法
          //  is.read(b);
            //is.close();
          //  result = new String(b);
            String line;
            while ((line = is.readLine()) != null) {
                result +=line;
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * <pre>
     * 方法体说明：向远程接口发起请求，返回字节流类型结果
     * 日期：2015年3月17日 上午11:20:25
     * @param url 接口地址
     * @param requestMethod 请求方式
     * @param params 传递参数     重点：参数值需要用Base64进行转码
     * @return InputStream 返回结果
     * </pre>
     */
    public static BufferedReader httpRequestToStream(String url, String requestMethod,
                                                  Map<String, String> params){

        BufferedReader is = null;
        try {
            String parameters = "";
            boolean hasParams = false;
            //将参数集合拼接成特定格式，如name=zhangsan&age=24
            for(String key : params.keySet()){
                String value = URLEncoder.encode(params.get(key), "UTF-8");
                parameters += key +"="+ value +"&";
                hasParams = true;
            }
            if(hasParams){
                parameters = parameters.substring(0, parameters.length()-1);
            }

            //请求方式是否为get
            boolean isGet = "get".equalsIgnoreCase(requestMethod);
            //请求方式是否为post
            boolean isPost = "post".equalsIgnoreCase(requestMethod);
            if(isGet){
                url += "?"+ parameters;
            }

            URL u = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();

            //请求的参数类型(使用restlet框架时，为了兼容框架，必须设置Content-Type为“”空)
           // conn.setRequestProperty("Content-Type", "application/octet-stream");
           // conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置连接超时时间
            conn.setConnectTimeout(50000);
            //设置读取返回内容超时时间
            conn.setReadTimeout(50000);
            //设置向HttpURLConnection对象中输出，因为post方式将请求参数放在http正文内，因此需要设置为ture，默认false
            if(isPost){
                conn.setDoOutput(true);
            }
            //设置从HttpURLConnection对象读入，默认为true
            conn.setDoInput(true);
            //设置是否使用缓存，post方式不能使用缓存
            if(isPost){
                conn.setUseCaches(false);
            }
            //设置请求方式，默认为GET
            //conn.setRequestProperty("X-HTTP-Method-Override", requestMethod);
            conn.setRequestMethod(requestMethod);

            //post方式需要将传递的参数输出到conn对象中
            if(isPost){
                log.info("post参数"+parameters);
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(parameters);
                dos.flush();
                dos.close();
            }

            //从HttpURLConnection对象中读取响应的消息
            //执行该语句时才正式发起请求
            is = new BufferedReader(new InputStreamReader(conn.getInputStream(),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }


    /**
     * get 请求
     * @param url
     * @param code
     * @throws IOException
     */
    public static void readContentFromGet(String url,String code) throws IOException {
        // 拼凑get请求的URL字串，使用URLEncoder.encode对特殊和不可见字符进行编码
        String getURL = url + "?"+code+"="
                + URLEncoder.encode("fat man", "utf-8");
        URL getUrl = new URL(getURL);
        // 根据拼凑的URL，打开连接，URL.openConnection函数会根据URL的类型，
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) getUrl
                .openConnection();
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发到
        // 服务器
        connection.connect();
        // 取得输入流，并使用Reader读取
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        System.out.println("=============================");
        System.out.println("Contents of get request");
        System.out.println("=============================");
        String lines;
        while ((lines = reader.readLine()) != null) {
            System.out.println(lines);
        }
        reader.close();
        // 断开连接
        connection.disconnect();
        System.out.println("=============================");
        System.out.println("Contents of get request ends");
        System.out.println("=============================");
    }

    /**
     * post 请求
     * @param url
     * @throws IOException
     */
    public static void readContentFromPost(String url) throws IOException {
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = new URL(url);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl
                .openConnection();
        // Output to the connection. Default is
        // false, set to true because post
        // method must write something to the
        // connection
        // 设置是否向connection输出，因为这个是post请求，参数要放在
        // http正文内，因此需要设为true
        connection.setDoOutput(true);
        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // Set the post method. Default is GET
        connection.setRequestMethod("POST");
        // Post cannot use caches
        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        // This method takes effects to
        // every instances of this class.
        // URLConnection.setFollowRedirects是static函数，作用于所有的URLConnection对象。
        // connection.setFollowRedirects(true);

        // This methods only
        // takes effacts to this
        // instance.
        // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
        connection.setInstanceFollowRedirects(true);
        // Set the content type to urlencoded,
        // because we will write
        // some URL-encoded content to the
        // connection. Settings above must be set before connect!
        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded的
        // 意思是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
        // 进行编码
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成，
        // 要注意的是connection.getOutputStream会隐含的进行connect。
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection
                .getOutputStream());
        // The URL-encoded contend
        // 正文，正文内容其实跟get的URL中'?'后的参数字符串一致
        String content = "firstname=" + URLEncoder.encode("一个大肥人", "utf-8");
        // DataOutputStream.writeBytes将字符串中的16位的unicode字符以8位的字符形式写道流里面
        out.writeBytes(content);

        out.flush();
        out.close(); // flush and close
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String line;
        System.out.println("=============================");
        System.out.println("Contents of post request");
        System.out.println("=============================");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println("=============================");
        System.out.println("Contents of post request ends");
        System.out.println("=============================");
        reader.close();
        connection.disconnect();
    }
}