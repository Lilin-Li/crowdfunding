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
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@Log4j2
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    PaypalService paypalService;

    public static final String PAYPAL_SUCCESS_URL = "paypal/success";
    public static final String PAYPAL_CANCEL_URL = "paypal/cancel";

    @RequestMapping("/confirm/return/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(
            @PathVariable("projectId") Integer projectId,
            @PathVariable("returnId") Integer returnId,
            HttpSession session,
            Model model) {
        OrderProjectVO orderProjectVO = orderService.getOrderProjectVO(projectId, returnId);

        session.setAttribute("orderProjectVO", orderProjectVO);
        session.setAttribute("projectId", projectId);
        model.addAttribute("orderProjectVO", orderProjectVO);


        return "confirm_return";
    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(
            @PathVariable("returnCount") Integer returnCount,
            HttpSession session) {
        // 1.把接收到的回報數量合併到 Session 域
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");

        // TODO: session過期，重導回募資詳情頁面
        // if(orderProjectVO == null)

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
        // TODO: session過期，重導回募資詳情頁面
        Integer returnCount = orderProjectVO.getReturnCount();
        return "redirect:" + CrowdConstant.GATEWAY_URL + "/order/confirm/order/" + returnCount;
    }

    @ResponseBody
    @RequestMapping("/generate/order")
    public String generateOrder(HttpSession session, OrderVO orderVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1.從 Session 域獲取 orderProjectVO 對像
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");

        // TODO: session過期，重導回募資詳情頁面

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

        session.setAttribute("orderVO", orderVO);

        String cancelUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
        String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;


        JSONObject extraParam = new JSONObject();
        extraParam.put("projectID",   (Integer)session.getAttribute("projectId"));

        try {
            Payment payment = paypalService.createPayment(
                    extraParam.toJSONString(),
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
                    // 付款頁面
                    response.sendRedirect(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "訂單建立失敗";
    }

    @ResponseBody
    @RequestMapping("/paypal/success")
    public String returnUrlMethod(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {


        Payment payment = paypalService.executePayment(paymentId, payerId);
        if (payment.getState().equals("approved")) {
            // 交易狀態
            String status = "";
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    status = links.getRel();
                    break;
                }
                status = links.getRel();
            }
            // 商戶訂單號
            String out_trade_no = payment.getTransactions().get(0).getInvoiceNumber();
            // paypal交易號
            String trade_no = payment.getId();
            // 總金額
            Double orderAmount = Double.valueOf(
                    payment.getTransactions()
                            .get(0)
                            .getAmount()
                            .getTotal());

            // 儲存到DB
            OrderVO orderVO = (OrderVO) session.getAttribute("orderVO");
            // TODO: session過期，重導回募資詳情頁面
            orderVO.setPayOrderNum(trade_no);
            orderService.saveOrder(orderVO);

            return "trade_no:" + trade_no + "<br/>out_trade_no:" + out_trade_no + "<br/>total_amount:" + orderAmount + "<br/><a href=\"" + CrowdConstant.GATEWAY_URL + "\">返回首頁";

        } else {
            return "交易未成功";
        }
    }

    /**
     * 訂單撤銷
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/paypal/cancel")
    public String cancelPay() {
        return "訂單取消";
    }

    @PostMapping("/notify")
    public void notifyUrlMethod(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = getBody(request);
        JSONObject json = JSONObject.parseObject(body);
        log.info("貝寶通知>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + json.toJSONString());

        //驗簽
        boolean isValid = paypalService.webhookValidate(body, request);

        if (isValid) {
            log.info("webhook數據驗證通過，event_type=" + json.getString("event_type"));

            JSONObject resource = json.getJSONObject("resource");
            // 實際驗證過程建議商戶務必新增以下校驗：
            resource.getString("id");   //paypal訂單唯一識別號
            // 1、需要驗證該通知數據中的out_trade_no是否為商戶系統中建立的訂單號，
            // 2、判斷total_amount是否確實為該訂單的實際金額（即商戶訂單建立時的金額），
            JSONObject amount = resource.getJSONObject("amount");
            Double total = Double.valueOf(amount.getString("total"));

            // resource.getString(某個取得Total金額的參數);
            // 3、校驗通知中的seller_id（或者seller_email) 是否為out_trade_no這筆單據的對應的操作方（有的時候，一個商戶可能有多個seller_id/seller_email）
            // 4、驗證app_id是否為該商戶本身。
            // 5、驗證是否已處理過
            // */

            //支付狀態
            if (resource.getString("state").equalsIgnoreCase("completed")) {
                try {
                    System.out.println("訂單支付完成");
                    System.out.println("todo:改變資料庫訂單狀態、募資金額");

                    String extra_common_param = resource.getString("custom");
                    JSONObject extraParam = JSONObject.parseObject(extra_common_param);
                    Integer projectId = Integer.valueOf(extraParam.getString("projectID"));
                    orderService.updateProjectMoney(projectId,total);

                    System.out.println("todo:通知廠商、使其安排物流");
                    response.setStatus(200);
                    return;
                } catch (Exception e) {
                    log.warn("此支付訂單更新失敗，訂單ID=" + resource.getString("invoice_number") + ",paypal訂單唯一識別號：" + resource.getString("id"));
                    //這裡返回500或者其他HTTP錯誤碼，即可重發
                    response.setStatus(500);
                    return;
                }
            }
        }
    }

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
