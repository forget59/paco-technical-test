package technical.test.api.facade;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import technical.test.api.mapper.FlightMapper;
import technical.test.api.record.AirportRecord;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.AirportRepresentation;
import technical.test.api.representation.FlightRepresentation;
import technical.test.api.services.AirportService;
import technical.test.api.services.FlightService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightFacadeTest {

    @Mock
    FlightMapper flightFacade;
    @Mock
    FlightService flightService;
    @Mock
    AirportService airportService;
    @Mock
    FlightMapper flightMapper;
    @InjectMocks
    FlightFacade flightFacadeTest;

    @Test
    void createFlight_ShouldOrchestrateAndMap() {

        var uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        FlightRepresentation flightRepresentation = getFlightRepresentation(uuid);

        AirportRecord origin = AirportRecord.builder()
                .country("France")
                .iata("CDG")
                .name("Charles de Gaulle")
                .build();

        AirportRecord destination = AirportRecord.builder()
                .country("USA")
                .iata("JFK")
                .name("John F. Kennedy")
                .build();

        FlightRecord flightRecord = FlightRecord.builder()
                .id(uuid)
                .departure(LocalDateTime.parse("2023-12-17T20:15:00"))
                .arrival(LocalDateTime.parse("2023-12-18T06:40:00"))
                .price(125.29)
                .image("image")
                .origin("CDG")
                .destination("JFK")
                .build();

        when(flightMapper.convert(flightRepresentation)).thenReturn(flightRecord);
        when(flightService.createFlight(flightRecord)).thenReturn(Mono.just(flightRecord));
        when(airportService.findByIataCode("CDG")).thenReturn(Mono.just(origin));
        when(airportService.findByIataCode("JFK")).thenReturn(Mono.just(destination));
        when(flightMapper.toDTO(flightRecord, origin, destination)).thenReturn(flightRepresentation);

        reactor.test.StepVerifier.create(flightFacadeTest.createFlight(flightRepresentation))
                .expectNext(flightRepresentation)
                .verifyComplete();

        verify(flightMapper).convert(flightRepresentation);
        verify(flightService).createFlight(flightRecord);
        verify(airportService).findByIataCode("CDG");
        verify(airportService).findByIataCode("JFK");
        verify(flightMapper).toDTO(flightRecord, origin, destination);

    }

    private static @NotNull FlightRepresentation getFlightRepresentation(UUID uuid) {
        AirportRepresentation originRepresentation = new AirportRepresentation();
        originRepresentation.setCountry("France");
        originRepresentation.setIata("CDG");
        originRepresentation.setName("Charles de Gaulle");

        AirportRepresentation destinationRepresentation = new AirportRepresentation();
        destinationRepresentation.setCountry("USA");
        destinationRepresentation.setIata("JFK");
        destinationRepresentation.setName("John F. Kennedy");

        FlightRepresentation representation = new FlightRepresentation();
        representation.setId(uuid);
        representation.setDeparture(LocalDateTime.parse("2023-12-17T20:15:00"));
        representation.setArrival(LocalDateTime.parse("2023-12-18T06:40:00"));
        representation.setPrice(125.29);
        representation.setImage("image");
        representation.setOrigin(originRepresentation);
        representation.setDestination(destinationRepresentation);
        return representation;
    }
}