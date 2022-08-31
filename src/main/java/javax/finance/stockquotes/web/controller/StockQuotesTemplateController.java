package javax.finance.stockquotes.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class StockQuotesTemplateController {

    @GetMapping
    public String index() {
        return "/index.html";
    }

}
