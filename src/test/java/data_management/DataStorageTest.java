package data_management;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataStorageTest {
    private DataStorage storage;

    @BeforeEach
    public void setUp() {
        storage = new DataStorage();
    }

    @Test
    public void testAddAndGetRecords() {
        // Add test data with current timestamp
        long timestamp = System.currentTimeMillis();
        storage.addPatientData(1, 120.0, "HeartRate", timestamp);

        // Test retrieval - use the exact timestamp we just used
        List<PatientRecord> records = storage.getRecords(1, timestamp-1000, timestamp+1000);

        assertEquals(1, records.size(), "Should find 1 record");
        PatientRecord record = records.get(0);
        assertEquals(1, record.getPatientId());
        assertEquals(120.0, record.getMeasurementValue(), 0.01);
        assertEquals("HeartRate", record.getRecordType());
    }
}