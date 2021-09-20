package inc.lilin.crowd.order.web;

import com.alibaba.fastjson.JSONObject;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RelatedResources;
import com.paypal.api.payments.Sale;
import com.paypal.base.rest.PayPalRESTException;
import inc.lilin.crowd.common.paypal.PaypalPaymentIntent;
import inc.lilin.crowd.common.paypal.PaypalPaymentMethod;
import inc.lilin.crowd.common.paypal.PaypalService;
import inc.lilin.crowd.common.paypal.URLUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 贝宝支付
 */
@RequestMapping("/paypal")
@Controller
public class PaypalController extends BaseController {

    public static final String PAYPAL_SUCCESS_URL = "paypal/success.do";
    public static final String PAYPAL_CANCEL_URL = "paypal/cancel.do";

    @Autowired
    private PaypalService paypalService;

    @RequestMapping(method = RequestMethod.GET,value = "index.do")
    public String index(){
        return "paypal/index";
    }

    /**
     * paypal 下单接口
     * @param money
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, value = "pay.do")
    public void pay(Double money,HttpServletRequest request, HttpServletResponse response) throws Exception {
        String cancelUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
        String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;

        //前置业务判断 此处省略

        // 生成支付订单号
        String orderid = "你的业务规则";

        //执行业务 此处省略

        //拓展参数
        JSONObject extraParam = new JSONObject();
        extraParam.put("key1",value1);
        extraParam.put("key2",value2);
        try {
            //创建支付
            Payment payment = paypalService.createPayment(
                    extraParam.toJSONString(),
                    orderid,
                    money,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "service fee",
                    cancelUrl,
                    successUrl);
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    response.sendRedirect(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return;
    }

    /**
     * 订单撤销
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "cancel")
    public String cancelPay(){
        return "paypal/cancel";
    }

    /**
     * paypal 异步执行支付接口 返回支付结果
     * @param paymentId
     * @param payerId
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "success")
    public void successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletRequest request, HttpServletResponse response) throws Exception{
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if(payment.getState().equals("approved")){
                if (paySuccess(payment, request)) {
                    request.getRequestDispatcher("/jsp/paypal/success.jsp").forward(request, response);
                    return;
                } else {
                    request.getRequestDispatcher("/jsp/paypal/cancel.jsp").forward(request, response);
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return;
    }

    /**
     * paypal webhook 异步回调
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/notify.do")
    @ResponseBody
    public void notifyUrl(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String body = getBody(request);
        JSONObject json = JSONObject.parseObject(body);
        log.info("贝宝通知>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+json.toJSONString());
        // 获取支POST过来反馈信息
        boolean isValid = paypalService.webhookValidate(body,request);
        if(isValid) {
            log.info("webhook数据验证通过，event_type="+json.getString("event_type"));
            Payment payment = new Payment();
            JSONObject resource = json.getJSONObject("resource");
            List<Transaction> transactionList = new ArrayList<>();
            Transaction transaction = new Transaction();
            transaction.setInvoiceNumber(resource.getString("invoice_number"));
            transaction.setCustom(resource.getString("custom"));
            List<RelatedResources> relatedResourcesList = new ArrayList<>();
            RelatedResources relatedResources = new RelatedResources();
            //paypal订单唯一识别号
            Sale sale = new Sale();
            sale.setId(resource.getString("id"));
            //支付状态
            sale.setState(resource.getString("state"));
            relatedResources.setSale(sale);
            relatedResourcesList.add(relatedResources);
            transaction.setRelatedResources(relatedResourcesList);
            transactionList.add(transaction);
            payment.setTransactions(transactionList);
            if(sale.getState().equalsIgnoreCase("completed")) {
                if (paySuccess(payment, request)) {
                    response.setStatus(200);
                    return;
                }else{
                    log.warn("此支付订单更新失败，订单ID=" + sale.getState() + ",参数信息：" + BeanUtil.objectToJson(payment));
                    //这里返回500或者其他HTTP错误码，即可重发
                    response.setStatus(500);
                    return;
                }
            }
        }
    }


    /**
     * 支付成功的处理逻辑
     *
     * @param payment
     *            贝宝的参数信息
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public boolean paySuccess(Payment payment, HttpServletRequest request) {
        // 交易状态
        String status = "";
        for(Links links : payment.getLinks()){
            if(links.getRel().equals("approval_url")){
                status=links.getRel();
                break;
            }
            status=links.getRel();
        }
        // 商户订单号
        String out_trade_no = payment.getTransactions().get(0).getInvoiceNumber();
        // paypal交易号
        String trade_no = payment.getId();
        // 附加json字段，取出下单时存放的业务信息
        String extra_common_param = payment.getTransactions().get(0).getCustom();
        JSONObject extraParam = JSONObject.parseObject(extra_common_param);

        //执行业务 此处省略

        return true;
    }

    /**
     * 引用参考github上paypal-restful-api-example
     */
    private static String getBody(HttpServletRequest request) throws IOException {
        String body;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

}