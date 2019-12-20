package webservice;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloClass {

    @RequestMapping("/")
    public String index() {
        return "Bem vindo(a) ao GreenHouse Management!";
    }

}
