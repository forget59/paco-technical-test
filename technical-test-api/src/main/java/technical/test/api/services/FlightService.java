package technical.test.api.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.record.FlightRecord;
import technical.test.api.repository.FlightRepository;

@Service
@RequiredArgsConstructor
public class FlightService {
    private final FlightRepository flightRepository;

    public Flux<FlightRecord> getAllFlights() {
        return this.flightRepository.findAll();
    }
    public Mono<FlightRecord> createFlight(FlightRecord flightRecord) {
        return this.flightRepository.save(flightRecord);
    }

    public Flux<FlightRecord> getListFlights(Pageable pageable) {
        return this.flightRepository.findAllBy(pageable);
    }

    public Mono<Long> getFlightCount() {
        return this.flightRepository.count();
    }

}
