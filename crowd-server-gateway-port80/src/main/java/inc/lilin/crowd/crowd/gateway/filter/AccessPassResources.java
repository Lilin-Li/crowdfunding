package inc.lilin.crowd.crowd.gateway.filter;

import inc.lilin.crowd.common.core.constant.CrowdConstant;

import java.util.HashSet;
import java.util.Set;

public class AccessPassResources {

	public static final Set<String> PASS_RES_SET = new HashSet<>();

	static {
		PASS_RES_SET.add("/");
		PASS_RES_SET.add("/auth/member/to/reg/page");
		PASS_RES_SET.add("/auth/member/to/login/page");
		PASS_RES_SET.add("/auth/member/logout");
		PASS_RES_SET.add("/auth/member/do/login");
		PASS_RES_SET.add("/auth/do/member/register");
		PASS_RES_SET.add("/auth/member/send/short/message.json");
	}

	public static final Set<String> STATIC_RES_SET = new HashSet<>();

	static {
		STATIC_RES_SET.add("bootstrap");
		STATIC_RES_SET.add("css");
		STATIC_RES_SET.add("fonts");
		STATIC_RES_SET.add("img");
		STATIC_RES_SET.add("jquery");
		STATIC_RES_SET.add("layer");
		STATIC_RES_SET.add("script");
		STATIC_RES_SET.add("ztree");
	}

	/**
	 * 用於判斷某個ServletPath值是否對應一個靜態資源
	 * @param servletPath
	 * @return
	 * 		true：是靜態資源
	 * 		false：不是靜態資源
	 */
	public static boolean judgeCurrentServletPathWetherStaticResource(String servletPath) {

		// 1.排除字串無效的情況
		if(servletPath == null || servletPath.length() == 0) {
			throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
		}

		// 2.根據「/」拆分ServletPath字串
		String[] split = servletPath.split("/");

		// 3.考慮到第一個斜槓左邊經過拆分后得到一個空字串是陣列的第一個元素，所以需要使用下標1取第二個元素
		String firstLevelPath = split[1];

		// 4.判斷是否在集合中
		return STATIC_RES_SET.contains(firstLevelPath);
	}

}
