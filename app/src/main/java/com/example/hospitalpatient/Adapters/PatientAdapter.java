package com.example.hospitalpatient.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalpatient.Entities.Patient;
import com.example.hospitalpatient.Holders.PatientViewHolder;
import com.example.hospitalpatient.R;

import java.util.ArrayList;
import java.util.Random;

public class PatientAdapter extends RecyclerView.Adapter<PatientViewHolder> {
    Context context;
    ArrayList<Patient> patients;
    int[] menAvatars = {R.drawable.img_2, R.drawable.img_3,R.drawable.img_6,R.drawable.img_8};
    int[] womenAvatars = {R.drawable.img_1, R.drawable.img_4, R.drawable.img_5,R.drawable.img_7};

    public PatientAdapter(Context context, ArrayList<Patient> patients) {
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
        this.patients = patients;
        notifyDataSetChanged();
    }
}
