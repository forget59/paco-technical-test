package technical.test.api.endpoints;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import technical.test.api.facade.FlightFacade;
import technical.test.api.representation.FlightRepresentation;

import java.util.Optional;

@RestController
@RequestMapping("/flight")
@RequiredArgsConstructor
public class FlightEndpoint {
    private final FlightFacade flightFacade;

    @GetMapping
    public Flux<FlightRepresentation> getAllFlights() {
        return flightFacade.getAllFlights();
    }
    @PostMapping
    public Mono<ResponseEntity<FlightRepresentation>> createFlight(@RequestBody Mono<FlightRepresentation> flightRepresentation) {
        return flightRepresentation.flatMap(flightFacade::createFlight)
                .map(saved -> ResponseEntity.status(201).body(saved));
    }

    @GetMapping("/pageable")
    public Mono<Page<FlightRepresentation>> getFlights(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "6") int size,
                                                       @RequestParam(defaultValue = "id") String sort,
                                                       @RequestParam(defaultValue = "ASC") String direction) {
        var dir = Sort.Direction.valueOf(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        return flightFacade.getFlights(pageable);
    }
}
