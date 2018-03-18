package com.madx.wechat.config;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.madx.wechat.common.CommonUtil;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Date;

/**
 * 时间格式，传递到前端时，格式化
 * Create by A-mdx at 2018-03-18 13:28
 */
@JsonComponent
public class JsonDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(CommonUtil.transDate2Str(date));
    }
}
