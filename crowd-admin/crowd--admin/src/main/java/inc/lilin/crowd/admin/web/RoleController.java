package inc.lilin.crowd.admin.web;

import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.core.service.RoleService;
import inc.lilin.crowd.common.web.vo.ResultVO;
import inc.lilin.crowd.entity.po.RolePO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class RoleController {
    @Autowired
    RoleService roleService;

    @ResponseBody
    @PostMapping("/role/get/page/info.json")
    public ResultVO<PageInfo<RolePO>> getPageInfo(
            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "keyword", defaultValue = "") String keyword
    ) {
        PageInfo<RolePO> pageInfo = roleService.getPageInfo(pageNum, pageSize, keyword);

        return ResultVO.successWithData(pageInfo);
    }

    @ResponseBody
    @PostMapping("/role/save.json")
    public ResultVO<String> saveRole(RolePO role) {
        roleService.saveRole(role);
        return ResultVO.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/role/update.json")
    public ResultVO<String> updateRole(RolePO role) {

        roleService.updateRole(role);

        return ResultVO.successWithoutData();
    }

    @ResponseBody
    @RequestMapping("/role/remove/by/role/id/array.json")
    public ResultVO<String> removeByRoleIdAarry(@RequestBody List<Integer> roleIdList) {
        roleService.removeRole(roleIdList);
        return ResultVO.successWithoutData();
    }
}
