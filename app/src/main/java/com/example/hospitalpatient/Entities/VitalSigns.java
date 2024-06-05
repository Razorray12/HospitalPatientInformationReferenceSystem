package com.example.hospitalpatient.Entities;

public class VitalSigns {
    private String temperature = null;
    private String heartRate = null;
    private String respiratoryRate = null;
    private String bloodPressure = null;
    private String oxygenSaturation = null;
    private String bloodGlucose = null;

    public VitalSigns(String temperature, String heartRate, String respiratoryRate,
                      String bloodPressure, String oxygenSaturation, String bloodGlucose) {
        this.temperature = temperature;
        this.heartRate = heartRate;
        this.respiratoryRate = respiratoryRate;
        this.bloodPressure = bloodPressure;
        this.oxygenSaturation = oxygenSaturation;
        this.bloodGlucose = bloodGlucose;
    }
    public VitalSigns() {}

    public String getTemperature() {
       return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getRespiratoryRate() {
        return respiratoryRate;
    }

    public void setRespiratoryRate(String respiratoryRate) {
        this.respiratoryRate = respiratoryRate;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getOxygenSaturation() {
        return oxygenSaturation;
    }

    public void setOxygenSaturation(String oxygenSaturation) {
        this.oxygenSaturation = oxygenSaturation;
    }

    public String getBloodGlucose() {
        return bloodGlucose;
    }

    public void setBloodGlucose(String bloodGlucose) {
        this.bloodGlucose = bloodGlucose;
    }
}
