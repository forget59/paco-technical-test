package technical.test.api.mapper;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import technical.test.api.record.AirportRecord;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.AirportRepresentation;
import technical.test.api.representation.FlightRepresentation;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ExtendWith({SpringExtension.class})
@Import({FlightMapperImpl.class, AirportMapperImpl.class})
class FlightMapperTest {

    @Autowired
    private FlightMapper flightMapper;

    @Test
    void toDTO_shouldMapAllField() {

        UUID uuid = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

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

        FlightRepresentation representation = getFlightRepresentation(uuid);

        var res = flightMapper.toDTO(flightRecord, origin, destination);

        Assertions.assertEquals(representation, res);
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