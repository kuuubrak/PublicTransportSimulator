package mockup;

import java.util.UUID;

/**
 * Created by ppeczek on 2014-05-28.
 */
public class MockupPassenger {
    private final UUID ID;
    private Long TIMESTAMP;
    private MockupBusStop destination;

    public MockupPassenger(UUID id, Long timestamp, MockupBusStop destination) {
        ID = id;
        TIMESTAMP = timestamp;
        this.destination = destination;
    }

    public UUID getID() {
        return ID;
    }

    public Long getTIMESTAMP() {
        return TIMESTAMP;
    }

    public MockupBusStop getDestination() {
        return destination;
    }
}
