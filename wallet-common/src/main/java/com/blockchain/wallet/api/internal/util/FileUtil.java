package com.blockchain.wallet.api.internal.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.*;

/**
 *
 * @since on 2018/5/8.
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);


    private static ResourceBundle bundle = ResourceBundle.getBundle("config/constant");

    public static String fileUploadPath =bundle.getString("file-upload.dir");









    private static List<String> listFile = new ArrayList<>();


    public static void getFile(String path) {
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (File f : tempList) {
            if (f.isFile()) {
                listFile.add(f.getPath());
                System.out.println(f.getPath());
                continue;
            }
            if (f.isDirectory()) {
                getFile(f.getPath());
            }
        }

    }


    /**
     * 保存文件到临时目录
     * @param inputStream 文件输入流
     * @param fileName 文件名
     */
    public static void savePic(InputStream inputStream, String fileName) {
        OutputStream os = null;
        try {
            // 保存到临时文件
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流保存到本地文件
            File tempFile = new File(fileUploadPath);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            // 开始读取
            while ((len = inputStream.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static File createTemporaryFile(InputStream file, String name) throws Exception {
        File temp = new File(name);
        OutputStream out = new FileOutputStream(temp);
        try {
            int byteCount = 0;
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = file.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                byteCount += bytesRead;
            }
            out.flush();
        }
        finally {
            try {
                file.close();
            }
            catch (IOException ex) {
            }
            try {
                out.close();
            }
            catch (IOException ex) {
            }
        }
        return temp;
    }

    private static void copyFile(File newFile, FileInputStream file) throws Exception {
        FileOutputStream outFile = new FileOutputStream(newFile);
        FileChannel inC = file.getChannel();
        FileChannel outC = outFile.getChannel();
        int length = 2097152;
        while (true) {
            if (inC.position() == inC.size()) {
                inC.close();
                outC.close();
                outFile.close();
                file.close();
                return ;
            }
            if ((inC.size() - inC.position()) < 20971520) {
                length = (int) (inC.size() - inC.position());
            }else {
                length = 20971520;
            }
            inC.transferTo(inC.position(), length, outC);
            inC.position(inC.position() + length);
        }

    }

    public static File getNewFile(String fileName) throws IOException {
        String filePath = fileUploadPath + fileName;
        File newFile = new File(filePath);
        File fileParent = newFile.getParentFile();
        if(!fileParent.exists()){
            fileParent.mkdirs();
        }
        if (!newFile.exists()){
            newFile.createNewFile();
        }
        return newFile;
    }


    public static String getFileUrl(String fileDir){
        return bundle.getString("file-upload.url") + fileDir;
    }


    /**
     * 删除文件目录
     * @param path
     */
    private static void delDir(String path){
        File dir=new File(path);
        if(dir.exists()){
            File[] tmp=dir.listFiles();
            for(int i=0;i<tmp.length;i++){
                if(tmp[i].isDirectory()){
                    delDir(path+File.separator+tmp[i].getName());
                }else{
                    tmp[i].delete();
                }
            }
            dir.delete();
        }
    }

    /**
     *  截取文件排除后缀名
     * @param fileName 文件名
     * @return
     */
    public static String cutNameSuffix(String fileName) {
        String suffix = fileName.substring(0,fileName.lastIndexOf("."));
        return suffix;
    }













    /**
     * 不使用renameTo,如果文件(isFile)不存在则不复制.
     *
     * @param sourceFile
     * @param target
     * @throws Exception
     */
    public static void moveFile(File sourceFile, String target) throws Exception {
        if (!sourceFile.exists() || !sourceFile.isFile()) {
            return;
        }
        InputStream inputStream = null;
        File targetFile = new File(target + File.separator + sourceFile.getName());
        OutputStream outputStream = null;
        inputStream = new FileInputStream(sourceFile);
        outputStream = new FileOutputStream(targetFile);
        int readBytes = 0;
        byte[] buffer = new byte[10000];
        while ((readBytes = inputStream.read(buffer, 0, 10000)) != -1) {
            outputStream.write(buffer, 0, readBytes);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }


    /**
     * 删除文件
     *
     * @param file
     * @return
     */
    public static boolean deleteFile(File file) {
        boolean flag = false;
        if (file != null && file.exists()) {
            if (file.isDirectory()) {
                for (File f : file.listFiles()) {
                    deleteFile(f);
                }
            }
            flag = file.delete();
        }

        return flag;
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) { // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath 被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查文件名是否合法
     *
     * @param fileName
     * @return
     */
    public static boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.length() > 255)
            return false;
        else {
            return fileName.matches(
                    "[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
        }
    }

    /**
     * 复制文件
     *
     * @param src
     * @param dst
     */
    public static void copy(File src, File dst) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(src);
            out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (out != null) {
                        out.close();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return;
    }

    /**
     * 取指定文件的扩展名
     *
     * @param filePathName 文件路径
     * @return 扩展名
     */
    public static String getFileExt(String filePathName) {
        int pos = 0;
        pos = filePathName.lastIndexOf('.');
        if (pos != -1) {
            return filePathName.substring(pos + 1, filePathName.length());
        }
        else {
            return "";
        }
    }

    /**
     * 去掉文件扩展名
     *
     * @param filename
     * @return
     */
    public static String trimExtension(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');
            if ((i > -1) && (i < (filename.length()))) {
                return filename.substring(0, i);
            }
        }
        return filename;
    }

    /**
     * 读取文件大小
     *
     * @param filename 指定文件路径
     * @return 文件大小
     */
    public static int getFileSize(String filename) {
        try {
            File fl = new File(filename);
            int length = (int) fl.length();
            return length;
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * 判断是否是图片
     *
     * @param file
     * @return
     */
    public static boolean isImage(File file) {
        boolean flag = false;
        try {
            ImageInputStream is = ImageIO.createImageInputStream(file);
            if (null == is) {
                return flag;
            }
            is.close();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * TODO. 读取文件内容
     *
     * @param file
     * @param fullFilePath
     * @return
     * @throws IOException
     */
    @SuppressWarnings("unused")
    public static String readFileContent(File file, String fullFilePath) throws IOException {
        String returnStr = "";
        if (ComUtil.isEmpty(file) && ComUtil.isEmpty(fullFilePath)) {
            return "";
        }
        if (ComUtil.isEmpty(file)) {
            file = new File(fullFilePath);
        }
        FileInputStream in = null;

        try {
            in = new FileInputStream(file);
            byte[] buf = new byte[1024];
            int len = -1;
            while ((len = in.read(buf)) > 0) {
                returnStr += new String(buf, "utf-8");
                buf = new byte[1024];
            }
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage() + ";" + file.getPath(), e);
            throw e;
        } catch (IOException e) {
            logger.error(e.getMessage() + ";" + file.getPath(), e);
            throw e;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return returnStr;
    }

    public static boolean writeToFile(String content, File file, String fullFilePath) throws IOException {
        if ((ComUtil.isEmpty(file) && ComUtil.isEmpty(fullFilePath)) || ComUtil.isEmpty(content)) {
            return false;
        }
        if (ComUtil.isEmpty(file)) {
            file = new File(fullFilePath);
        }
        FileOutputStream out = null;

        try {

            out = new FileOutputStream(file);

            out.write(content.getBytes("utf-8"));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage() + ";" + file.getPath(), e);
            throw e;
        } catch (IOException e) {
            logger.error(e.getMessage() + ";" + file.getPath(), e);
            throw e;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    /**
     * 在指定的目录下创建文件
     *
     * @param directory
     * @param fileName
     * @param content
     * @throws Exception
     */
    public static String createFile(String directory, String fileName, InputStream content) throws Exception {
        File currentDir = new File(directory);
        if (!currentDir.exists()) {
            currentDir.mkdirs();
        }
        FileOutputStream fileOut = null;
        String fullFilePath = directory + File.separator + fileName;
        try {
            fileOut = new FileOutputStream(fullFilePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = content.read(buffer)) > 0) {
                fileOut.write(buffer);
            }
        } finally {
            if (content != null) {
                content.close();
            }
            if (fileOut != null) {
                fileOut.close();
            }
        }
        return fullFilePath;
    }

    public static File mkdir(String directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static String readFileContent(File file, Charset charset) throws Exception {
        if (!file.exists() || file.isDirectory()) throw new Exception("file is not exists or is a directory");
        StringWriter out = new StringWriter();
        FileInputStream in = new FileInputStream(file);
        try {
            byte[] buffer = new byte[1024];
            int length = 0;
            while ((length = in.read(buffer)) > 0) {
                out.write(new String(buffer, 0, length, charset));
            }
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
        return out.toString();
    }

    public static String image2Base64String(InputStream content) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            int length = 0;
            byte[] buffer = new byte[1024];
            while((length = content.read(buffer)) > 0){
                out.write(buffer,0,length);
            }
        } finally {
            if (content != null) content.close();
            if(out != null) out.close();
        }
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(out.toByteArray());
    }

    public static byte[] base64String2Image(String base64String) throws Exception{
        if(ComUtil.isEmpty(base64String)) return null;
        base64String = base64String.replaceAll("data:image/(jpg|png|jpeg);base64,","");
        BASE64Decoder decoder = new BASE64Decoder();
        return decoder.decodeBuffer(base64String);
    }

    public static void main(String[] args) throws Exception {
        //System.out.println(Jsoup.parse(new File("D:\\result.htm"),"GBK").outerHtml());
        //System.out.println(readFileContent(new File("D:\\result.htm"),Charset.forName("GBK")));
        //System.out.println(image2Base64String(new FileInputStream(new File("D://123.png"))));
        System.out.println("data:image/png;base64,".replaceAll("data:image/(jpg|png|jpeg);base64,",""));
    }
    /**
     * 创建文件路径(文件夹)
     * @param fileName
     */
    public  static void fileMkdir(String fileName) {
        File fileTotal=new File(fileName);
        if (!fileTotal.exists()) {
            fileTotal.mkdirs();
        }
    }


    /**
     * 读取txt文件
     */
    public  static void readTxt(String fileName) {

        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            /* 读入TXT文件 */
            String pathname = "/Users/zhangkai/Downloads/address.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
            File filename = new File(pathname); // 要读取以上路径的input。txt文件
            InputStreamReader reader = new InputStreamReader(
                    new FileInputStream(filename)); // 建立一个输入流对象reader
            BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
            String line = "";
            line = br.readLine();
            while (line != null) {
                line = br.readLine(); // 一次读入一行数据
                System.out.println(line+"\n");
            }
            System.out.println(line);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入TXT
     * @param fileName
     */
    public  static void writeTxt(String fileName) {
        try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
            /* 写入Txt文件 */
            File writename = new File(".\\result\\en\\output.txt"); // 相对路径，如果没有则要建立一个新的output。txt文件
            writename.createNewFile(); // 创建新文件
            BufferedWriter out = new BufferedWriter(new FileWriter(writename));
            out.write("我会写入文件啦\r\n"); // \r\n即为换行
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析处理Json格式数据：
     */
    private static void process(String txtStr) {
        JSONObject json = JSONObject.parseObject(txtStr);
        JSONArray datas = json.getJSONObject("data").getJSONArray("rows");
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            JSONObject obj = datas.getJSONObject(i).getJSONObject("cells");
            String name = obj.getString("weibo_name");
            String code = obj.getString("weibo_id");
            map.put("name", name);
            map.put("code", code);
            list.add(map);
        }
    }


    /**
     * 读取txt文件内容(Json格式数据)：
     */
    public static String reader(String filePath) {
        try {
            File file = new File(filePath);
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = bufferedReader.readLine();
                System.out.println(lineTxt+"\n");
                while (lineTxt != null) {
                    return lineTxt;
                }
            }
        } catch (UnsupportedEncodingException | FileNotFoundException e) {
            System.out.println("Cannot find the file specified!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading file content!");
            e.printStackTrace();
        }
        return null;
    }


    public static void read() {
        // 读取nameID.txt文件中的NAMEID字段（key）对应值（value）并存储
        ArrayList<String> list = new ArrayList<String>();
        BufferedReader brname;
        try {
            brname = new BufferedReader(new FileReader("src/json/nameID.txt"));// 读取NAMEID对应值
            String sname = null;
            while ((sname = brname.readLine()) != null) {
                // System.out.println(sname);
                list.add(sname);// 将对应value添加到链表存储
            }
            brname.close();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        // 读取原始json文件并进行操作和输出
        try {
            BufferedReader br = new BufferedReader(new FileReader(
                    "src/json/HK_geo.json"));// 读取原始json文件
            BufferedWriter bw = new BufferedWriter(new FileWriter(
                    "src/json/HK_new.json"));// 输出新的json文件
            String s = null, ws = null;
            while ((s = br.readLine()) != null) {
                // System.out.println(s);
                try {
                    JSONObject dataJson = new JSONObject();// 创建一个包含原始json串的json对象
                    JSONArray features = dataJson.getJSONArray("features");// 找到features的json数组
                    for (int i = 0; i < features.size(); i++) {
                        JSONObject info = features.getJSONObject(i);// 获取features数组的第i个json对象
                        JSONObject properties = info.getJSONObject("properties");// 找到properties的json对象
                        String name = properties.getString("name");// 读取properties对象里的name字段值
                        System.out.println(name);
                        properties.put("NAMEID", list.get(i));// 添加NAMEID字段
                        // properties.append("name", list.get(i));
                        System.out.println(properties.getString("NAMEID"));
                        properties.remove("ISO");// 删除ISO字段
                    }
                    ws = dataJson.toString();
                    System.out.println(ws);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

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
}
