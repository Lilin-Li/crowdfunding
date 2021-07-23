package inc.lilin.crowd.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

//@ControllerLog(description = "測試")
@Controller
public class TestController {


    @RequestMapping("/")
    public String test(Map<String,Object> map) throws Exception{
//        throw new Exception();
        return "index";
    }
}
