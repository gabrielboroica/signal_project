package data_management;

import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DataStorageTest {

    @Test
    public void testSingletonReturnsSameInstance() {
        DataStorage instance1 = DataStorage.getInstance();
        DataStorage instance2 = DataStorage.getInstance();

        assertSame(instance1, instance2); // ambele trebuie să fie exact același obiect
    }

    @Test
    public void testAddAndRetrievePatientData() {
        DataStorage storage = DataStorage.getInstance();
        storage.addPatientData(10, 98.6, "OxygenSaturation", System.currentTimeMillis());

        assertFalse(storage.getAllPatients().isEmpty());
        assertNotNull(storage.getRecords(10, 0, System.currentTimeMillis()));
    }
}
