package com.alerts;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlertGeneratorTest {

    private AlertGenerator alertGenerator;
    private Patient patient;
    private ByteArrayOutputStream output;

    @BeforeEach
    void setUp() {
        alertGenerator = new AlertGenerator(null); // DataStorage not needed here
        patient = new Patient(101);

        // Capture System.out for verifying alerts
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
    }

    @Test
    void testHeartRateHighAndLowTriggers() {
        long now = System.currentTimeMillis();

        // High HR
        patient.addRecord(120.0, "HeartRate", now - 1000);
        // Low HR
        patient.addRecord(40.0, "HeartRate", now - 2000);

        alertGenerator.evaluateData(patient);

        String logs = output.toString();
        assertTrue(logs.contains("Tachycardia"));
        assertTrue(logs.contains("Bradycardia"));
    }

    @Test
    void testBloodPressureHighAndLowTriggers() {
        long now = System.currentTimeMillis();

        // Systolic High
        patient.addRecord(185.0, "Systolic", now - 1000);
        // Systolic Low
        patient.addRecord(85.0, "Systolic", now - 2000);
        // Diastolic High
        patient.addRecord(130.0, "Diastolic", now - 3000);
        // Diastolic Low
        patient.addRecord(55.0, "Diastolic", now - 4000);

        alertGenerator.evaluateData(patient);

        String logs = output.toString();
        assertTrue(logs.contains("High Systolic"));
        assertTrue(logs.contains("Low Systolic"));
        assertTrue(logs.contains("High Diastolic"));
        assertTrue(logs.contains("Low Diastolic"));
    }

    @Test
    void testOxygenSaturationAlert() {
        long now = System.currentTimeMillis();
        patient.addRecord(88.0, "OxygenSaturation", now - 1000);

        alertGenerator.evaluateData(patient);

        assertTrue(output.toString().contains("Low Oxygen Saturation"));
    }

    @Test
    void testTriggeredManualAlert() {
        long now = System.currentTimeMillis();
        patient.addRecord(1.0, "TriggeredAlert", now - 1000);

        alertGenerator.evaluateData(patient);

        assertTrue(output.toString().contains("Manual alert triggered"));
    }

    @Test
    void testECGSpikeAlert() {
        long now = System.currentTimeMillis();

        // Create 5 ECG values where last is 2x average
        patient.addRecord(1.0, "ECG", now - 5000);
        patient.addRecord(1.2, "ECG", now - 4000);
        patient.addRecord(1.1, "ECG", now - 3000);
        patient.addRecord(1.3, "ECG", now - 2000);
        patient.addRecord(4.0, "ECG", now - 1000); // spike

        alertGenerator.evaluateData(patient);

        assertTrue(output.toString().contains("Abnormal ECG spike"));
    }

    @Test
    void testNoAlertForNormalHeartRate() {
        long now = System.currentTimeMillis();
        patient.addRecord(75.0, "HeartRate", now - 1000);

        alertGenerator.evaluateData(patient);

        assertFalse(output.toString().contains("ALERT"));
    }

    @Test
    void testNoAlertForInsufficientECGRecords() {
        long now = System.currentTimeMillis();
        patient.addRecord(1.0, "ECG", now - 5000);
        patient.addRecord(1.2, "ECG", now - 4000);

        alertGenerator.evaluateData(patient);

        assertFalse(output.toString().contains("ECG"));
    }

    @Test
    void testCaseInsensitiveRecordTypeHandling() {
        long now = System.currentTimeMillis();
        patient.addRecord(1.0, "sYsToLiC", now - 1000); // mixed case

        alertGenerator.evaluateData(patient);

        assertTrue(output.toString().contains("Systolic"));
    }
}
