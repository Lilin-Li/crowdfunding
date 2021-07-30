package inc.lilin.crowd.admin.core.service;

import inc.lilin.crowd.admin.core.exception.LoginFailedException;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminT;
import inc.lilin.crowd.admin.database.mysql.mybatis.AdminTMapper;
import inc.lilin.crowd.common.core.ErrorCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImp implements LoginService{

    @Autowired
    AdminTMapper adminDAO;

    @Override
    public AdminT getAdminByLoginAcct(String loginAcct, String userPswd) {
        List<AdminT> list = adminDAO.selectByAcct(loginAcct);

        if(list == null || list.size() == 0) {
            throw new LoginFailedException(ErrorCodeEnum.LOGIN_FAILED_ACCT_NOT_EXIST.getErrorCodeAndMes());
        }
        if(list.size() > 1) {
            throw new LoginFailedException(ErrorCodeEnum.LOGIN_FAILED_ACCT_NOT_UNIQUE.getErrorCodeAndMes());
        }

        AdminT admin = list.get(0);
        if(admin == null) {
            throw new LoginFailedException(ErrorCodeEnum.LOGIN_FAILED_RESULT_NULL.getErrorCodeAndMes());
        }

        String userPswdDB = admin.getUserPswd();
        String userPswdForm = new BCryptPasswordEncoder().encode(userPswd);

        if(!userPswdDB.equals(userPswdForm)) {
            throw new LoginFailedException(ErrorCodeEnum.LOGIN_PASSWORD_ERROR.getErrorCodeAndMes());
        }

        return admin;
    }
}
