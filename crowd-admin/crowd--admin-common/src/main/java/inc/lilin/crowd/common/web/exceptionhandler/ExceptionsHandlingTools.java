package inc.lilin.crowd.common.web.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import inc.lilin.crowd.common.web.requestTools.RequestHeadTools;
import inc.lilin.crowd.common.web.vo.ResultVO;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExceptionsHandlingTools implements EnvironmentAware {

    public static Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }

    public static void printStack(Exception ex){
        if(env.getProperty("myDev.mode").equals("on")) {
            ex.printStackTrace();
        }

    }

    // 對JSON 與 Web請求，分別處理。
    public static ModelAndView resolve(String viewName, Exception exception, HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isThis_a_JSON_Request = RequestHeadTools.isThis_a_JSON_Request(request);

        // 如果是JSON請求
        if (isThis_a_JSON_Request) {
            ResultVO<Object> resultEntity = ResultVO.failed(exception.getMessage());

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
