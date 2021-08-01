package inc.lilin.crowd.admin.core.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminT;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminTMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RbacServiceImp implements RbacService {

    @Autowired
    AdminTMapper adminMapper;

    @Override
    public PageInfo<AdminT> getUsers(String keyword, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<AdminT> list = adminMapper.selectAdminByKeyword(keyword);

        return new PageInfo<>(list);
    }
}
