package inc.lilin.crowd.authentication.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PortalController {
    @RequestMapping("/")
    public String showPortalPage() {
        return "portal";
    }
}
