package com.madx.cherry.core.common.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Optional;

/**
 * Created by A-mdx on 2017/6/28.
 */
public class Util {
    
    public static byte[] downloadPic(String httpPath){
        byte[] data = null;
        try {
            data = new byte[1024*1024+600*1024]; // 1.6mb
            URL url = new URL(httpPath);
            /*此为联系获得网络资源的固定格式用法，以便后面的in变量获得url截取网络资源的输入流*/
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedInputStream bif = new BufferedInputStream(is);
            int rest = bif.read(data);
            if (rest >= -1){
                return data;
            }
            return data;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void test1() throws IOException {
        File file = new File("/Users/a-mdx/Desktop/ok.jpg");
        FileInputStream in = new FileInputStream(file);
        FileChannel channel = in.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
        while (channel.read(byteBuffer) > 0){

        }
        byteBuffer.array();
    }

    
}
