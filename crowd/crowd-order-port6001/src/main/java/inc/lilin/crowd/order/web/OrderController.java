package inc.lilin.crowd.order.web;

import inc.lilin.crowd.common.core.constant.CrowdConstant;
import inc.lilin.crowd.entity.vo.AddressVO;
import inc.lilin.crowd.entity.vo.MemberLoginVO;
import inc.lilin.crowd.entity.vo.OrderProjectVO;
import inc.lilin.crowd.order.core.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

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
        OrderProjectVO orderProjectVO = (OrderProjectVO)session.getAttribute("orderProjectVO");
        Integer returnCount = orderProjectVO.getReturnCount();
        return "redirect:" + CrowdConstant.GATEWAY_URL + "/order/confirm/order/" + returnCount;
    }
}
