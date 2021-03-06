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
        // 1.???????????????????????????????????? Session ???
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");

        // TODO: session????????????????????????????????????
        // if(orderProjectVO == null)

        orderProjectVO.setReturnCount(returnCount);
        session.setAttribute("orderProjectVO", orderProjectVO);

        // 2.????????????????????????????????? id
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();

        // 3.?????????????????????????????????
        List<AddressVO> list = orderService.getAddressVO(memberId);
        session.setAttribute("addressVOList", list);

        return "confirm_order";
    }

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession session) {
        orderService.saveAddress(addressVO);
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");
        // TODO: session????????????????????????????????????
        Integer returnCount = orderProjectVO.getReturnCount();
        return "redirect:" + CrowdConstant.GATEWAY_URL + "/order/confirm/order/" + returnCount;
    }

    @ResponseBody
    @RequestMapping("/generate/order")
    public String generateOrder(HttpSession session, OrderVO orderVO, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1.??? Session ????????? orderProjectVO ??????
        OrderProjectVO orderProjectVO = (OrderProjectVO) session.getAttribute("orderProjectVO");

        // TODO: session????????????????????????????????????

        // 2.??? orderProjectVO ????????? orderVO ?????????????????????
        orderVO.setOrderProjectVO(orderProjectVO);

        // 3.??????????????????????????? orderVO ?????????
        // ???????????????????????????????????????
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        // ????????? UUID ??????????????? ID ??????
        String user = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        // ?????????
        String orderNum = time + user;
        // ???????????? OrderVO ?????????
        orderVO.setOrderNum(orderNum);


        // 4.????????????????????????????????? orderVO ?????????
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
                    // ????????????
                    response.sendRedirect(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "??????????????????";
    }

    @ResponseBody
    @RequestMapping("/paypal/success")
    public String returnUrlMethod(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId, HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {


        Payment payment = paypalService.executePayment(paymentId, payerId);
        if (payment.getState().equals("approved")) {
            // ????????????
            String status = "";
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    status = links.getRel();
                    break;
                }
                status = links.getRel();
            }
            // ???????????????
            String out_trade_no = payment.getTransactions().get(0).getInvoiceNumber();
            // paypal?????????
            String trade_no = payment.getId();
            // ?????????
            Double orderAmount = Double.valueOf(
                    payment.getTransactions()
                            .get(0)
                            .getAmount()
                            .getTotal());

            // ?????????DB
            OrderVO orderVO = (OrderVO) session.getAttribute("orderVO");
            // TODO: session????????????????????????????????????
            orderVO.setPayOrderNum(trade_no);
            orderService.saveOrder(orderVO);

            return "trade_no:" + trade_no + "<br/>out_trade_no:" + out_trade_no + "<br/>total_amount:" + orderAmount + "<br/><a href=\"" + CrowdConstant.GATEWAY_URL + "\">????????????";

        } else {
            return "???????????????";
        }
    }

    /**
     * ????????????
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/paypal/cancel")
    public String cancelPay() {
        return "????????????";
    }

    @PostMapping("/notify")
    public void notifyUrlMethod(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String body = getBody(request);
        JSONObject json = JSONObject.parseObject(body);
        log.info("????????????>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + json.toJSONString());

        //??????
        boolean isValid = paypalService.webhookValidate(body, request);

        if (isValid) {
            log.info("webhook?????????????????????event_type=" + json.getString("event_type"));

            JSONObject resource = json.getJSONObject("resource");
            // ?????????????????????????????????????????????????????????
            resource.getString("id");   //paypal?????????????????????
            // 1????????????????????????????????????out_trade_no?????????????????????????????????????????????
            // 2?????????total_amount?????????????????????????????????????????????????????????????????????????????????
            JSONObject amount = resource.getJSONObject("amount");
            Double total = Double.valueOf(amount.getString("total"));

            // resource.getString(????????????Total???????????????);
            // 3?????????????????????seller_id?????????seller_email) ?????????out_trade_no??????????????????????????????????????????????????????????????????????????????seller_id/seller_email???
            // 4?????????app_id???????????????????????????
            // 5???????????????????????????
            // */

            //????????????
            if (resource.getString("state").equalsIgnoreCase("completed")) {
                try {
                    System.out.println("??????????????????");
                    System.out.println("todo:??????????????????????????????????????????");

                    String extra_common_param = resource.getString("custom");
                    JSONObject extraParam = JSONObject.parseObject(extra_common_param);
                    Integer projectId = Integer.valueOf(extraParam.getString("projectID"));
                    orderService.updateProjectMoney(projectId,total);

                    System.out.println("todo:?????????????????????????????????");
                    response.setStatus(200);
                    return;
                } catch (Exception e) {
                    log.warn("????????????????????????????????????ID=" + resource.getString("invoice_number") + ",paypal????????????????????????" + resource.getString("id"));
                    //????????????500????????????HTTP????????????????????????
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
