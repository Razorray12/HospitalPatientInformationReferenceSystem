package com.example.hospitalpatient.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hospitalpatient.Entities.Patient;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<Patient>> selectedPatients = new MutableLiveData<ArrayList<Patient>>();

    public void select(ArrayList<Patient> patients) {
        selectedPatients.setValue(patients);
    }

    public LiveData<ArrayList<Patient>> getSelected() {
        return selectedPatients;
    }
}
