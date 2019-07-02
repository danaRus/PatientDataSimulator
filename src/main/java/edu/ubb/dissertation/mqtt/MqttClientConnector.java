package edu.ubb.dissertation.mqtt;

import edu.ubb.dissertation.exception.ConnectionException;
import io.vavr.CheckedRunnable;
import io.vavr.control.Try;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Consumer;

import static edu.ubb.dissertation.util.Retrier.retry;

/**
 * Encapsulates the logic needed for connecting/disconnecting to MQTT and publishing to a topic.
 */
public class MqttClientConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(MqttClientConnector.class);

    private static final String CLIENT_ID = "SIMULATOR_CLIENT_CONNECTION_ID";
    private static final String TOPIC_NAME = "dissertation/patient_data";
    private static final String SERVER_ADDRESS = "tcp://iot.eclipse.org:1883";

    private MqttClient client;

    public MqttClientConnector() {
        client = retry(this::initializeMqttClient, ConnectionException.class);
    }

    public void connect() {
        execute(() -> client.connect(createConnectionOptions()),
                v -> LOGGER.info("Successfully connected to MQTT server"),
                e -> LOGGER.error("Failed to connect to MQTT server. Message: {}", e.getMessage()));
    }

    public void publish(final MqttMessage message) {
        Try.run(() -> client.publish(TOPIC_NAME, message))
                .onFailure(e -> LOGGER.error("Failed to publish to MQTT topic. Message: {}", e.getMessage()));
    }

    public void disconnect() {
        Optional.ofNullable(client)
                .filter(MqttClient::isConnected)
                .ifPresent(v -> disconnectClient());
    }

    private MqttClient initializeMqttClient() {
        return Try.of(() -> new MqttClient(SERVER_ADDRESS, CLIENT_ID))
                .onFailure(e -> LOGGER.error("Failed to initialize the MQTT client. Message: {}", e.getMessage()))
                .getOrElseThrow(ConnectionException::create);
    }

    /*
     * Create the options needed for connecting to MQTT. The options were chosen so that the connection is reliable
     * (even when the application is restarted).
     */
    private MqttConnectOptions createConnectionOptions() {
        final MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(false);
        options.setKeepAliveInterval(120);
        return options;
    }

    private void disconnectClient() {
        execute(() -> client.disconnect(),
                v -> LOGGER.info("Successfully disconnected from MQTT server"),
                e -> LOGGER.error("Failed to disconnect from MQTT topic. Message: {}", e.getMessage()));
    }

    private void execute(final CheckedRunnable runnable, final Consumer<? super Void> successAction,
                         final Consumer<? super Throwable> failureAction) {
        Try.run(runnable)
                .onSuccess(successAction)
                .onFailure(failureAction)
                .getOrElseThrow(ConnectionException::create);
    }
}
