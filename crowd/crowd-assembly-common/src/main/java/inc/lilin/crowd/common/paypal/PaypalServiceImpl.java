package inc.lilin.crowd.common.paypal;

import com.paypal.api.payments.*;
import com.paypal.base.Constants;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.*;

@Service
public class PaypalServiceImpl implements PaypalService {

    @Override
    public Payment createPayment(String extraParam,
                                 String orderNo,
                                 Double total,
                                 String currency,
                                 PaypalPaymentMethod method,
                                 PaypalPaymentIntent intent,
                                 String description,
                                 String cancelUrl,
                                 String successUrl) throws PayPalRESTException {
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        transaction.setInvoiceNumber(orderNo);
        transaction.setCustom(extraParam);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString());

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(new PaypalConfig().getApiContext());
    }

    @Override
    public Payment executePayment(String paymentId,
                                  String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(new PaypalConfig().getApiContext(), paymentExecute);
    }


    @Override
    public Boolean webhookValidate(String body,
                                   HttpServletRequest request) throws PayPalRESTException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        APIContext apiContext = new PaypalConfig().getApiContext();
        apiContext.addConfiguration(Constants.PAYPAL_WEBHOOK_ID, PaypalConfig.WEBHOOK_ID);
        Boolean result = Event.validateReceivedEvent(apiContext, getHeadersInfo(request), body);
        return result;
    }

    /**
     * 引用参考github上paypal-restful-api-example
     */
    private static Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }
}