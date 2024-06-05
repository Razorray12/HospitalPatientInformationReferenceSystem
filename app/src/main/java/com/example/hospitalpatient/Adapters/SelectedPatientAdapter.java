package com.example.hospitalpatient.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalpatient.Entities.Doctor;
import com.example.hospitalpatient.Entities.Patient;
import com.example.hospitalpatient.Holders.PatientViewHolder;
import com.example.hospitalpatient.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class SelectedPatientAdapter extends RecyclerView.Adapter<PatientViewHolder> {
    Context context;
    ArrayList<Patient> patients;
    int[] menAvatars = {R.drawable.img_2, R.drawable.img_3,R.drawable.img_6,R.drawable.img_8};
    int[] womenAvatars = {R.drawable.img_1, R.drawable.img_4, R.drawable.img_5,R.drawable.img_7};

    public SelectedPatientAdapter(Context context, ArrayList<Patient> patients) {
        this.context = context;
        this.patients = patients;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PatientViewHolder(LayoutInflater.from(context).inflate(R.layout.patient_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        Patient patient = patients.get(position);
        String fullName = patient.getFirstName() + " " + patient.getMiddleName() + " " + patient.getLastName();

        holder.name.setText(fullName);
        holder.sex.setText(patient.getSex());
        holder.doctorName.setText(patient.getMainDoctor());
        holder.room.setText(patient.getRoom());

        if (patient.getSex().equals("Мужчина")) {
            int randomIndex = new Random().nextInt(menAvatars.length);

            holder.avatar.setImageResource(menAvatars[randomIndex]);
        }
        else {
            int randomIndex = new Random().nextInt(womenAvatars.length);

            holder.avatar.setImageResource(womenAvatars[randomIndex]);
        }
    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPatients(ArrayList<Patient> patients) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId = Objects.requireNonNull(user).getUid();

        DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference("users/doctors").child(userId);

        doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Doctor doctor = dataSnapshot.getValue(Doctor.class);
                String mainDoctor = Objects.requireNonNull(doctor).getLastName() + " " + doctor.getFirstName().charAt(0) + "." + doctor.getMiddleName().charAt(0) + ".";

                ArrayList<Patient> filteredPatients = new ArrayList<>();

                for (Patient patient : patients) {
                    if (patient.getMainDoctor() != null && patient.getMainDoctor().equals(mainDoctor)) {
                        filteredPatients.add(patient);
                    }
                }

                SelectedPatientAdapter.this.patients = filteredPatients;
                notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
