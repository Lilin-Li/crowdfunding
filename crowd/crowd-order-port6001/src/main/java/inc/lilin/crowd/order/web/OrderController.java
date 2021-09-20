package inc.lilin.crowd.order.web;

import com.alibaba.fastjson.JSONObject;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import inc.lilin.crowd.common.core.constant.CrowdConstant;
import inc.lilin.crowd.common.paypal.PaypalPaymentIntent;
import inc.lilin.crowd.common.paypal.PaypalPaymentMethod;
import inc.lilin.crowd.common.paypal.PaypalService;
import inc.lilin.crowd.common.paypal.URLUtils;
import inc.lilin.crowd.entity.vo.AddressVO;
import inc.lilin.crowd.entity.vo.MemberLoginVO;
import inc.lilin.crowd.entity.vo.OrderProjectVO;
import inc.lilin.crowd.entity.vo.OrderVO;
import inc.lilin.crowd.order.core.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    PaypalService paypalService;

    public static final String PAYPAL_SUCCESS_URL = "paypal/success.do";
    public static final String PAYPAL_CANCEL_URL = "paypal/cancel.do";

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("returnId") Integer returnId,
            HttpSession session,
            Model model) {
        OrderProjectVO orderProjectVO = orderService.getOrderProjectVO(projectId, returnId);

        session.setAttribute("orderProjectVO", orderProjectVO);
        model.addAttribute("orderProjectVO", orderProjectVO);


        return "confirm_return";
    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(
            @PathVariable("returnCount") Integer returnCount,
            HttpSession session) {
        // 1.把接收到的回報數量合併到 Session 域
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        orderProjectVO.setReturnCount(returnCount);
        session.setAttribute("orderProjectVO", orderProjectVO);

        // 2.獲取目前已登錄使用者的 id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();

        // 3.查詢目前的收貨地址數據
        List<AddressVO> list = orderService.getAddressVO(memberId);
        session.setAttribute("addressVOList", list);

        return "confirm_order";
    }

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session) {
        orderService.saveAddress(addressVO);
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        Integer returnCount = orderProjectVO.getReturnCount();
        return "redirect:" + CrowdConstant.GATEWAY_URL + "/order/confirm/order/" + returnCount;
    }

    @ResponseBody
    @RequestMapping("/generate/order")
    public String generateOrder(HttpSession session, OrderVO orderVO, HttpServletRequest request, HttpServletResponse response) {
        // 1.從 Session 域獲取 orderProjectVO 對像
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        // 2.將 orderProjectVO 對像和 orderVO 對像組裝到一起
        orderVO.setOrderProjectVO(orderProjectVO);

        // 3.產生訂單號並設定到 orderVO 對像中
        // ①根據目前日期時間產生字串
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // ②使用 UUID 產生使用者 ID 部分
        String user = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        // ③組裝
        String orderNum = time + user;
        // ④設定到 OrderVO 對像中
        orderVO.setOrderNum(orderNum);


        // 4.計算訂單總金額並設定到 orderVO 對像中
        Double orderAmount =
                (double) (orderProjectVO.getSupportPrice() *
                        orderProjectVO.getReturnCount() +
                        orderProjectVO.getFreight());
        orderVO.setOrderAmount(orderAmount);


        String cancelUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
        String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;

        // 5.呼叫專門封裝好的方法給支付寶介面發送請求
        // return sendRequestToAliPay(
        //         orderNum,
        //         orderAmount,
        //         orderProjectVO.getProjectName(),
        //         orderProjectVO.getReturnContent());
        try {
            Payment payment = paypalService.createPayment(
                    null,
                    orderVO.getOrderNum(),
                    orderVO.getOrderAmount(),
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "service fee",
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    response.sendRedirect(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return;
    }

    @ResponseBody
    @RequestMapping("/return")
    public String returnUrlMethod(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws UnsupportedEncodingException {

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


        // // 獲取支付寶GET過來反饋資訊
        // Map<String, String> params = new HashMap<String, String>();
        // Map<String, String[]> requestParams = request.getParameterMap();
        // for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
        //     String name = (String) iter.next();
        //     String[] values = (String[]) requestParams.get(name);
        //     String valueStr = "";
        //     for (int i = 0; i < values.length; i++) {
        //         valueStr = (i == values.length - 1) ? valueStr + values[i]
        //                 : valueStr + values[i] + ",";
        //     }
        //     // 亂碼解決，這段程式碼在出現亂碼時使用
        //     valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
        //     params.put(name, valueStr);
        // }
        //
        // boolean signVerified = AlipaySignature.rsaCheckV1(
        //         params,
        //         payProperties.getAlipayPublicKey(),
        //         payProperties.getCharset(),
        //         payProperties.getSignType()); //呼叫SDK驗證簽名
        //
        // // ——請在這裡編寫您的程式（以下程式碼僅作參考）——
        // if (signVerified) {
        //     // 商戶訂單號
        //     String orderNum = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");
        //
        //     // 支付寶交易號
        //     String payOrderNum = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");
        //
        //     // 付款金額
        //     String orderAmount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"), "UTF-8");
        //
        //     // 儲存到數據庫
        //     // 1.從Session域中獲取OrderVO對像
        //     OrderVO orderVO = (OrderVO) session.getAttribute("orderVO");
        //
        //     // 2.將支付寶交易號設定到OrderVO對像中
        //     orderVO.setPayOrderNum(payOrderNum);
        //
        //     orderService.saveOrder(orderVO);
        //
        //     return "trade_no:" + payOrderNum + "<br/>out_trade_no:" + orderNum + "<br/>total_amount:" + orderAmount;
        // } else {
        //     // 頁面顯示資訊：驗簽失敗
        //     return "驗簽失敗";
        // }
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

    @RequestMapping("/notify")
    public void notifyUrlMethod(HttpServletRequest request) throws UnsupportedEncodingException, AlipayApiException {

        //獲取支付寶POST過來反饋資訊
        Map<String, String> params = new HashMap<String, String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //亂碼解決，這段程式碼在出現亂碼時使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(
                params,
                payProperties.getAlipayPublicKey(),
                payProperties.getCharset(),
                payProperties.getSignType()); //呼叫SDK驗證簽名

        //——請在這裡編寫您的程式（以下程式碼僅作參考）——

		/* 實際驗證過程建議商戶務必新增以下校驗：
		1、需要驗證該通知數據中的out_trade_no是否為商戶系統中建立的訂單號，
		2、判斷total_amount是否確實為該訂單的實際金額（即商戶訂單建立時的金額），
		3、校驗通知中的seller_id（或者seller_email) 是否為out_trade_no這筆單據的對應的操作方（有的時候，一個商戶可能有多個seller_id/seller_email）
		4、驗證app_id是否為該商戶本身。
		*/
        if (signVerified) {//驗證成功
            //商戶訂單號
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //支付寶交易號
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"), "UTF-8");

            //交易狀態
            String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"), "UTF-8");

            //判斷交易狀態
            //看此交易是否處理過
            //若未處理，執行後續物流等業務流程

            logger.info("out_trade_no=" + out_trade_no);
            logger.info("trade_no=" + trade_no);
            logger.info("trade_status=" + trade_status);

        } else {//驗證失敗
            //除錯用，寫文字函式記錄程式執行情況是否正常
            //String sWord = AlipaySignature.getSignCheckContentV1(params);
            //AlipayConfig.logResult(sWord);

            logger.info("驗證失敗");
        }

    }
}
