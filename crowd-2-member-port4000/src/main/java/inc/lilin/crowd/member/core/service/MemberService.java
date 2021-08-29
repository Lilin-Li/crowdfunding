package inc.lilin.crowd.member.core.service;


import inc.lilin.crowd.entity.po.MemberPO;

public interface MemberService {
    MemberPO getMemberPOByLoginAcct(String loginacct);
}
