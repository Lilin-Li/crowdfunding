package inc.lilin.crowd.admin.exception_handler;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
    public String handleException(Exception ex){
        System.out.println("例外測試");
        return "exception";
    }

}
