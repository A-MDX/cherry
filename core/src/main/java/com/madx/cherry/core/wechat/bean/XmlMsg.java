package com.madx.cherry.core.wechat.bean;

import com.madx.cherry.core.common.CommonUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * Created by A-mdx on 2017/2/23.
 */
public class XmlMsg {
    //文本消息基本变量
    private String toUserName;
    private String fromUserName;
    private String createTime;
    private String msgType;
    private String content;
    private String msgId;
    
    //图片消息的特殊变量
    private String picUrl;
    private String mediaId;
    
    //语音消息
    private String format;
    private String recognition;
    
    //视频消息
    private String thumbMediaId;
    
    //地理位置
    private String location_x;
    private String location_y;
    private String scale;
    private String label;
    
    //链接消息
    private String title;
    private String description;
    private String url;
    
    //事件推送变量
    private String event;
    //自定义菜单项
    private String eventKey;
    
    public XmlMsg(){}

    public XmlMsg(String str) throws DocumentException {
        Document doc = DocumentHelper.parseText(str);
        Element root = doc.getRootElement();
        this.toUserName = root.elementText("ToUserName");
        this.fromUserName = root.elementText("FromUserName");
        this.createTime = root.elementText("CreateTime");
        this.msgType = root.elementText("MsgType");
        this.content = root.elementText("Content");
        this.msgId = root.elementText("MsgId");
        
        this.picUrl = root.elementText("PicUrl");
        this.mediaId = root.elementText("MediaId");
        
        this.format = root.elementText("Format");
        this.recognition = root.elementText("Recognition");
        
        this.thumbMediaId = root.elementText("ThumbMediaId");
        
        this.location_x = root.elementText("Location_X");
        this.location_y = root.elementText("Location_Y");
        this.scale = root.elementText("Scale");
        
        this.label = root.elementText("Label");
        
        this.title = root.elementText("Title");
        this.description = root.elementText("Description");
        this.url = root.elementText("Url");
        
        this.event = root.elementText("Event");
        this.eventKey = root.elementText("EventKey");
    }
    
    public String transToXml(){


        return null;
    }


    @Override
    public String toString() {
        return "{ XmlMsg : "+ CommonUtil.toString(this)+" }";
    }

	public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getMediaId() {
		return mediaId;
	}

	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getRecognition() {
		return recognition;
	}

	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}

	public String getThumbMediaId() {
		return thumbMediaId;
	}

	public void setThumbMediaId(String thumbMediaId) {
		this.thumbMediaId = thumbMediaId;
	}

	public String getLocation_x() {
		return location_x;
	}

	public void setLocation_x(String location_x) {
		this.location_x = location_x;
	}

	public String getLocation_y() {
		return location_y;
	}

	public void setLocation_y(String location_y) {
		this.location_y = location_y;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
    
    
}
