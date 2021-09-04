package inc.lilin.crowd.admin.web;

import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.core.exception.DeleteAdminFailedException;
import inc.lilin.crowd.admin.core.exception.DuplicateAcctOrEmailException;
import inc.lilin.crowd.admin.core.service.AdminService;
import inc.lilin.crowd.common.core.ErrorCodeEnum;
import inc.lilin.crowd.common.core.SystemConstant;
import inc.lilin.crowd.entity.po.AdminPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
public class AdminController {
    @Autowired
    AdminService adminService;

    /*
     *    改用SpringSecurity
     */
    // @GetMapping("admin/logout")
    // public String adminLogout(HttpSession session) throws Exception {
    //     session.invalidate();
    //     return "redirect:/";
    // }

    @GetMapping("/users")
    public String getAdmins(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize, Map<String, Object> map,
                            @RequestParam(value = "exceptionMsg", defaultValue = "") String exceptionMsg) throws Exception {

        PageInfo<AdminPO> pageInfo = adminService.getAdmins(keyword, pageNum, pageSize);
        map.put("pageInfo", pageInfo);
        return "user";
    }

    @GetMapping({"/user/delete/{adminId}/{pageNum}/{keyword}", "/user/delete/{adminId}/{pageNum}/"})
    public String deleteAdmin(@PathVariable("adminId") Integer adminId,
                              @PathVariable(value = "pageNum") Integer pageNum,
                              @PathVariable(value = "keyword", required = false) String keyword,
                              HttpSession session) throws Exception {

        // 若delete的帳號是目前登入帳號 => 拋例外
        Integer sessionAdminId = ((AdminPO) session.getAttribute(SystemConstant.SESSION_LOGIN_ADMIN)).getId();
        if (adminId.equals(sessionAdminId)) {
            throw new DeleteAdminFailedException(ErrorCodeEnum.DELETA_ADMIN_FAILED.getErrorCodeAndMes());
        }
        ;

        adminService.deleteAdmin(adminId);
        if (keyword == null) {
            keyword = "";
        }
        return "redirect:/users?pageNum=" + pageNum + "&keyword=" + keyword;
    }

    @PostMapping("/admin/create")
    public String createAdmin(AdminPO admin) throws Exception {
        try {
            adminService.createAdmin(admin);
        } catch (DuplicateKeyException duplicateKeyException) {
            throw new DuplicateAcctOrEmailException(ErrorCodeEnum.DUPLICATE_ACCT_OR_EMAIL.getErrorCodeAndMes());
        }
        return "redirect:/users";
    }

    @GetMapping({"/user/toEditPage/{adminId}/{pageNum}/{keyword}", "/user/toEditPage/{adminId}/{pageNum}/"})
    public String toEditPage(@PathVariable(value = "adminId") Integer adminId,
                             @PathVariable(value = "pageNum") Integer pageNum,
                             @PathVariable(value = "keyword", required = false) String keyword,
                             Map<String, Object> map) throws Exception {

        AdminPO admin = adminService.getAdminByID(adminId);
        map.put("admin", admin);
        map.put("adminId", adminId);
        map.put("pageNum", pageNum);
        map.put("keyword", keyword);
        return "edit";
    }

    @PostMapping("/user/edit")
    public String toEditPage(@RequestParam(value = "pageNum") Integer pageNum,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             AdminPO admin) throws Exception {
        try {
            adminService.update(admin);
        } catch (DuplicateKeyException duplicateKeyException) {
            throw new Exception("DuplicateKeyException  這在/admin/create 新建使用者時處理過，不重複demo了");
        }
        return "redirect:/users?pageNum=" + pageNum + "&keyword=" + keyword;
    }
}