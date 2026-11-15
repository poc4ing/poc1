package es.kyoe.ing.mortgage.web.infrastructure.rest.controller;

import es.kyoe.ing.mortgage.exception.MaturityPeriodNotFoundException;
import es.kyoe.ing.mortgage.exception.MortgageException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Validation failed");
        pd.setDetail("Request body validation failed");
        List<Map<String, Object>> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("field", err.getField());
                    map.put("message", err.getDefaultMessage() != null ? err.getDefaultMessage() : "invalid");
                    map.put("rejectedValue", err.getRejectedValue());
                    return map;
                })
                .toList();
        pd.setProperty("errors", errors);
        stamp(pd);
        pd.setProperty("code", "2001");
        return pd;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Constraint violation");
        pd.setDetail(ex.getMessage());
        List<Map<String, Object>> violations = ex.getConstraintViolations().stream()
                .map(v -> {
                    Map<String, Object> map = new LinkedHashMap<>();
                    map.put("property", v.getPropertyPath().toString());
                    map.put("message", v.getMessage());
                    map.put("invalidValue", v.getInvalidValue());
                    return map;
                })
                .toList();
        pd.setProperty("violations", violations);
        stamp(pd);
        pd.setProperty("code", "2002");
        return pd;
    }

    @ExceptionHandler(MaturityPeriodNotFoundException.class)
    public ProblemDetail handleMaturityPeriodNotFound(MaturityPeriodNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        pd.setTitle("Invalid maturity period");
        pd.setDetail(ex.getMessage());
        stamp(pd);
        pd.setProperty("code", MaturityPeriodNotFoundException.CODE);
        return pd;
    }

    @ExceptionHandler(MortgageException.class)
    public ProblemDetail handleMortgageException(MortgageException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Mortgage error");
        pd.setDetail(ex.getMessage());
        stamp(pd);
        pd.setProperty("code", MortgageException.CODE);
        return pd;
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ProblemDetail handleResponseStatus(ResponseStatusException ex) {
        ProblemDetail pd = ProblemDetail.forStatus(ex.getStatusCode());
        pd.setTitle(ex.getReason() != null ? ex.getReason() : String.valueOf(ex.getStatusCode().value()));
        pd.setDetail(ex.getMessage());
        stamp(pd);
        pd.setProperty("code", MortgageException.CODE);
        return pd;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex) {
        log.error("Exception {}", ex.getMessage(), ex);

        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Unexpected error");
        pd.setDetail("An unexpected error occurred");
        stamp(pd);
        pd.setProperty("code", MortgageException.CODE);
        return pd;
    }

    private void stamp(ProblemDetail pd) {
        pd.setProperty("timestamp", OffsetDateTime.now().toString());
    }
}

