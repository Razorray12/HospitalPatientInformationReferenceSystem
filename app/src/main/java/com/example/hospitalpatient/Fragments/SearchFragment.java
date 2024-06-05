package com.example.hospitalpatient.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalpatient.Adapters.PatientAdapter;
import com.example.hospitalpatient.Entities.Patient;
import com.example.hospitalpatient.R;
import com.example.hospitalpatient.ViewModels.PatientViewModel;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class SearchFragment extends Fragment {
    private boolean isDoctor = false;
    private OnFragmentSwitchListener fragmentSwitchListener;
    RecyclerView recyclerView;
    DatabaseReference database;
    PatientAdapter patientAdapter;
    ArrayList<Patient> patients;
    private ShimmerFrameLayout shimmerView1, shimmerView2, shimmerView3, shimmerView4, shimmerView5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        database = FirebaseDatabase.getInstance().getReference("users/patients");
        recyclerView = view.findViewById(R.id.recyclerview_patient);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        patients = new ArrayList<>();

        patientAdapter = new PatientAdapter(getContext(),patients);

        patientAdapter.setOnItemClickListener(position -> {
            if (fragmentSwitchListener != null) {
                Patient clickedPatient = patients.get(position);
                PatientViewModel patientViewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);
                patientViewModel.selectPatient(clickedPatient);

                fragmentSwitchListener.onSwitchToInformationFragment();
            }
        });

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

                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    LinearLayout shimmerLinearLayout = view.findViewById(R.id.shimmer_view_container);
                    shimmerView1 = view.findViewById(R.id.shimmer_view_1);
                    shimmerView2 = view.findViewById(R.id.shimmer_view_2);
                    shimmerView3 = view.findViewById(R.id.shimmer_view_3);
                    shimmerView4 = view.findViewById(R.id.shimmer_view_4);
                    shimmerView5 = view.findViewById(R.id.shimmer_view_5);


                    shimmerView1.stopShimmer();
                    shimmerView2.stopShimmer();
                    shimmerView3.stopShimmer();
                    shimmerView4.stopShimmer();
                    shimmerView5.stopShimmer();
                    shimmerLinearLayout.setVisibility(View.GONE);
                }, 2000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        return view;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void onQueryTextSubmit(String query) {
        ArrayList<Patient> filteredPatients = filterPatients(patients, query);
        patientAdapter.setPatients(filteredPatients);
        patientAdapter.notifyDataSetChanged();
    }

    private ArrayList<Patient> filterPatients(ArrayList<Patient> patients, String query) {
        ArrayList<Patient> filteredPatients = new ArrayList<>();
        for (Patient patient : patients) {
            if (patient.getFirstName().toLowerCase().contains(query.toLowerCase()) ||
                    patient.getLastName().toLowerCase().contains(query.toLowerCase()) ||
                    patient.getMiddleName().toLowerCase().contains(query.toLowerCase())) {
                filteredPatients.add(patient);
            }
        }
        return filteredPatients;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void showAllPatients() {
        patientAdapter.setPatients(patients);
        patientAdapter.notifyDataSetChanged();
    }

    public interface OnFragmentSwitchListener {
        void onSwitchToInformationFragment();
    }

    public void setOnFragmentSwitchListener(OnFragmentSwitchListener listener) {
        this.fragmentSwitchListener = listener;
    }
}