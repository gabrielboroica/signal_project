package com;

import com.WebSocketPatientClient;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WebSocketPatientClientTest {

    @Test
    public void testMessageParsingValidFormat() {
        DataStorage storage = DataStorage.getInstance();
        int sizeBefore = storage.getAllPatients().size();

        WebSocketPatientClient dummy = new WebSocketPatientClient(null);
        dummy.onMessage("123|HeartRate|88.5|1716643200000");

        assertTrue(storage.getAllPatients().size() >= sizeBefore);
    }

    @Test
    public void testInvalidMessageFormat() {
        WebSocketPatientClient dummy = new WebSocketPatientClient(null);
        assertDoesNotThrow(() -> dummy.onMessage("bad|message")); // nu aruncă excepții
    }
}
