package inc.lilin.crowd.admin.web.springmvc;

import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.core.service.RoleService;
import inc.lilin.crowd.admin.database.mysql.mybatis.RoleT;
import inc.lilin.crowd.common.web.springmvc.responseTools.RestResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RoleController {
    @Autowired
    RoleService roleService;

    @ResponseBody
    @PostMapping("/role/get/page/info.json")
    public RestResultDTO<PageInfo<RoleT>> getPageInfo(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
    ) {
        PageInfo<RoleT> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);

        return RestResultDTO.successWithData(pageInfo);
    }
}
