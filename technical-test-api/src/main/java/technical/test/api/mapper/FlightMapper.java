package technical.test.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import technical.test.api.record.AirportRecord;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.AirportRepresentation;
import technical.test.api.representation.FlightRepresentation;

import java.util.Collections;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, imports = Collections.class, uses = {AirportMapper.class})
public interface FlightMapper {
    @Mapping(target = "origin", source = "origin", ignore = true)
    @Mapping(target = "destination", source = "destination", ignore = true)
    FlightRepresentation convert(FlightRecord source);

    @Mapping(target = "origin", source = "origin", qualifiedByName = "extractAirportCode")
    @Mapping(target = "destination", source = "destination", qualifiedByName = "extractAirportCode")
    FlightRecord convert(FlightRepresentation source);

    @Mapping(target = "origin", source = "originObject")
    @Mapping(target = "destination", source = "destinationObject")
    FlightRepresentation toDTO(FlightRecord flightRecord, AirportRecord originObject, AirportRecord destinationObject);

    @Named("extractAirportCode")
    default String wrapImageAsList(final AirportRepresentation source) {
        return source.getIata();
    }
}
