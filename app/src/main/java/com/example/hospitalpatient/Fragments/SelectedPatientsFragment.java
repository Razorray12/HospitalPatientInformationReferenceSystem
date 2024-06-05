package com.example.hospitalpatient.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalpatient.Adapters.SelectedPatientAdapter;
import com.example.hospitalpatient.Entities.Patient;
import com.example.hospitalpatient.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SelectedPatientsFragment extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference database;
    SelectedPatientAdapter patientAdapter;
    ArrayList<Patient> patients;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_patients, container, false);
        database = FirebaseDatabase.getInstance().getReference("users/patients");
        recyclerView = view.findViewById(R.id.recyclerview_patient2);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        patients = new ArrayList<>();

        patientAdapter = new SelectedPatientAdapter(getContext(),patients);
        recyclerView.setAdapter(patientAdapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patients.clear();
                for (DataSnapshot patientSnapshot : snapshot.getChildren()) {
                    Patient patient = patientSnapshot.getValue(Patient.class);
                    patients.add(patient);
                }
                patientAdapter.setPatients(patients);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        return view;
    }
}