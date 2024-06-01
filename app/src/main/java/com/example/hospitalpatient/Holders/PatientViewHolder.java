package com.example.hospitalpatient.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitalpatient.R;

public class PatientViewHolder extends RecyclerView.ViewHolder {
    public ImageView avatar;
    public TextView name;
    public TextView sex;
    public TextView doctorName;
    public TextView room;

    public PatientViewHolder(@NonNull View itemView) {
        super(itemView);

        avatar = itemView.findViewById(R.id.avatar);
        name = itemView.findViewById(R.id.patient);
        sex = itemView.findViewById(R.id.sex2);
        doctorName = itemView.findViewById(R.id.doctor2);
        room = itemView.findViewById(R.id.room2);

    }
}
