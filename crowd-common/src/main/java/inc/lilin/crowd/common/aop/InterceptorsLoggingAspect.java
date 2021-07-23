//package inc.lilin.crowd.common.aop;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Aspect
//@Component
//public class InterceptorsLoggingAspect {
//
////  有bug，無法正確對 攔截器做point cut
////   @Pointcut("execution(public * inc.lilin.crowd.*.Interceptors.*.*(..))")
//    @Pointcut("@annotation(inc.lilin.crowd.common.aop.InterceptorsLog)")
//    public void interceptorsLog() {
//    }
//
//    @After("interceptorsLog()")
//    public void beforeMethod(JoinPoint joinPoint) {
//        log.debug("進入攔截器 " + joinPoint.getThis().getClass().getSimpleName() + "執行 " + joinPoint.getSignature().getName());
//    }
//}
