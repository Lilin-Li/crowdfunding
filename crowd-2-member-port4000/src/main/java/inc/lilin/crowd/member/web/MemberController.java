package inc.lilin.crowd.member.web;

import inc.lilin.crowd.common.core.constant.CrowdConstant;
import inc.lilin.crowd.common.util.CrowdUtil;
import inc.lilin.crowd.entity.po.MemberPO;
import inc.lilin.crowd.entity.vo.MemberVO;
import inc.lilin.crowd.entity.vo.ResultVO;
import inc.lilin.crowd.member.config.ShortMessageProperties;
import inc.lilin.crowd.member.core.service.MemberService;
import inc.lilin.crowd.redis.RedisHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired
    private RedisHandler redisHandler;

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
        ResultVO<String> sendMessageResultEntity = CrowdUtil.sendCodeByShortMessage(
                shortMessageProperties.getHost(),
                shortMessageProperties.getPath(),
                shortMessageProperties.getMethod(), phoneNum,
                shortMessageProperties.getAppCode(),
                shortMessageProperties.getSign(),
                shortMessageProperties.getSkin());

        // 2.判斷簡訊發送結果
        if(ResultVO.SUCCESS.equals(sendMessageResultEntity.getResult())) {
            // 3.如果發送成功，則將驗證碼存入Redis
            // ①從上一步操作的結果中獲取隨機產生的驗證碼
            String code = sendMessageResultEntity.getData();

            // ②拼接一個用於在Redis中儲存數據的key
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

            // ③呼叫遠端介面存入Redis
            ResultVO<String> saveCodeResultEntity = redisHandler.setRedisKeyValueRemoteWithTimeout(key, code, 15, TimeUnit.MINUTES);

            // ④判斷結果
            if(ResultVO.SUCCESS.equals(saveCodeResultEntity.getResult())) {

                return ResultVO.successWithoutData();
            }else {
                return saveCodeResultEntity;
            }
        } else {
            return sendMessageResultEntity;
        }
    }

    @RequestMapping("/auth/do/member/register")
    public String register(MemberVO memberVO, ModelMap modelMap) {

        // 1.獲取使用者輸入的手機號
        String phoneNum = memberVO.getPhoneNum();

        // 2.拼Redis中儲存驗證碼的Key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

        // 3.從Redis讀取Key對應的value
        ResultVO<String> resultEntity = redisHandler.getRedisStringValueByKeyRemote(key);

        String result = resultEntity.getResult();
        if(ResultVO.FAILED.equals(result)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-reg";
        }

        String redisCode = resultEntity.getData();

        if(redisCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-reg";
        }

        // 5.如果從Redis能夠查詢到value則比較表單驗證碼和Redis驗證碼
        String formCode = memberVO.getCode();

        if(!Objects.equals(formCode, redisCode)) {
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
        // ①建立空的MemberPO對像
        MemberPO memberPO = new MemberPO();

        // ②複製屬性
        BeanUtils.copyProperties(memberVO, memberPO);

        // ③呼叫遠端方法
        ResultVO<String> saveMemberResultEntity = mySQLRemoteService.saveMember(memberPO);

        if(ResultVO.FAILED.equals(saveMemberResultEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());
            return "member-reg";
        }

        // 使用重定向避免重新整理瀏覽器導致重新執行註冊流程
        return "redirect:/auth/member/to/login/page";
    }
}
