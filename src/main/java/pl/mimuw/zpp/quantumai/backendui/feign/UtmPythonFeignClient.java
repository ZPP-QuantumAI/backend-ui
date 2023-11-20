package pl.mimuw.zpp.quantumai.backendui.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "utm-python")
public interface UtmPythonFeignClient {
    @GetMapping("/hello-world") String test();

    @PostMapping("/solve-string")
    ResponseEntity<String> solve(
            @RequestParam String programText,
            @RequestParam String inputText
    );
}
