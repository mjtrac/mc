package sample.camel.ui;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestRedirectController {

    @PostMapping("/submit")
    public String postSubmit() {
        return "redirect:/test/result";
    }

    @GetMapping("/result")
    public String getResult() {
        return "result";  // result.html in templates
    }
}
