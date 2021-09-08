package inc.lilin.crowd.member.web;

import inc.lilin.crowd.common.core.constant.CrowdConstant;
import inc.lilin.crowd.database.MemberMapper;
import inc.lilin.crowd.entity.po.MemberPO;
import inc.lilin.crowd.entity.vo.MemberLoginVO;
import inc.lilin.crowd.entity.vo.MemberVO;
import inc.lilin.crowd.entity.vo.ResultVO;
import inc.lilin.crowd.member.config.ShortMessageProperties;
import inc.lilin.crowd.member.core.service.MemberService;
import inc.lilin.crowd.redis.RedisHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Controller
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired
    private RedisHandler redisHandler;

    @Autowired
    private MemberMapper memberMapper;

    @RequestMapping("/get/memberpo/by/login/acct/remote")
    public ResultVO<MemberPO> getMemberPOByLoginAcctRemote(@RequestParam("loginacct") String loginacct) {
        MemberPO memberPO = memberService.getMemberPOByLoginAcct(loginacct);
        return ResultVO.successWithData(memberPO);
    }

    // 簡訊驗證碼
    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultVO<String> sendMessage(@RequestParam("phoneNum") String phoneNum) {

        // 1.發送驗證碼到phoneNum手機
        // ResultVO<String> sendMessageResultEntity = CrowdUtil.sendCodeByShortMessage(
        //         shortMessageProperties.getHost(),
        //         shortMessageProperties.getPath(),
        //         shortMessageProperties.getMethod(), phoneNum,
        //         shortMessageProperties.getAppCode(),
        //         shortMessageProperties.getSign(),
        //         shortMessageProperties.getSkin());

        // 2.判斷簡訊發送結果
        // if (ResultVO.SUCCESS.equals(sendMessageResultEntity.getResult())) {
        // 3.如果發送成功，則將驗證碼存入Redis
        // ①從上一步操作的結果中獲取隨機產生的驗證碼
        // String code = sendMessageResultEntity.getData();
        String code = "1111";

        // ②拼接一個用於在Redis中儲存數據的key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

        // ③存入Redis
        try {
            redisHandler.setRedisKeyValueRemoteWithTimeout(key, code, 15, TimeUnit.MINUTES);
            return ResultVO.successWithoutData();
        } catch (Exception e) {
            return ResultVO.failed(e.getMessage());
            // }
        }
    }

    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {

        // 1.獲取使用者輸入的手機號
        String phoneNum = memberVO.getPhoneNum();

        // 2.拼Redis中儲存驗證碼的Key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

        // 3.從Redis讀取Key對應的value
        String redisCode;
        try {
            redisCode = redisHandler.getRedisStringValueByKeyRemote(key);
        } catch (Exception e) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, e.getMessage());
            return "member-reg";
        }

        if (redisCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }

        // 5.如果從Redis能夠查詢到value則比較表單驗證碼和Redis驗證碼
        String formCode = memberVO.getCode();
        if (!Objects.equals(formCode, redisCode)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_INVALID);
            return "member-reg";
        }

        // 6.如果驗證碼一致，則從Redis刪除
        redisHandler.removeRedisKeyRemote(key);

        // 7.執行密碼加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userpswdBeforeEncode = memberVO.getUserpswd();
        String userpswdAfterEncode = passwordEncoder.encode(userpswdBeforeEncode);
        memberVO.setUserpswd(userpswdAfterEncode);

        // 8.執行儲存
        MemberPO memberPO = new MemberPO();
        BeanUtils.copyProperties(memberVO, memberPO);
        memberService.saveMember(memberPO);

        // 使用重定向避免重新整理瀏覽器導致重新執行註冊流程
        return "redirect:/auth/member/to/login/page";
    }

    @RequestMapping("/auth/member/do/login")
    public String login(
            @RequestParam("loginacct") String loginacct,
            @RequestParam("userpswd") String userpswd,
            ModelMap modelMap,
            HttpSession session) {

        // 1.呼叫遠端介面根據登錄賬號查詢MemberPO對像
        MemberPO memberPO;
        try {
            memberPO = memberService.getMemberPOByLoginAcct(loginacct);
        } catch (Exception e) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, e.getMessage());
            return "member-login";
        }

        if (memberPO == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        // 2.比較密碼
        String userpswdDataBase = memberPO.getUserpswd();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matcheResult = passwordEncoder.matches(userpswd, userpswdDataBase);

        if (!matcheResult) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }

        // 3.建立MemberLoginVO對像存入Session域
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);

        return "redirect:" + CrowdConstant.GATEWAY_URL + "/auth/member/to/center/page";
    }

    @RequestMapping("/auth/member/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:" + CrowdConstant.GATEWAY_URL;
    }
}
