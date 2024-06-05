package com.example.hospitalpatient.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hospitalpatient.Entities.Patient;

public class PatientViewModel extends ViewModel {
    private final MutableLiveData<Patient> selectedPatient = new MutableLiveData<>();

    public void selectPatient(Patient patient) {
        selectedPatient.setValue(patient);
    }

    public LiveData<Patient> getSelectedPatient() {
        return selectedPatient;
    }
}

