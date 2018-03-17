package com.madx.wechat.initme.web;

import com.madx.wechat.common.WechatConfigUtil;
import com.madx.wechat.common.XmlMsg;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Create by A-mdx at 2018-03-17 14:46
 */
@RestController
public class WechatController {

    private static final Logger logger = LoggerFactory.getLogger(WechatController.class);

    @Autowired
    private WechatConfigUtil wechatConfigUtil;

    /**
     * 微信核心服务，全靠这里处理消息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "${wechat.bindAPI}", method = RequestMethod.POST)
    public String index(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("path : " + request.getContextPath());
        XmlMsg msg = null;
        // try-with-resources
        try (InputStream inputStream = request.getInputStream()) {
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);

            String requestXML = document.asXML();
            System.out.println("requestXML -> " + requestXML);

            String subXML = requestXML.split(">")[0] + ">";
            requestXML = requestXML.substring(subXML.length());

            msg = new XmlMsg(requestXML);

            System.out.println("msg -> \n" + msg);

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
            logger.error("接收微信消息失败了。", e);
            return null;
        }

//        String returnStr = wechatService.distributeMsg(msg);

        String returnStr = "123333333";

        System.out.println("response :\n" + returnStr);
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        return "success";
    }

    /**
     * 接收并校验四个请求参数
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return echostr
     */
    @RequestMapping(value = "${wechat.bindAPI}", method = RequestMethod.GET)
    public String checkName(@RequestParam(name = "signature") String signature,
                            @RequestParam(name = "timestamp") String timestamp,
                            @RequestParam(name = "nonce") String nonce,
                            @RequestParam(name = "echostr") String echostr) {
        System.out.println("-----------------------开始校验------------------------");
        //排序
        //此处TOKEN即我们刚刚所填的token
        String sortString = sort(wechatConfigUtil.getToken(), timestamp, nonce);
        //加密
        String myString = sha1(sortString);
        //校验
        if (myString != null && myString != "" && myString.equals(signature)) {
            System.out.println("签名校验通过 ");
            //如果检验成功原样返回echostr，微信服务器接收到此输出，才会确认检验完成。
            return echostr;
        } else {
            System.out.println("签名校验失败");
            return "";
        }
    }


    /**
     * 排序方法
     */
    public String sort(String token, String timestamp, String nonce) {
        String[] strArray = {token, timestamp, nonce};
        Arrays.sort(strArray);
        StringBuilder sb = new StringBuilder();
        for (String str : strArray) {
            sb.append(str);
        }

        return sb.toString();
    }

    /**
     * 将字符串进行sha1加密
     *
     * @param str 需要加密的字符串
     * @return 加密后的内容
     */
    public String sha1(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(str.getBytes());
            byte messageDigest[] = digest.digest();
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            // 字节数组转换为 十六进制 数
            for (int i = 0; i < messageDigest.length; i++) {
                String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
                if (shaHex.length() < 2) {
                    hexString.append(0);
                }
                hexString.append(shaHex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
