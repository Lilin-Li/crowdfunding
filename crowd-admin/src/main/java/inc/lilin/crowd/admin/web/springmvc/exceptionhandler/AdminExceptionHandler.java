package inc.lilin.crowd.admin.web.springmvc.exceptionhandler;

import inc.lilin.crowd.admin.core.exception.AccessForbiddenException;
import inc.lilin.crowd.admin.core.exception.DeleteAdminFailedException;
import inc.lilin.crowd.admin.core.exception.DuplicateAcctOrEmailException;
import inc.lilin.crowd.admin.core.exception.LoginFailedException;
import inc.lilin.crowd.common.web.springmvc.exceptionhandler.ExceptionsHandlingTools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@ControllerAdvice
public class AdminExceptionHandler {

    @ExceptionHandler({LoginFailedException.class})
    public ModelAndView handleLoginFailedException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExceptionsHandlingTools.printStack(ex);
        return ExceptionsHandlingTools.resolve("index", ex, request, response);
    }

    @ExceptionHandler({AccessForbiddenException.class})
    public ModelAndView handleAccessForbiddenException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExceptionsHandlingTools.printStack(ex);
        return ExceptionsHandlingTools.resolve("index", ex, request, response);
    }

    @ExceptionHandler({DeleteAdminFailedException.class})
    public ModelAndView handleDeletaAdminFailedException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExceptionsHandlingTools.printStack(ex);
        return ExceptionsHandlingTools.resolve("redirect:/users?exceptionMsg=" + URLEncoder.encode(ex.getMessage(),"UTF-8"), ex, request, response);
    }
    @ExceptionHandler({DuplicateAcctOrEmailException.class})
    public ModelAndView handleDuplicateAcctOrEmailException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ExceptionsHandlingTools.printStack(ex);
        return ExceptionsHandlingTools.resolve("redirect:/admin/add?exceptionMsg=" + URLEncoder.encode(ex.getMessage(),"UTF-8"), ex, request, response);
    }

}
