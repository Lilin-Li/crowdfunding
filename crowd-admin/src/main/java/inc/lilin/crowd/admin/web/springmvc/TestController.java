package inc.lilin.crowd.admin.web.springmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class TestController {

    @RequestMapping("/test")
    public String test(Map<String,Object> map) throws Exception{
       throw new Exception("例外abc");
        // return "exception";
    }
}