package pl.mimuw.zpp.quantumai.backendui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BackendUiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendUiApplication.class, args);
    }

}
