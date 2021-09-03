package inc.lilin.crowd.admin.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.core.exception.LoginFailedException;
import inc.lilin.crowd.common.database.AdminPO;
import inc.lilin.crowd.common.database.AdminMapper;
import inc.lilin.crowd.common.core.ErrorCodeEnum;
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
        // 舊數據如下：
        // adminId roleId
        // 1 1（要刪除）
        // 1 2（要刪除）
        // 1 3
        // 1 4
        // 1 5
        // 新數據如下：
        // adminId roleId
        // 1 3（本來就有）
        // 1 4（本來就有）
        // 1 5（本來就有）
        // 1 6（新）
        // 1 7（新）
        // 爲了簡化操作：先根據 adminId 刪除舊的數據，再根據 roleIdList 儲存全部新的數據

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
