package inc.lilin.crowd.admin.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminT;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminTMapper;
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
    AdminTMapper adminMapper;

    @Override
    public PageInfo<AdminT> getAdmins(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<AdminT> list = adminMapper.selectAdminByKeyword(keyword);

        return new PageInfo<>(list);
    }

    @Override
    public void deleteAdmin(Integer adminId) {
        adminMapper.deleteByPrimaryKey(adminId);
    }

    @Override
    public void createAdmin(AdminT admin) {
        String encodePswd = new BCryptPasswordEncoder().encode(admin.getUserPswd());
        admin.setUserPswd(encodePswd);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        admin.setCreateTime(formatter.format(LocalDateTime.now()));
        adminMapper.insert(admin);
    }

    @Override
    public AdminT getAdminByID(Integer adminId) {
        return adminMapper.selectByPrimaryKey(adminId);
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void update(AdminT admin) {
        AdminT adminFromDB = adminMapper.selectByPrimaryKey(admin.getId());
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
}
