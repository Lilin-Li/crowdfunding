package inc.lilin.crowd.common.web.exceptionhandler;

import inc.lilin.crowd.common.core.SystemConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ModelAndView handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExceptionsHandlingTools.printStack(ex);
        return ExceptionsHandlingTools.resolve(SystemConstant.EXCEPTION_VIEW, ex, request, response);
    }
}
