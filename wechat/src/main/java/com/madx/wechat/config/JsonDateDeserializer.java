package com.madx.wechat.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.madx.wechat.common.CommonUtil;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Date;

/**
 * 头次发现还能这么玩，简直了
 * Create by A-mdx at 2018-03-18 14:15
 */
@JsonComponent
public class JsonDateDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String val = p.getText();
        return CommonUtil.transStr2Date(val);
    }
}
