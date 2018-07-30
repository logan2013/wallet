package com.blockchain.wallet.api.internal.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created by Administrator on 2018/6/26.
 */
@Slf4j
public class SupportUtil {

    public static String getInputString(String inputStr, String charset) {
        String defaultCharset = Charset.defaultCharset().name().toUpperCase();
        if (charset.equals(defaultCharset)) {
            return inputStr;
        }
        try {
            inputStr = changeCharset(inputStr, charset);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return inputStr;
    }

    public String getGbkInputString(String inputStr) {
        return getInputString(inputStr, "GBK");
    }

    public String getUtf8InputString(String inputStr) {
        return getInputString(inputStr, "UTF-8");
    }



    public static String filterLineSeparator(String str)
    {
        return str.replaceAll("[\\n|\\r]", "");
    }



    public static boolean isExists(String filePath)
    {
        return new File(filePath).exists();
    }

    public static void mkDir(String dir)
    {
        mkDir(new File(dir));
    }

    public static void mkDir(File file)
    {
        if (file.getParentFile().exists())
        {
            file.mkdir();
        }
        else
        {
            mkDir(file.getParentFile());
            file.mkdir();
        }
    }

    public static String readFileAsString(String fileName)
            throws Exception
    {
        String content = new String(readFileBinary(fileName));
        return content;
    }

    public static byte[] readFileBinary(String fileName)
            throws Exception
    {
        FileInputStream fin = new FileInputStream(fileName);
        return readFileBinary(fin);
    }

    public static byte[] readFileBinary(InputStream streamIn)
            throws IOException {
        BufferedInputStream in = new BufferedInputStream(streamIn);
        ByteArrayOutputStream out = new ByteArrayOutputStream(10240);

        byte[] buf = new byte[1024];
        int len=0;
        while ((len = in.read(buf)) >= 0) {
            out.write(buf, 0, len);
        }
        in.close();
        return out.toByteArray();
    }

    public static boolean writeFileString(String fileName, String content)
            throws IOException {
        FileWriter fout = new FileWriter(fileName);
        fout.write(content);
        fout.close();
        return true;
    }

    public static boolean writeFileString(String fileName, String content, String encoding)
            throws IOException {
        OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(
                fileName), encoding);
        fout.write(content);
        fout.close();
        return true;
    }

    public static String changeCharset(String str, String newCharset)
            throws UnsupportedEncodingException {
        if (str != null) {
            return new String(str.getBytes("ISO-8859-1"), newCharset);
        }
        return null;
    }

    public static boolean appendFileString(String fileName, String content, String encode)
            throws IOException {
        OutputStreamWriter fout = new OutputStreamWriter(new FileOutputStream(
                fileName, true), encode);

        fout.write(content);
        fout.close();
        return true;
    }

    public static boolean delFile(File file) {
        if ((!file.exists()) || (!file.isFile())) {
            return false;
        }
        return file.delete();
    }

    public static void delFile(String file) {
        delFile(new File(file));
    }

    public static boolean delDir(File dir) {
        if ((dir == null) || (!dir.exists()) || (dir.isFile())) {
            return false;
        }
        File[] arrayOfFile;
        int j = (arrayOfFile = dir.listFiles()).length;
        for (int i = 0; i < j; i++)
        {
            File file = arrayOfFile[i];
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                delDir(file);
            }
        }
        return dir.delete();
    }

    public static String filterSpaceTab(String str)
    {
        str = str.replace(" ", "");
        return str.replace("\t", "");
    }

    public static InputStream string2InputStream(String str, String charset)
            throws UnsupportedEncodingException
    {
        ByteArrayInputStream stream = new ByteArrayInputStream(
                str.getBytes(charset));

        return stream;
    }

    public static String inputStream2String(InputStream is)
            throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line + "\r\n");
        }
        return buffer.toString();
    }

    public static String inputStream2String(InputStream is, String charset)
            throws IOException
    {
        BufferedReader in = new BufferedReader(new InputStreamReader(is,
                charset));

        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null) {
            buffer.append(line + "\r\n");
        }
        return buffer.toString();
    }

    public static void inputStream2File(InputStream is, String file)
            throws IOException
    {
        int BUFFER_SIZE = 1024;
        byte[] buf = new byte[BUFFER_SIZE];
        int size = 0;
        BufferedInputStream bis = new BufferedInputStream(is);
        FileOutputStream fos = new FileOutputStream(file);
        while ((size = bis.read(buf)) != -1) {
            fos.write(buf, 0, size);
        }
        fos.close();
        bis.close();
    }

}
