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
import technical.test.api.utils.DataFactory;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlightFacadeTest {

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

        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        FlightRepresentation flightRepresentation = DataFactory.getFlightRepresentation(uuid);

        AirportRecord origin = DataFactory.getFRAirportRecord();
        AirportRecord destination = DataFactory.getUSAirportRecord();

        FlightRecord flightRecord = DataFactory.getFlightRecord(uuid);

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

}