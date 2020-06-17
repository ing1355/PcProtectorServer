package oms.pc_protector.Web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController implements ErrorController {
    @GetMapping({"/", "/error"})
    public String main() {
        return "index.html";
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
