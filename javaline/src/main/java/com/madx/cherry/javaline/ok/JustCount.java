package com.madx.cherry.javaline.ok;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by a-mdx on 2017/7/17.
 */
public class JustCount {

    public static void main(String... args) throws IOException {

        // 1.初始化
        init();

        // 2.统计数据

        Map<String, Object> sendMap = genReMap();
        System.out.println(sendMap);

        // 3. 发送出去
        sendMsg(sendMap);

    }

    private static void sendMsg(Map<String, Object> sendMap) throws IOException {
        System.out.println("sendUrl:"+sendUrl);
        System.out.println("token:"+token);
        System.out.println("user:"+user);

        sendMap.put("user", user);
        sendMap.put("token", token); // ? 该如何处理这个，将是个问题

        String reStr = map2Str(sendMap);
        System.out.println("json:"+reStr);

        URL url = new URL(sendUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        // 必须设置

        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", "application/json");
//        connection.setRequestProperty("connection", "Keep-Alive");
//        connection.setRequestProperty("user-agent",
//                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        connection.connect();
        try (PrintWriter out = new PrintWriter(connection.getOutputStream())){
            out.print(reStr);
            out.flush();
        }
        StringBuilder result = new StringBuilder();
        if (connection.getResponseCode() != 200){
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getErrorStream()))){
                String temp;
                while ((temp = in.readLine()) != null){
                    result.append(temp);
                }
            }
            System.out.println(result);
            System.out.println("本次发送异常了。");
            return;
        }

        try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
            String temp;
            while ((temp = in.readLine()) != null){
                result.append(temp);
            }
        }
        System.out.println(result);

    }

    /**
     * map 转 json字符串
     * @param map
     * @return
     */
    private static String map2Str(Map<String, Object> map){
        StringBuilder str = new StringBuilder("{");
        map.forEach((k, v) -> {
            str.append("\"" + k +"\":");
            if (v instanceof String){
                str.append("\"").append(v).append("\"");
            }else if(v instanceof List){
                List<Integer> list = (List<Integer>) v;
                str.append("["+ list.get(0)+","+list.get(1)+"]");
            }else if (v instanceof Map){
                str.append(map2Str((Map<String, Object>) v));
            }else {
                str.append("\"").append(v).append("\"");
            }
            str.append(",");
        });
        int temp = str.lastIndexOf(",");
        str.deleteCharAt(temp);

        str.append("}");

        return str.toString();
    }

    private static void analysisDync(){
        System.out.println("----------------------------------------");
        System.out.println("统计行数程序");
        System.out.println("输入地址：");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();
        System.out.println("----------------------------------------");
        System.out.println("当前统计目录为：" + path);

        int[] arr = analyPath(path);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("行数：" + arr[0]);
        System.out.println("文件数：" + arr[1]);

        System.out.println("bye bye...");
        System.out.println("----------------------------------------");
    }

    private static Map<String, Object> genReMap(){
        Map<String, Object> sendMap = new HashMap<>();
        // 就这？
        projectPath.forEach((k, v) -> {
            int[] arr = analyPath(v);
            Map<String, Integer> count = new HashMap<>();
            count.put("javaLine", arr[0]);
            count.put("javaFile", arr[1]);
            sendMap.put(k, count);
            List<Integer> list = Arrays.asList(arr[0], arr[1]);
            System.out.println(k +" : "+list);
        });

        return sendMap;
    }

    private static String sendUrl;
    private static String token;
    private static String user;
    private static Map<String, String> projectPath;

    private static void init() throws IOException {
        System.out.println("-- 初始化开始 --");

        // init
        Properties properties = new Properties();

        projectPath = new HashMap<>();
        sendUrl = properties.getProperty("sendUrl");
        token = properties.getProperty("token");
        user = properties.getProperty("user");

        properties.list(System.out);

        properties.remove("sendUrl");
        properties.remove("token");
        properties.remove("user");

        for (String key : properties.stringPropertyNames()){
            projectPath.put(key, properties.getProperty(key));
        }

        System.out.println("-- 初始化完成 --");
    }

    private static int[] analyPath(String path){
        File file = new File(path);
        if (!file.exists()){
            throw new RuntimeException("这个目录不存在："+path);
        }
        List<String> fileList = new ArrayList<>();

        if (file.isFile() && path.endsWith(".java")){
            fileList.add(path);
        }else if (file.isDirectory()){
            genFileList(file, fileList);
        }

        final int[] lineNum = {0};

        fileList.forEach(f -> lineNum[0] += countFile(f));

        int[] arr = {lineNum[0], fileList.size()};

        return arr;
    }

    /**
     * 获取所有文件的列表
     * @param path
     * @param fileList
     */
    private static void genFileList(File path, List<String> fileList){
        for (File file : path.listFiles()){
            if (file.isFile() && file.getPath().endsWith(".java")){
                fileList.add(file.getAbsolutePath());
            }else if (file.isDirectory()){
                genFileList(file, fileList);
            }
        }
    }

    /**
     * 统计，返回的列表中，第一个为行数，第二个为文件数
     * @param path
     * @return
     */
    private static int countFile(String path){

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(path)))){
            int count = 0;
            while (reader.readLine() != null){
                count++;
            }
            return count;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
