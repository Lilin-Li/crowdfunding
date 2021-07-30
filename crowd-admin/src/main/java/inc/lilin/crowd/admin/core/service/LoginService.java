package inc.lilin.crowd.admin.core.service;

import inc.lilin.crowd.admin.database.mysql.mybatis.AdminT;

public interface LoginService {

    AdminT getAdminByLoginAcct(String loginAcct, String userPswd);
}
