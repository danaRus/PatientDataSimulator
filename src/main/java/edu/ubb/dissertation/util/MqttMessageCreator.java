package edu.ubb.dissertation.util;

import edu.ubb.dissertation.model.AbnormalVitalSignsEntry;
import edu.ubb.dissertation.model.PatientMeasurement;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public final class MqttMessageCreator {

    private MqttMessageCreator() {
    }

    public static MqttMessage createMqttMessage(final PatientMeasurement measurement, final AbnormalVitalSignsEntry entry) {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("measurementId", measurement.getId());
        jsonObject.accumulate("timestamp", measurement.getTimestamp().toString());
        jsonObject.accumulate("sbp", measurement.getSystolicBloodPressure().getValue());
        jsonObject.accumulate("sbpUL", measurement.getSystolicBloodPressure().getUpperLimit());
        jsonObject.accumulate("sbpLL", measurement.getSystolicBloodPressure().getLowerLimit());
        jsonObject.accumulate("dbp", measurement.getDiastolicBloodPressure().getValue());
        jsonObject.accumulate("dbpUL", measurement.getDiastolicBloodPressure().getUpperLimit());
        jsonObject.accumulate("dbpLL", measurement.getDiastolicBloodPressure().getLowerLimit());
        jsonObject.accumulate("hr", measurement.getHeartRate().getValue());
        jsonObject.accumulate("hrUL", measurement.getHeartRate().getUpperLimit());
        jsonObject.accumulate("hrLL", measurement.getHeartRate().getLowerLimit());
        jsonObject.accumulate("os", measurement.getOxygenSaturationLevel().getValue());
        jsonObject.accumulate("osUL", measurement.getOxygenSaturationLevel().getUpperLimit());
        jsonObject.accumulate("osLL", measurement.getOxygenSaturationLevel().getLowerLimit());
        jsonObject.accumulate("bloodLossRate", measurement.getBloodLossRate());
        jsonObject.accumulate("abnormalVitalSigns", entry.getAbnormalVitalSigns());
        return new MqttMessage(jsonObject.toString().getBytes());
    }
}
