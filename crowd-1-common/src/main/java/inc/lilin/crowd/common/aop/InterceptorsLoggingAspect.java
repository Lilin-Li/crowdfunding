package inc.lilin.crowd.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Aspect
@Component
public class InterceptorsLoggingAspect implements HandlerInterceptor {

    @Pointcut("execution(public * inc.lilin.crowd.*.web.springmvc.interceptors.*.*(..))")
    public void interceptorsLog() {
    }

    @Around("interceptorsLog()")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable{

        Object result = null;
        try {
            //=================前置通知=====================
            log.debug("進入攔截器 " + joinPoint.getTarget().getClass().getSimpleName() + " 執行 " + joinPoint.getSignature().getName() + " 函數");
            result = joinPoint.proceed();
            //=================返回通知=====================
        } catch (Throwable e) {
            //==================異常通知=====================
            throw e;
        } finally {
            //=================後置通知=====================
        }
        return result;
    }

}
