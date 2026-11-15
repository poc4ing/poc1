package es.kyoe.ing.mortgage.web.infrastructure.rest.mapper;

import es.kyoe.ing.mortgage.MortgageCheck;
import es.kyoe.ing.mortgage.MortgageCheckResult;
import es.kyoe.ing.mortgage.Rate;
import es.kyoe.ing.mortgage.web.infrastructure.rest.model.MortgageCheckRequestDto;
import es.kyoe.ing.mortgage.web.infrastructure.rest.model.MortgageCheckResponseDto;
import es.kyoe.ing.mortgage.web.infrastructure.rest.model.PageRateDto;
import es.kyoe.ing.mortgage.web.infrastructure.rest.model.RateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MortgageRestMapper {

    RateDto toDto(Rate rate);

    List<RateDto> toDtoList(List<Rate> rates);

    MortgageCheck toDomain(MortgageCheckRequestDto dto);

    MortgageCheckResponseDto toDto(MortgageCheckResult result);

    default PageRateDto toPageDto(Page<Rate> page) {
        if (page == null) {
            return null;
        }
        return new PageRateDto()
                .content(toDtoList(page.getContent()))
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .size(page.getSize())
                .number(page.getNumber())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty());
    }

    default String map(OffsetDateTime value) {
        return value != null ? value.toString() : null;
    }

    default BigDecimal map(Double value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }

    default Double map(BigDecimal value) {
        return value != null ? value.doubleValue() : null;
    }
}
