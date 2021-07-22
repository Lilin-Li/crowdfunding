package inc.lilin.crowd.common.exceptionhandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    public String handleException(Exception ex){
        log.debug("經過例外統一處理層ExceptionHandler.handleException");
        return "exception";
    }
}