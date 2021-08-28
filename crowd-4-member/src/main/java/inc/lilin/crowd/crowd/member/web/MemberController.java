package inc.lilin.crowd.crowd.member.web;

import inc.lilin.crowd.common.database.MemberPO;
import inc.lilin.crowd.common.web.vo.ResultVO;
import inc.lilin.crowd.crowd.member.core.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    @RequestMapping("/get/memberpo/by/login/acct/remote")
    public ResultVO<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct) {
            MemberPO memberPO = memberService.getMemberPOByLoginAcct(loginacct);
            return ResultVO.successWithData(memberPO);
    }
}
