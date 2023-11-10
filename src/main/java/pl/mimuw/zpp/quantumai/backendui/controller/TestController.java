package pl.mimuw.zpp.quantumai.backendui.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.mimuw.zpp.quantumai.backendui.feign.UtmPythonFeignClient;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final UtmPythonFeignClient utmPythonFeignClient;

    @GetMapping("/hello-world")
    public String test() {
        return utmPythonFeignClient.test();
    }
}
