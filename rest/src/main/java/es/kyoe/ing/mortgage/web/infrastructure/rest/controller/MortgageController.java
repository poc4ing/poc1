package es.kyoe.ing.mortgage.web.infrastructure.rest.controller;

import es.kyoe.ing.mortgage.MortgageCheck;
import es.kyoe.ing.mortgage.MortgageCheckResult;
import es.kyoe.ing.mortgage.Rate;
import es.kyoe.ing.mortgage.usecase.CheckMortgageUseCase;
import es.kyoe.ing.mortgage.usecase.GetRatesUseCase;
import es.kyoe.ing.mortgage.web.infrastructure.rest.api.MortgageCheckApi;
import es.kyoe.ing.mortgage.web.infrastructure.rest.api.RatesApi;
import es.kyoe.ing.mortgage.web.infrastructure.rest.mapper.MortgageRestMapper;
import es.kyoe.ing.mortgage.web.infrastructure.rest.model.MortgageCheckRequestDto;
import es.kyoe.ing.mortgage.web.infrastructure.rest.model.MortgageCheckResponseDto;
import es.kyoe.ing.mortgage.web.infrastructure.rest.model.PageRateDto;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class MortgageController implements RatesApi, MortgageCheckApi {

    private static final Logger log = LoggerFactory.getLogger(MortgageController.class);

    private final GetRatesUseCase getRatesUseCase;
    private final CheckMortgageUseCase checkMortgageUseCase;
    private final MortgageRestMapper mapper;

    public MortgageController(
            GetRatesUseCase getRatesUseCase,
            CheckMortgageUseCase checkMortgageUseCase,
            MortgageRestMapper mapper
    ) {
        this.getRatesUseCase = getRatesUseCase;
        this.checkMortgageUseCase = checkMortgageUseCase;
        this.mapper = mapper;
    }

    @Override
    @GetMapping(
            value = "/v1/rates",
            produces = {"application/json"},
            version = "v1"
    )
    public ResponseEntity<PageRateDto> getRates(Pageable pageable) {
        log.debug("GET /v1/rates pageable={}", pageable);
        Page<Rate> rates = getRatesUseCase.getRates(pageable);
        return ResponseEntity.ok(mapper.toPageDto(rates));
    }

    @Override
    @PostMapping(
            value = "/v1/mortgage-check",
            produces = {"application/json", "application/problem+json"},
            consumes = {"application/json"},
            version = "v1"
    )
    public ResponseEntity<MortgageCheckResponseDto> checkMortgage(
            @Valid MortgageCheckRequestDto request
    ) {
        log.debug("POST /v1/mortgage-check maturityPeriod={}", request.getMaturityPeriod());
        MortgageCheck check = mapper.toDomain(request);
        MortgageCheckResult result = checkMortgageUseCase.checkMortgage(check);
        return ResponseEntity.ok(mapper.toDto(result));
    }
}
