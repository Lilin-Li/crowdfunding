package inc.lilin.crowd.common.web.springmvc.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import inc.lilin.crowd.common.web.springmvc.response.RestResultDTO;
import inc.lilin.crowd.common.web.springmvc.request.RequestHeadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@ControllerAdvice
public class crowdExceptionHandler {

    @ExceptionHandler({Exception.class})
    public ModelAndView handleException(Exception ex, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("經過例外統一處理層ExceptionHandler.handleException");
        ex.printStackTrace();
        return commonResolve("exception", ex, request, response);
    }

    // 對JSON 與 Web請求，分別處理。
    private ModelAndView commonResolve(String viewName, Exception exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isThis_a_JSON_Request = RequestHeadUtil.isThis_a_JSON_Request(request);

        // 如果是Ajax請求
        if (isThis_a_JSON_Request) {
            RestResultDTO<Object> resultEntity = RestResultDTO.failed(exception.getMessage());
            String json = new ObjectMapper().writeValueAsString(resultEntity);
            response.getWriter().write(json);    // 將JSON字串作為響應體返回給瀏覽器
            // 由於上面已經通過原生的response對像返回了響應，所以不提供ModelAndView對像
            return null;
        }

        // 如果不是Ajax請求則建立ModelAndView對像
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("exception", exception);  // 將Exception對像存入模型
        modelAndView.setViewName(viewName); // 設定對應的檢視名稱

        return modelAndView;
    }
}
