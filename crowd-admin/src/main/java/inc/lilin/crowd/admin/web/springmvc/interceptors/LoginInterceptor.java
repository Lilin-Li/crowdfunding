package inc.lilin.crowd.admin.web.springmvc.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor  {

    // 改用Spring Security

    // @Override
    // public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    //     HttpSession session = request.getSession();
    //     AdminT admin =(AdminT)session.getAttribute(SystemConstant.SESSION_LOGIN_ADMIN);
    //
    //     if(admin == null){
    //         throw new AccrbiddenException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorCodeAndMes());
    //     }
    //     return true;
    // }
}
