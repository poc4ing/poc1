package es.kyoe.ing.mortgage.persistency.h2.mapper;

import es.kyoe.ing.mortgage.Rate;
import es.kyoe.ing.mortgage.persistency.h2.entity.RateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MortgageRateEntityMapper {

    @Mapping(target = "id", source = "idApi")
    Rate toDomain(RateEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "idApi", source = "id")
    RateEntity toEntity(Rate domain);
}
