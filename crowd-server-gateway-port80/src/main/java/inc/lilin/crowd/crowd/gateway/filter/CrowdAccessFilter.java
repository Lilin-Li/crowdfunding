package inc.lilin.crowd.crowd.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import inc.lilin.crowd.common.core.constant.CrowdConstant;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CrowdAccessFilter extends ZuulFilter {

	// 判斷請求是否要過濾
	// 要過濾：返回true，繼續執行run()方法
	// 不過綠：返回false，直接放行

	@Override
	public boolean shouldFilter() {

		// 1.獲取RequestContext對像
		RequestContext requestContext = RequestContext.getCurrentContext();

		// 2.通過RequestContext對像獲取目前請求對像（框架底層是藉助ThreadLocal從目前執行緒上獲取事先繫結的Request對像）
		HttpServletRequest request = requestContext.getRequest();

		// 3.獲取servletPath值
		String servletPath = request.getServletPath();

		// 4.根據servletPath判斷目前請求是否對應可以直接放行的特定功能
		boolean containsResult = AccessPassResources.PASS_RES_SET.contains(servletPath);

		if (containsResult) {
			// 5.如果目前請求是可以直接放行的特定功能請求則返回false放行
			return false;
		}

		// 5.判斷目前請求是否為靜態資源
		// 工具方法返回true：說明目前請求是靜態資源請求，取反為false表示放行不做登錄檢查
		// 工具方法返回false：說明目前請求不是可以放行的特定請求也不是靜態資源，取反為true表示需要做登錄檢查
		return !AccessPassResources.judgeCurrentServletPathWetherStaticResource(servletPath);
	}

	@Override
	public Object run() throws ZuulException {

		// 1.獲取目前請求對像
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest request = requestContext.getRequest();

		// 2.獲取目前Session對像
		HttpSession session = request.getSession();

		// 3.嘗試從Session對像中獲取已登錄的使用者
		Object loginMember = session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);

		// 4.判斷loginMember是否為空
		if(loginMember == null) {

			// 5.從requestContext對像中獲取Response對像
			HttpServletResponse response = requestContext.getResponse();

			// 6.將提示訊息存入Session域
			session.setAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_ACCESS_FORBIDEN);

			// 7.重定向到auth-consumer工程中的登錄頁面
			try {
				response.sendRedirect("/auth/member/to/login/page");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	@Override
	public String filterType() {

		// 這裡返回「pre」意思是在目標微服務前執行過濾
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

}
