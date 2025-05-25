package com;

import com.data_management.DataStorage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.logging.Logger;

public class WebSocketPatientClient extends WebSocketClient {

    private final DataStorage storage = DataStorage.getInstance();
    private static final Logger logger = Logger.getLogger(WebSocketPatientClient.class.getName());

    public WebSocketPatientClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        logger.info("✅ Connected to WebSocket server.");
    }

    @Override
    public void onMessage(String message) {
        try {
            // Exemplu format: "patientId|recordType|value|timestamp"
            String[] parts = message.split("\\|");
            if (parts.length != 4) {
                logger.warning("❌ Invalid message format: " + message);
                return;
            }

            int patientId = Integer.parseInt(parts[0]);
            String recordType = parts[1];
            double value = Double.parseDouble(parts[2]);
            long timestamp = Long.parseLong(parts[3]);

            storage.addPatientData(patientId, value, recordType, timestamp);
            logger.info("📥 Stored data: " + message);

        } catch (Exception e) {
            logger.severe("💥 Error parsing message: " + message + " – " + e.getMessage());
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        logger.warning("🔌 Disconnected from WebSocket: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        logger.severe("🔥 WebSocket error: " + ex.getMessage());
    }
}
