package com.madx.cherry.core.wechat.common;

import com.madx.cherry.core.wechat.bean.XmlMsg;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;

/**
 * Created by A-mdx on 2017/6/26.
 */
public class WechatUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);
    private static final String SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=";
    
    /**
     * 验证是否为空
     * @param obj 验证对象
     * @param msg 主要给返回数据使用
     * @param message 错误信息
     */
    public static void isNullForMsg(Object obj, XmlMsg msg, String message){
        if (obj == null){
            throw new WechatException(message, msg);
        }
        
    }

    /**
     * 保存图片至本地
     * @param httpPath 
     * @param name
     * @param path
     * @return
     */
    public static boolean saveFileFromUrl(String httpPath, String name, String path){
        try {
            URL url = new URL(httpPath);
            /*此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流*/
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            String filePath = path+"/"+name;

            logger.info("当前保存目录是：{}", filePath);
            File file = new File(filePath);
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }

            // 只是为了自动关闭
            try (DataInputStream inputStream = new DataInputStream(connection.getInputStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
                // 保存图片开始
                byte[] temp = new byte[1024*100]; // 100k 
                int length;
                while ((length = inputStream.read(temp)) != -1){
                    fileOutputStream.write(temp, 0, length);
                }
            }
            
  
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("保存文件出错,当前参数是：httpPath={}, name={}, path={} ... ",  httpPath, name, path, e);
            return false;
        }
        
        return true;
    }

    /**
     * 发送消息
     * @param accessToken
     * @param json
     * @return
     */
    public static Optional<BasicDBObject> sendMessage(String accessToken, String json){

        try {
            URL realUrl = new URL(SEND_MESSAGE_URL+accessToken);
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();

            // 必须设置
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.connect();
            try (PrintWriter out = new PrintWriter(connection.getOutputStream())){
                out.print(json);
                out.flush();
            }
            StringBuilder result = new StringBuilder();
            try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))){
                String temp;
                while ((temp = in.readLine()) != null){
                    result.append(temp);
                }
            }
            return Optional.ofNullable((BasicDBObject) JSON.parse(result.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String... args) throws FileNotFoundException {
//        BasicDBObject json = new BasicDBObject();
//        json.append("touser", "oEYIP0wamw-fSnN103-JYGN5eHq8")
//                .append("msgtype", "text")
//                .append("text", new BasicDBObject("content", "Hello, Mr.Ma."));
//        String accessToken = "NVEaNrmYnlQvSJAZG0ixq43noLd-73ItnYbqlWmAENj2zU3ZZtTEFGlGxdoiPw-ugiWrIRnofKJOB7hLCD9PLJHRHb1-rChVJFkpbGsImJYJ3sabrenTiUvp_H3anRBOLYOgABAOBR";
//        Optional<BasicDBObject> jsonO = sendMessage(accessToken, json.toJson());
//        json = jsonO.orElse(json);
//
//        System.out.println(json.toJson());
        
        String filePath = "C:/Users/A-mdx/Desktop/image/201707/01-12_22_11,320+50.jpg";
        String httpPath = "http://mmbiz.qpic.cn/mmbiz_jpg/IUWdxTWzmFMibUiaP6biboj6KylYO30SuVpNPHiagiaLdAYKnsQAPUXVcEKcJsQtm44bFY2EjibiaZVZxujgsjvMGATUw/0";
        
    }
    
}
