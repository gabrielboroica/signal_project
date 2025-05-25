package com.data_management;

import com.WebSocketPatientClient;

import java.io.IOException;
import java.net.URI;

public class RealTimeDataReader implements DataReader {

    private WebSocketPatientClient client;

    @Override
    public void connect() {
        try {
            URI uri = new URI("ws://localhost:8080"); // sau portul ales
            client = new WebSocketPatientClient(uri);
            client.connect();
        } catch (Exception e) {
            System.err.println("❌ Cannot connect to WebSocket server: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        if (client != null && client.isOpen()) {
            client.close();
        }
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {

    }
}
