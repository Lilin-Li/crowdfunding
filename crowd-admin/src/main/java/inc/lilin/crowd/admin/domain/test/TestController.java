package inc.lilin.crowd.admin.domain.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class TestController {

    @RequestMapping("/test")
    public String test(Map<String,Object> map) throws Exception{
        return "index";
    }
}
