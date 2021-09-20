package inc.lilin.crowd.common.paypal;

import com.paypal.base.rest.APIContext;

public class PaypalConfig {

    // {你的应用编号}
    private String clientId = "AbuQi3TThOUcTEbnwIDy4yS4q8wxP9FiOtaVoyHSougIkWG2wEuWsAgIC7nHg1AuhD-6zOSypyLE4g6C";

    // {你的秘钥}
    private String clientSecret = "EGFQ8zIk__qtzDVLiQFaT5g1I8q7jXir7MGJdexRurBDar34zZzWnZXHgwcb1Y0tUOgeAe1UamGjTiTG";
    /**
     * sandbox 沙盒  live 生产
     */
    private String mode = "sandbox";
    /**
     * WEBHOOK_ID
     */
    public static final String WEBHOOK_ID = "1U331971MR443433F";

    private APIContext apiContext = new APIContext(clientId, clientSecret, mode);

    public APIContext getApiContext() {
        return apiContext;
    }

}
