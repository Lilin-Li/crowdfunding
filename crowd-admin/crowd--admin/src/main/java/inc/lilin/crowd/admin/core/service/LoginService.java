package inc.lilin.crowd.admin.core.service;

import inc.lilin.crowd.common.database.AdminPO;

public interface LoginService {

    AdminPO getAdminByLoginAcct(String loginAcct, String userPswd);
}
