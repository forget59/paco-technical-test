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
import technical.test.api.utils.DataFactory;

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

        AirportRecord origin = DataFactory.getFRAirportRecord();

        AirportRecord destination = DataFactory.getUSAirportRecord();

        FlightRecord flightRecord = DataFactory.getFlightRecord(uuid);

        FlightRepresentation representation = DataFactory.getFlightRepresentation(uuid);

        FlightRepresentation res = flightMapper.toDTO(flightRecord, origin, destination);

        Assertions.assertEquals(representation, res);
    }
}