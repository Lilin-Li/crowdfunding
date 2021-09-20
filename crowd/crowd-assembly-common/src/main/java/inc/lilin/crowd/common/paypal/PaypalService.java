package inc.lilin.crowd.common.paypal;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

/**
 * paypal支付
 */
public interface PaypalService {

    /**
     * 创建支付
     * @return Payment
     */
    Payment createPayment(String extraParam, String orderNo, Double total,
                          String currency,
                          PaypalPaymentMethod method,
                          PaypalPaymentIntent intent,
                          String description,
                          String cancelUrl,
                          String successUrl)throws PayPalRESTException;


    /**
     * 执行支付
     * @return Payment
     */
    Payment executePayment(String paymentId, String payerId) throws PayPalRESTException;

    /**
     * webhook数据验证
     * @return
     */
    Boolean webhookValidate(String body, HttpServletRequest request) throws PayPalRESTException, NoSuchAlgorithmException, InvalidKeyException, SignatureException;

}