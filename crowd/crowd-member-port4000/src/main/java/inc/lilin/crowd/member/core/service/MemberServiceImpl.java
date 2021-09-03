package inc.lilin.crowd.member.core.service;


import inc.lilin.crowd.database.MemberMapper;
import inc.lilin.crowd.entity.po.MemberPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

        if(list == null || list.size() == 0) {
            return null;
        }

        return list.get(0);

    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            rollbackFor = Exception.class,
            readOnly = false)
    @Override
    public void saveMember(MemberPO memberPO) {
        memberPOMapper.insertSelective(memberPO);
    }
}
