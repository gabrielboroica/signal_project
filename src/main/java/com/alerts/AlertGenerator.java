package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when predefined health conditions are met.
 */
public class AlertGenerator {

    private final DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator}.
     *
     * @param dataStorage the data storage system used to retrieve patient data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met.
     *
     * @param patient the patient whose data should be evaluated
     */


    /**
     * Checks for blood pressure alerts including trends and critical thresholds.
     *
     * @param patient        the patient being evaluated
     * @param recentRecords  list of recent patient records
     */
    private void evaluateBloodPressureAlerts(Patient patient, List<PatientRecord> recentRecords) {
        List<PatientRecord> systolic = filterByType(recentRecords, "Systolic");
        List<PatientRecord> diastolic = filterByType(recentRecords, "Diastolic");

        checkBPTrends(patient.getPatientId(), systolic, "Systolic");
        checkBPTrends(patient.getPatientId(), diastolic, "Diastolic");
        checkCriticalThresholds(patient.getPatientId(), systolic, diastolic);
    }

    /**
     * Filters a list of records by record type.
     *
     * @param records   list of all patient records
     * @param type      the type of record to filter by
     * @return filtered list of records matching the type
     */
    private List<PatientRecord> filterByType(List<PatientRecord> records, String type) {
        return records.stream()
                .filter(r -> r.getRecordType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    /**
     * Checks for increasing or decreasing trends in blood pressure.
     *
     * @param patientId the ID of the patient
     * @param records   the list of blood pressure records (systolic or diastolic)
     * @param type      the type of blood pressure being evaluated
     */
    private void checkBPTrends(int patientId, List<PatientRecord> records, String type) {
        if (records.size() < 3) return;

        for (int i = 0; i < records.size() - 2; i++) {
            double v1 = records.get(i).getMeasurementValue();
            double v2 = records.get(i + 1).getMeasurementValue();
            double v3 = records.get(i + 2).getMeasurementValue();

            if ((v2 - v1 > 10) && (v3 - v2 > 10)) {
                triggerAlert(new Alert(patientId, "Increasing " + type + " trend", records.get(i + 2).getTimestamp()));
            } else if ((v1 - v2 > 10) && (v2 - v3 > 10)) {
                triggerAlert(new Alert(patientId, "Decreasing " + type + " trend", records.get(i + 2).getTimestamp()));
            }
        }
    }

    /**
     * Checks for critical systolic and diastolic blood pressure thresholds.
     *
     * @param patientId the ID of the patient
     * @param systolic  list of systolic pressure records
     * @param diastolic list of diastolic pressure records
     */
    private void checkCriticalThresholds(int patientId, List<PatientRecord> systolic, List<PatientRecord> diastolic) {
        for (PatientRecord s : systolic) {
            double val = s.getMeasurementValue();
            if (val > 180 || val < 90) {
                triggerAlert(new Alert(patientId, "Critical systolic BP: " + val, s.getTimestamp()));
            }
        }

        for (PatientRecord d : diastolic) {
            double val = d.getMeasurementValue();
            if (val > 120 || val < 60) {
                triggerAlert(new Alert(patientId, "Critical diastolic BP: " + val, d.getTimestamp()));
            }
        }
    }

    /**
     * Evaluates blood oxygen saturation for low values and rapid drops.
     *
     * @param patient        the patient being evaluated
     * @param recentRecords  list of recent patient records
     */
    private void evaluateBloodSaturationAlerts(Patient patient, List<PatientRecord> recentRecords) {
        List<PatientRecord> oxygen = filterByType(recentRecords, "OxygenSaturation");

        for (int i = 0; i < oxygen.size(); i++) {
            double val = oxygen.get(i).getMeasurementValue();
            long ts = oxygen.get(i).getTimestamp();

            if (val < 92.0) {
                triggerAlert(new Alert(patient.getPatientId(), "Low oxygen saturation: " + val + "%", ts));
            }

            if (i >= 1) {
                double prevVal = oxygen.get(i - 1).getMeasurementValue();
                long prevTs = oxygen.get(i - 1).getTimestamp();
                if ((prevVal - val >= 5.0) && (ts - prevTs <= 600_000)) {
                    triggerAlert(new Alert(patient.getPatientId(), "Rapid O₂ drop: from " + prevVal + "% to " + val + "%", ts));
                }
            }
        }
    }

    /**
     * Evaluates combined blood pressure and oxygen saturation for a hypotensive hypoxemia alert.
     *
     * @param patient        the patient being evaluated
     * @param recentRecords  list of recent patient records
     */
    private void evaluateHypotensiveHypoxemiaAlert(Patient patient, List<PatientRecord> recentRecords) {
        List<PatientRecord> systolic = filterByType(recentRecords, "Systolic");
        List<PatientRecord> oxygen = filterByType(recentRecords, "OxygenSaturation");

        for (PatientRecord bp : systolic) {
            if (bp.getMeasurementValue() < 90) {
                for (PatientRecord o2 : oxygen) {
                    if (Math.abs(bp.getTimestamp() - o2.getTimestamp()) <= 60_000 && o2.getMeasurementValue() < 92) {
                        triggerAlert(new Alert(patient.getPatientId(), "Hypotensive Hypoxemia Alert", o2.getTimestamp()));
                    }
                }
            }
        }
    }

    /**
     * Evaluates ECG readings for abnormal values.
     *
     * @param patient        the patient being evaluated
     * @param recentRecords  list of recent patient records
     */
    private void evaluateECGAlerts(Patient patient, List<PatientRecord> recentRecords) {
        List<PatientRecord> ecg = filterByType(recentRecords, "ECG");
        if (ecg.size() < 5) return;

        double avg = ecg.stream()
                .mapToDouble(PatientRecord::getMeasurementValue)
                .average().orElse(0.0);

        for (PatientRecord r : ecg) {
            if (r.getMeasurementValue() > avg * 1.5) {
                triggerAlert(new Alert(patient.getPatientId(), "Abnormal ECG peak", r.getTimestamp()));
            }
        }
    }

    /**
     * Evaluates whether any manual alerts were triggered.
     *
     * @param patient        the patient being evaluated
     * @param recentRecords  list of recent patient records
     */
    private void evaluateTriggeredAlerts(Patient patient, List<PatientRecord> recentRecords) {
        List<PatientRecord> triggered = filterByType(recentRecords, "TriggeredAlert");
        for (PatientRecord r : triggered) {
            if (r.getMeasurementValue() == 1.0) {
                triggerAlert(new Alert(patient.getPatientId(), "Manual alert triggered", r.getTimestamp()));
            }
        }
    }

    /**
     * Triggers a system alert.
     *
     * @param alert the alert to be triggered
     */
    private void triggerAlert(Alert alert) {
        System.out.println("ALERT: Patient " + alert.getPatientId() + " - " + alert.getMessage() + " @ " + alert.getTimestamp());
    }


    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met and triggers alerts accordingly.
     *
     * @param patient the patient whose data should be evaluated
     */
    public void evaluateData(Patient patient) {
        long now = System.currentTimeMillis();
        long oneHourAgo = now - 60 * 60 * 1000;

        List<PatientRecord> records = patient.getRecords(oneHourAgo, now);

        for (PatientRecord record : records) {
            String type = record.getRecordType();
            double value = record.getMeasurementValue();
            long timestamp = record.getTimestamp();
            int patientId = patient.getPatientId();

            switch (type) {
                case "HeartRate":
                    if (value > 100) {
                        triggerAlert(new Alert(patientId, "Tachycardia: Heart rate too high (" + value + " bpm)", timestamp));
                    } else if (value < 50) {
                        triggerAlert(new Alert(patientId, "Bradycardia: Heart rate too low (" + value + " bpm)", timestamp));
                    }
                    break;

                case "Systolic":
                    if (value > 180) {
                        triggerAlert(new Alert(patientId, "High Systolic BP: " + value + " mmHg", timestamp));
                    } else if (value < 90) {
                        triggerAlert(new Alert(patientId, "Low Systolic BP: " + value + " mmHg", timestamp));
                    }
                    break;

                case "Diastolic":
                    if (value > 120) {
                        triggerAlert(new Alert(patientId, "High Diastolic BP: " + value + " mmHg", timestamp));
                    } else if (value < 60) {
                        triggerAlert(new Alert(patientId, "Low Diastolic BP: " + value + " mmHg", timestamp));
                    }
                    break;

                case "OxygenSaturation":
                    if (value < 92.0) {
                        triggerAlert(new Alert(patientId, "Low Oxygen Saturation: " + value + "%", timestamp));
                    }
                    break;

                case "TriggeredAlert":
                    if (value == 1.0) {
                        triggerAlert(new Alert(patientId, "Manual alert triggered", timestamp));
                    }
                    break;
            }
        }

        // Optional: additional analysis like ECG spikes
        evaluateECGSpikes(patient, records);
    }

    /**
     * Evaluates ECG records to detect abnormal spikes based on average value.
     *
     * @param patient the patient whose ECG is being analyzed
     * @param records all recent patient records
     */
    private void evaluateECGSpikes(Patient patient, List<PatientRecord> records) {
        List<PatientRecord> ecg = records.stream()
                .filter(r -> r.getRecordType().equalsIgnoreCase("ECG"))
                .toList();

        if (ecg.size() < 5) return;

        double average = ecg.stream().mapToDouble(PatientRecord::getMeasurementValue).average().orElse(0.0);

        for (PatientRecord record : ecg) {
            if (record.getMeasurementValue() > 1.5 * average) {
                triggerAlert(new Alert(
                        patient.getPatientId(),
                        "Abnormal ECG spike: " + record.getMeasurementValue(),
                        record.getTimestamp()
                ));
            }
        }
    }

}
