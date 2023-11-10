package pl.mimuw.zpp.quantumai.backendui.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "utm-python")
public interface UtmPythonFeignClient {
    @GetMapping("/hello-world") String test();
}
