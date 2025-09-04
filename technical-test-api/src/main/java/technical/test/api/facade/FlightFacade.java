package technical.test.api.facade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.mapper.AirportMapper;
import technical.test.api.mapper.FlightMapper;
import technical.test.api.record.AirportRecord;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.FlightRepresentation;
import technical.test.api.services.AirportService;
import technical.test.api.services.FlightService;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class FlightFacade {
    private final FlightService flightService;
    private final AirportService airportService;
    private final FlightMapper flightMapper;
    private final AirportMapper airportMapper;

    public Flux<FlightRepresentation> getAllFlights() {
        return flightService.getAllFlights()
                .flatMap(flightRecord -> airportService.findByIataCode(flightRecord.getOrigin())
                        .zipWith(airportService.findByIataCode(flightRecord.getDestination()))
                        .flatMap(tuple -> {
                            AirportRecord origin = tuple.getT1();
                            AirportRecord destination = tuple.getT2();
                            FlightRepresentation flightRepresentation = this.flightMapper.convert(flightRecord);
                            flightRepresentation.setOrigin(this.airportMapper.convert(origin));
                            flightRepresentation.setDestination(this.airportMapper.convert(destination));
                            return Mono.just(flightRepresentation);
                        }));
    }

    public Mono<FlightRepresentation> createFlight(FlightRepresentation flightRepresentation) {
        FlightRecord flightRecord = this.flightMapper.convert(flightRepresentation);
        flightRecord.setId(UUID.randomUUID());
        return flightService.createFlight(flightRecord)
                .flatMap(savedFlight ->
                        Mono.zip(
                                Mono.just(savedFlight),
                                airportService.findByIataCode(savedFlight.getOrigin()),
                                airportService.findByIataCode(savedFlight.getDestination())
                        ).map(t -> flightMapper.toDTO(t.getT1(), t.getT2(), t.getT3())));
    }
}
