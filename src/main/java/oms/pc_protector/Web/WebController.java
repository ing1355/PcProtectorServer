package oms.pc_protector.Web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController{
    @GetMapping({"/"})
    public String main() {
        return "index.html";
    }
}
