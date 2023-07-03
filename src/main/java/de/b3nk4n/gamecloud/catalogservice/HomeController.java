package de.b3nk4n.gamecloud.catalogservice;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String get() {
        return "Hello world";
    }
}
