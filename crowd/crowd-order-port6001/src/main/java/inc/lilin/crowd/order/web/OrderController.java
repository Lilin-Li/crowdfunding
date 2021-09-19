package inc.lilin.crowd.order.web;

import inc.lilin.crowd.entity.vo.OrderProjectVO;
import inc.lilin.crowd.order.core.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;

    @ResponseBody
    @RequestMapping("/aa")
    public String test() {
        System.out.println(113);
        return "test";
    }

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
}
