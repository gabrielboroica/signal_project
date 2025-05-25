package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

/**
 * A DataReader that reads patient data from a structured output file.
 * Expected format per line: patientId,measurementValue,recordType,timestamp
 */
public class FileDataReader implements DataReader {
    private final Path filePath;

    public FileDataReader(Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public void readData(DataStorage  dataStorage) throws  IOException {
         try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;


                String[] parts = line.split(",");
                if (parts.length != 4) {
                    System.err.println("Invalid format at line " + lineNumber + ": " + line);
                    continue;
                }

                 try {
                     int patientId = Integer.parseInt(parts[0].trim());
                     double measurementValue = Double.parseDouble(parts[1].trim());
                       String recordType = parts[2].trim();
                    long timestamp = Long.parseLong(parts[3].trim());

                    dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                }
                 catch (NumberFormatException e) {
                    System.err.println("Parsing error at line " + lineNumber + ": " + e.getMessage());
                }
            }
        }



    }
   }
