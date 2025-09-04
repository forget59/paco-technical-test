package technical.test.api.utils;

import org.jetbrains.annotations.NotNull;
import technical.test.api.record.AirportRecord;
import technical.test.api.record.FlightRecord;
import technical.test.api.representation.AirportRepresentation;
import technical.test.api.representation.FlightRepresentation;

import java.time.LocalDateTime;
import java.util.UUID;

public class DataFactory {

    public static @NotNull FlightRepresentation getFlightRepresentation(UUID uuid) {
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

    public static FlightRecord getFlightRecord(UUID uuid) {
        return FlightRecord.builder()
                .id(uuid)
                .departure(LocalDateTime.parse("2023-12-17T20:15:00"))
                .arrival(LocalDateTime.parse("2023-12-18T06:40:00"))
                .price(125.29)
                .image("image")
                .origin("CDG")
                .destination("JFK")
                .build();
    }

    public static AirportRecord getUSAirportRecord() {
        return AirportRecord.builder()
                .country("USA")
                .iata("JFK")
                .name("John F. Kennedy")
                .build();
    }

    public static AirportRecord getFRAirportRecord() {
        return AirportRecord.builder()
                .country("France")
                .iata("CDG")
                .name("Charles de Gaulle")
                .build();
    }
}
