package inc.lilin.crowd.crowd.member.core.service;

import inc.lilin.crowd.common.database.MemberMapper;
import inc.lilin.crowd.common.database.MemberPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberPOMapper;

    @Transactional(readOnly = true)
    @Override
    public MemberPO getMemberPOByLoginAcct(String loginacct) {

        List<MemberPO> list = memberPOMapper.selectByLoginAcct(loginacct);

        return list.get(0);

    }

}
