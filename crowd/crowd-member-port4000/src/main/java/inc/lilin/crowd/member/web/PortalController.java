package inc.lilin.crowd.member.web;

import inc.lilin.crowd.common.core.constant.CrowdConstant;
import inc.lilin.crowd.entity.vo.PortalTypeVO;
import inc.lilin.crowd.member.core.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PortalController {

    @Autowired
    ProjectService projectServiceImpl;

    @RequestMapping("/")
    public String showPortalPage(Model model) {

        // 1、呼叫MySQLRemoteService提供的方法查詢首頁要顯示的數據
        List<PortalTypeVO> list = projectServiceImpl.getPortalTypeVO();

        model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA, list);

        return"portal";
    }
}
