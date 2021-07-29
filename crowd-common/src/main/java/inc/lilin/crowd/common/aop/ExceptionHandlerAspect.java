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
public class ExceptionHandlerAspect implements HandlerInterceptor {

    @Pointcut("execution(public * inc.lilin.crowd.*.web.springmvc.exceptionhandler.*.*(..)) && !execution(public * inc.lilin.crowd.common.web.springmvc.exceptionhandler.ExceptionsHandlingTools.*(..))")
    public void asept() {
    }

    @Around("asept()")
    public Object doAroundAdvice(ProceedingJoinPoint joinPoint) {

        Object result = null;
        try {
            //=================前置通知=====================
            log.debug("經過例外統一處理層 " + joinPoint.getTarget().getClass().getSimpleName() + " 執行 " + joinPoint.getSignature().getName());
            result = joinPoint.proceed();
            //=================返回通知=====================
        } catch (Throwable e) {
            //==================異常通知=====================
        } finally {
            //=================後置通知=====================
        }
        return result;
    }

}