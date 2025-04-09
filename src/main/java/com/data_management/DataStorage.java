package com.data_management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alerts.AlertGenerator;

public class DataStorage {
    private Map<Integer, Patient> patientMap;

    public DataStorage() {  // Removed Object reader parameter as it's unused
        this.patientMap = new HashMap<>();
    }

    public void addPatientData(int patientId, double measurementValue,
                               String recordType, long timestamp) {
        Patient patient = patientMap.get(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patientMap.put(patientId, patient);
        }
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    public List<PatientRecord> getRecords(int patientId, long startTime, long endTime) {
        Patient patient = patientMap.get(patientId);
        if (patient != null) {
            return patient.getRecords(startTime, endTime);
        }
        return new ArrayList<>();
    }

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patientMap.values());
    }

    public static void main(String[] args) {
        DataStorage storage = new DataStorage();

        // Add sample data
        storage.addPatientData(1, 120.0, "HeartRate", System.currentTimeMillis());
        storage.addPatientData(2, 80.0, "BloodPressure", System.currentTimeMillis() - 10000);

        // Initialize AlertGenerator
        AlertGenerator alertGenerator = new AlertGenerator(storage);

        // Evaluate all patients
        for (Patient patient : storage.getAllPatients()) {
            alertGenerator.evaluateData(patient);
        }
    }
}