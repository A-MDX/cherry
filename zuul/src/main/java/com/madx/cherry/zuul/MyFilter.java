package com.madx.cherry.zuul;

import com.madx.cherry.zuul.entity.Result;
import com.netflix.zuul.ExecutionStatus;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.ZuulFilterResult;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.monitoring.Tracer;
import com.netflix.zuul.monitoring.TracerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by A-mdx on 2017/6/11.
 */
@Configuration
public class MyFilter extends ZuulFilter {
    
    private static Logger logger = LoggerFactory.getLogger(MyFilter.class); 

    
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String path = request.getRequestURI();
        logger.info("getRequestURI: "+path);
        if (path.contains("wechat/apakfn")){
            return false;
        }else if (path.endsWith(".html") || path.endsWith(".js") || path.endsWith(".css")){
            // 静态资源就算了
            return false;
        }
        return true;
    }

    @Override
    public Object run() {
        Result result = Result.getInstance();
        RequestContext context = RequestContext.getCurrentContext();
        String token = context.getRequest().getParameter("token");
        if (token == null){
            result.setCode(Result.PARAM_ERROR);
            String msg = "token is null.我能吞下玻璃不伤身体。";
            logger.warn(msg);
            result.setMsg(msg);
            return result.toJson();
        }
        return null;
    }

    @Override
    public ZuulFilterResult runFilter() {
        ZuulFilterResult zr = new ZuulFilterResult();
        if (!isFilterDisabled()) {
            if (shouldFilter()) {
                Tracer t = TracerFactory.instance().startMicroTracer("ZUUL::" + this.getClass().getSimpleName());
                try {
                    Object res = run();
                    if (res != null){
                        RequestContext ctx = RequestContext.getCurrentContext();
                        ctx.setSendZuulResponse(false); // 不发
                        ctx.setResponseBody(res.toString());
                        ctx.getResponse().setContentType("text/html;charset=utf-8");
                        
                    }
                    zr = new ZuulFilterResult(res, ExecutionStatus.SUCCESS);
                } catch (Throwable e) {
                    t.setName("ZUUL::" + this.getClass().getSimpleName() + " failed");
                    zr = new ZuulFilterResult(ExecutionStatus.FAILED);
                    zr.setException(e);
                } finally {
                    t.stopAndLog();
                }
            } else {
                zr = new ZuulFilterResult(ExecutionStatus.SKIPPED);
            }
        }
        return zr;
    }
}
