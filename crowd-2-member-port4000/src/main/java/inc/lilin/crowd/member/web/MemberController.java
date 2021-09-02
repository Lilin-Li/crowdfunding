package inc.lilin.crowd.member.web;

import inc.lilin.crowd.common.core.constant.CrowdConstant;
import inc.lilin.crowd.entity.po.MemberPO;
import inc.lilin.crowd.entity.vo.ResultVO;
import inc.lilin.crowd.member.config.ShortMessageProperties;
import inc.lilin.crowd.member.core.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import inc.lilin.crowd.common.util.CrowdUtil;

import java.util.concurrent.TimeUnit;

@RestController
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private ShortMessageProperties shortMessageProperties;

    @Autowired
    private RedisTemplate redisTemplate;

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
            try {
                ValueOperations<String, String> operations = redisTemplate.opsForValue();
                operations.set(key, code, 15, TimeUnit.MINUTES);
                return ResultVO.successWithoutData();
            } catch (Exception e) {
                e.printStackTrace();
                return ResultVO.failed(e.getMessage());
            }
        } else {
            return sendMessageResultEntity;
        }

    }
}
