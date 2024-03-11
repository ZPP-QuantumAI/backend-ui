package pl.mimuw.zpp.quantumai.backendui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import pl.mimuw.zpp.quantumai.backendui.finalgrade.FinalGrader;
import pl.mimuw.zpp.quantumai.backendui.model.Problem;
import pl.mimuw.zpp.quantumai.backendui.utils.RandomNameGenerator;
import pl.mimuw.zpp.quantumai.backendui.verifier.Verifier;

import java.time.Clock;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class BackendUiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendUiApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    @Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*"); // Later we can specify allowed origins.
			}
		};
	}

    @Bean
    public RandomNameGenerator randomNameGenerator() {
        return () -> UUID.randomUUID().toString();
    }

    @Bean
    public Map<Problem, Verifier> verifierMap(List<Verifier> verifiers) {
        return verifiers.stream()
                .collect(Collectors.toMap(Verifier::problem, Function.identity()));
    }

    @Bean
    public Map<Problem, FinalGrader> finalGraderMap(List<FinalGrader> graders) {
        return graders.stream()
                .collect(Collectors.toMap(FinalGrader::problem, Function.identity()));
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
