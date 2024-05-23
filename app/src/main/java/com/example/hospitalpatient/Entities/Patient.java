package com.example.hospitalpatient.Entities;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patient {
    private String firstName;
    private String lastName;
    private String middleName;
    private Date birthDate;
    private String phoneNumber;
    private String diagnosis;
    private String room;
    private Date admissionDate;
    private ArrayList<String> medications;
    private ArrayList<String> allergies;
    private VitalSigns vitalSigns;

    public Patient(String firstName, String lastName, String middleName,
                   Date birthDate, String phoneNumber, String diagnosis,
                   String room, Date admissionDate,ArrayList<String> allergies,
                   ArrayList<String> medications, VitalSigns vitalSigns) {
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
    }

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
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

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public List<String> getMedications() {
        return medications;
    }

    public void setMedications(ArrayList<String> medications) {
        this.medications = medications;
    }

    public List<String> getAllergies() {
        return allergies;
    }

    public void setAllergies(ArrayList<String> allergies) {
        this.allergies = allergies;
    }

    public VitalSigns getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(VitalSigns vitalSigns) {
        this.vitalSigns = vitalSigns;
    }
}