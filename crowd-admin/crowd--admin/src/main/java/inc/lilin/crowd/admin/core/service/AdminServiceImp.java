package inc.lilin.crowd.admin.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.core.exception.LoginFailedException;
import inc.lilin.crowd.common.core.ErrorCodeEnum;
import inc.lilin.crowd.database.AdminMapper;
import inc.lilin.crowd.entity.po.AdminPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AdminServiceImp implements AdminService {

    @Autowired
    AdminMapper adminMapper;

    @Override
    public PageInfo<AdminPO> getAdmins(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<AdminPO> list = adminMapper.selectAdminByKeyword(keyword);

        return new PageInfo<>(list);
    }

    @Override
    public void deleteAdmin(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public void createAdmin(AdminPO admin) {
        String encodePswd = new BCryptPasswordEncoder().encode(admin.getUserPswd());
        admin.setUserPswd(encodePswd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        admin.setCreateTime(formatter.format(LocalDateTime.now()));
        adminMapper.insert(admin);
    }

    @Override
    public AdminPO getAdminByID(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void update(AdminPO admin) {
        AdminPO adminFromDB = adminMapper.selectByPrimaryKey(admin.getId());
        admin.setUserPswd(adminFromDB.getUserPswd());
        admin.setCreateTime(adminFromDB.getCreateTime());

        adminMapper.updateByPrimaryKey(admin);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void saveAdminRoleRelationship(Integer adminId, List<Integer> roleIdList) {
        // ??????????????????
        // adminId roleId
        // 1 1???????????????
        // 1 2???????????????
        // 1 3
        // 1 4
        // 1 5
        // ??????????????????
        // adminId roleId
        // 1 3??????????????????
        // 1 4??????????????????
        // 1 5??????????????????
        // 1 6?????????
        // 1 7?????????
        // ?????????????????????????????? adminId ?????????????????????????????? roleIdList ????????????????????????

        adminMapper.deleteOLdRelationship(adminId);
        if (roleIdList != null && roleIdList.size() > 0) {
            adminMapper.insertNewRelationship(adminId, roleIdList);
        }
    }

    @Override
    public AdminPO getAdminByLoginAcct(String username) {
        List<AdminPO> list = adminMapper.selectByAcct(username);

        if(list == null || list.size() == 0) {
            throw new LoginFailedException(ErrorCodeEnum.LOGIN_FAILED_ACCT_NOT_EXIST.getErrorCodeAndMes());
        }
        if(list.size() > 1) {
            throw new LoginFailedException(ErrorCodeEnum.LOGIN_FAILED_ACCT_NOT_UNIQUE.getErrorCodeAndMes());
        }

        AdminPO admin = list.get(0);
        if(admin == null) {
            throw new LoginFailedException(ErrorCodeEnum.LOGIN_FAILED_RESULT_NULL.getErrorCodeAndMes());
        }
        return admin;
    }
}
