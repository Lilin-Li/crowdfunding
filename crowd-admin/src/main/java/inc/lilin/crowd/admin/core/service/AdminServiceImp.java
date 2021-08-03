package inc.lilin.crowd.admin.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminT;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminTMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
}
