package technical.test.api.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import technical.test.api.record.FlightRecord;
import technical.test.api.repository.FlightRepository;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;


    @Test
    void saveFlight_ShouldSaveAndReturnFlightRecord() {

        FlightRecord flightRecord = FlightRecord.builder()
                .price(1234)
                .build();

        when(flightRepository.save(flightRecord)).thenReturn(Mono.just(flightRecord));

        Mono<FlightRecord> result = flightService.createFlight(flightRecord);

        StepVerifier
                .create(result)
                .expectNextMatches(saved -> saved.getPrice() == 1234)
                .verifyComplete();

        verify(flightRepository, times(1)).save(flightRecord);
    }
}