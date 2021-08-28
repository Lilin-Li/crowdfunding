package inc.lilin.crowd.admin.web.springmvc;

import inc.lilin.crowd.admin.core.service.AuthService;
import inc.lilin.crowd.admin.database.mysql.mybatis.AuthT;
import inc.lilin.crowd.common.web.springmvc.responseTools.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class RoleAssignAuth {

    @Autowired
    AuthService authService;

    @ResponseBody
    @RequestMapping("/assgin/get/all/auth.json")
    public ResultVO<List<AuthT>> getAllAauth() {
        List<AuthT> authList = authService.getAll();
        return ResultVO.successWithData(authList);
    }


    @ResponseBody
    @RequestMapping("/assign/get/assigned/auth/id/by/role/id.json")
    public ResultVO<List<Integer>> getAssignedAuthIdByRoleId(
            @RequestParam("roleId") Integer roleId) {
        List<Integer> authIdList = authService.getAssignedAuthIdByRoleId(roleId);
        return ResultVO.successWithData(authIdList);
    }

    @ResponseBody
    @RequestMapping("/assign/do/role/assign/auth.json")
    public ResultVO<String> saveRoleAuthRelathinship(
            @RequestBody Map<String, List<Integer>> map) {
        authService.saveRoleAuthRelathinship(map);
        return ResultVO.successWithoutData();
    }
}
