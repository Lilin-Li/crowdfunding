package inc.lilin.crowd.admin.restfulapi.web.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class TestController {

    @RequestMapping("/")
    public String test(Map<String,Object> map) throws Exception{
//        throw new Exception();
        return "index";
    }
}