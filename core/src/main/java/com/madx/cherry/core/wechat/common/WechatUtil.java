package com.madx.cherry.core.wechat.common;

import com.madx.cherry.core.wechat.bean.XmlMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by A-mdx on 2017/6/26.
 */
public class WechatUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);
    
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
            
            // 只是为了自动关闭
            try (DataInputStream inputStream = new DataInputStream(connection.getInputStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(path+"/"+name)){
                // 保存图片开始
                byte[] temp = new byte[1024*100]; // 100k 
                int length = 0;
                while ((length = inputStream.read(temp)) != -1){
                    fileOutputStream.write(temp, 0, length);
                }
            }
            
  
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("保存文件出错,当前参数是：httpPath={}, name={}, path={} ... e : "+e,  httpPath, name, path);
            return false;
        }
        
        return true;
    }
    
    
}
