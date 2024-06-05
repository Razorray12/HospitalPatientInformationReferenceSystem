package com.example.hospitalpatient.Entities;

public class Patient {
    private String id;
    private String firstName;
    private String lastName;
    private String middleName;
    private String birthDate;
    private String phoneNumber;
    private String diagnosis;
    private String room;
    private String sex;
    private String admissionDate;
    private String medications;
    private String allergies;
    private VitalSigns vitalSigns;
    private String mainDoctor;

    public Patient(String id, String firstName, String lastName, String middleName,
                   String birthDate, String phoneNumber, String diagnosis,
                   String room, String admissionDate, String allergies,
                   String medications, VitalSigns vitalSigns, String sex, String mainDoctor) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.diagnosis = diagnosis;
        this.room = room;
        this.admissionDate = admissionDate;
        this.medications = medications;
        this.allergies = allergies;
        this.vitalSigns = vitalSigns;
        this.sex = sex;
        this.mainDoctor = mainDoctor;
    }
    public Patient() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String  getBirthDate() { return birthDate; }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(String admissionDate) {
        this.admissionDate = admissionDate;
    }

    public String getMedications() {
        return medications;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public VitalSigns getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(VitalSigns vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public String getSex() { return sex; }

    public void setSex(String sex) { this.sex = sex; }

    public String getMainDoctor() { return mainDoctor; }

    public void setMainDoctor(String mainDoctor) { this.mainDoctor = mainDoctor; }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}