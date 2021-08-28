package inc.lilin.crowd.crowd.member.core.service;

import inc.lilin.crowd.common.database.MemberPO;

public interface MemberService {
    MemberPO getMemberPOByLoginAcct(String loginacct);
}
