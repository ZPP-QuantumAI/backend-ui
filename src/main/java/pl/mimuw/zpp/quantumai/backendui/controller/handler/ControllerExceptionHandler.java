package pl.mimuw.zpp.quantumai.backendui.controller.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.mimuw.zpp.quantumai.backendui.error.PackageNotFoundException;
import pl.mimuw.zpp.quantumai.backendui.error.SolutionNotFoundException;

@RestControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler({PackageNotFoundException.class})
    public ResponseEntity<Object> handlePackageNotFoundException(PackageNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({SolutionNotFoundException.class})
    public ResponseEntity<Object> handleSolutionNotFoundException(SolutionNotFoundException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception e) {
        log.error("Exception occurred", e);
        return ResponseEntity.internalServerError().build();
    }
}
