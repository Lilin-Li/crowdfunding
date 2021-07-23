package inc.lilin.crowd.common.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import inc.lilin.crowd.common.api.CurrentTimeMillisClock;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Aspect
@Component
public class ControllerAspect {

    @Autowired
    CurrentTimeMillisClock clock;

    /**
     * web層切點
     * 1. @Pointcut("execution(public * com.hyh.web.*.*(..))")  web層的所有方法
     * 2. @Pointcut("@annotation(com.hyh.annotation.Log)")      Log註解標註的方法
     */
    @Pointcut("execution(public * inc.lilin.crowd.admin.web.*.*(..))")
    public void interceptorsLog() {
    }

    /**
     * 環繞通知
     */
    @Around("interceptorsLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //獲取請求物件
        ServletRequestAttributes servletReqAttributes = getRequest();
        HttpServletRequest request = servletReqAttributes.getRequest();

        WebLog webLog = new WebLog();
        Object result = null;
        try {
            //=================前置通知=====================
            long start = clock.now();
            result = joinPoint.proceed();
            //=================返回通知=====================
            long timeCost = clock.now() - start;
            // 獲取Log註解

            webLog.setTimeCost((int) timeCost);
            webLog.setStartTime(start);
            webLog.setIpAddress(request.getRemoteAddr());
            webLog.setHttpMethod(request.getMethod());
            webLog.setParams(getParams(joinPoint));
            webLog.setResult(result);
            webLog.setUri(request.getRequestURI());
            webLog.setUrl(request.getRequestURL().toString());

            log.debug("{}",  new ObjectMapper().writeValueAsString(webLog));
        } catch (Throwable e) {
            //==================異常通知=====================
            log.debug("訪問 " + request.getRequestURI() + " 時出例外");
            throw new Throwable(e);
        }finally {
            //=================後置通知=====================
        }
        return result;
    }


    /**
     * 獲取引數 params:{"name":"天喬巴夏"}
     */
    private Object getParams(ProceedingJoinPoint joinPoint) {
        // 引數名
        String[] paramNames = getMethodSignature(joinPoint).getParameterNames();
        // 引數值
        Object[] paramValues = joinPoint.getArgs();
        // 儲存引數
        Map<String, Object> params = new LinkedHashMap<>();
        for (int i = 0; i < paramNames.length; i++) {
            Object value = paramValues[i];
            // MultipartFile物件以檔名作為引數值
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                value = file.getOriginalFilename();
            }
            params.put(paramNames[i], value);
        }
        return params;
    }

    private MethodSignature getMethodSignature(ProceedingJoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }


    private ServletRequestAttributes getRequest() {
        return (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    }

    @Data
    @ToString
    public static class WebLog {

        // URL
        private String url;

        // URI
        private String uri;

        // 消耗時間
        private Integer timeCost;

        // 操作時間
        private Long startTime;

        // 請求型別
        private String httpMethod;

        // IP地址
        private String ipAddress;

        // 請求引數
        private Object params;

        // 請求返回的結果
        private Object result;

    }
}
