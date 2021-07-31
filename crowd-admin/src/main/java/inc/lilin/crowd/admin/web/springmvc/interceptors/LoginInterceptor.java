package inc.lilin.crowd.admin.web.springmvc.interceptors;

import inc.lilin.crowd.admin.core.exception.AccessForbiddenException;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminT;
import inc.lilin.crowd.common.core.ErrorCodeEnum;
import inc.lilin.crowd.common.core.SystemConstant;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginInterceptor implements HandlerInterceptor  {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        AdminT admin =(AdminT)session.getAttribute(SystemConstant.SESSION_LOGIN_ADMIN);

        if(admin == null){
            throw new AccessForbiddenException(ErrorCodeEnum.ACCESS_FORBIDDEN.getErrorCodeAndMes());
        }

        return true;
    }
}
