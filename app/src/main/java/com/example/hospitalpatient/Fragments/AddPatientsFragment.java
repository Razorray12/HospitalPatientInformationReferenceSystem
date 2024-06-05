package com.example.hospitalpatient.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.hospitalpatient.Activities.MainActivity;
import com.example.hospitalpatient.Entities.Doctor;
import com.example.hospitalpatient.Entities.Patient;
import com.example.hospitalpatient.Entities.VitalSigns;
import com.example.hospitalpatient.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


public class AddPatientsFragment extends Fragment {
    private LinearLayout addLayout;
    private NestedScrollView addscrollView;

    FirebaseDatabase database;
    DatabaseReference patientsRef;

    private EditText firstName;
    private EditText lastName;
    private EditText middleName;
    private EditText birthDate;
    private EditText phoneNumber;
    private EditText diagnosis;
    private EditText room;
    private CheckBox isMale;
    private CheckBox isFemale;
    private EditText medications;
    private EditText allergies;
    private EditText temperature;
    private EditText heartRate;
    private EditText respiratoryRate;
    private EditText bloodPressure;
    private EditText oxygenSaturation;
    private EditText bloodGlucose;

    private Boolean isAlreadyShown = false;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_patients, container, false);


        addLayout = rootView.findViewById(R.id.linear_add);
        addscrollView = rootView.findViewById(R.id.add_scroll);

        addscrollView.setOnClickListener(view -> {
            addscrollView.clearFocus();

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        addLayout.setOnClickListener(view -> {
            addLayout.clearFocus();

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        firstName = rootView.findViewById(R.id.edit_first_name);
        lastName = rootView.findViewById(R.id.edit_last_name);
        middleName = rootView.findViewById(R.id.edit_middle_name);
        birthDate = rootView.findViewById(R.id.edit_birthDate);
        phoneNumber = rootView.findViewById(R.id.edit_phone);
        diagnosis = rootView.findViewById(R.id.edit_diagnosis);
        room = rootView.findViewById(R.id.edit_room);
        isMale = rootView.findViewById(R.id.checkBoxMale);
        isFemale = rootView.findViewById(R.id.checkBoxFemale);
        medications = rootView.findViewById(R.id.edit_medications);
        allergies = rootView.findViewById(R.id.edit_allergies);
        temperature = rootView.findViewById(R.id.edit_vitalsigns);
        heartRate = rootView.findViewById(R.id.edit_vitalsigns1);
        respiratoryRate = rootView.findViewById(R.id.edit_vitalsigns2);
        bloodPressure = rootView.findViewById(R.id.edit_vitalsigns3);
        oxygenSaturation = rootView.findViewById(R.id.edit_vitalsigns4);
        bloodGlucose = rootView.findViewById(R.id.edit_vitalsigns5);

        ImageButton buttonShowSigns = rootView.findViewById(R.id.button_plus_signs);

        buttonShowSigns.setOnClickListener(new View.OnClickListener() {
            boolean isRotated = false;

            @Override
            public void onClick(View v) {
                ObjectAnimator rotate;
                LinearLayout linearLayout = rootView.findViewById(R.id.linear_layout);
                if (!isRotated) {
                    rotate = ObjectAnimator.ofFloat(buttonShowSigns, "rotation", 0f, 180f);
                    isRotated = true;

                    ObjectAnimator fadeIn = ObjectAnimator.ofFloat(linearLayout, "alpha",  0f, 1f);
                    fadeIn.setDuration(300);

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(fadeIn);
                    animatorSet.start();

                    linearLayout.setVisibility(View.VISIBLE);

                    addscrollView.post(() -> addscrollView.fullScroll(View.FOCUS_DOWN));
                } else {
                    rotate = ObjectAnimator.ofFloat(buttonShowSigns, "rotation", 180f, 0f);
                    isRotated = false;

                    ObjectAnimator fadeOut = ObjectAnimator.ofFloat(linearLayout, "alpha",  1f, 0f);
                    fadeOut.setDuration(300);

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(fadeOut);
                    animatorSet.start();

                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);

                            linearLayout.setVisibility(View.GONE);
                        }
                    });
                }
                rotate.setDuration(300);
                rotate.start();
            }
        });

        isMale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isFemale.setChecked(false);
            }
        });

        isFemale.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                isMale.setChecked(false);

            }
        });

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_close_edit1) {
            ((MainActivity) requireActivity()).closeSaveButton1();
            ((MainActivity) requireActivity()).setTextForSearch();
            ((MainActivity) requireActivity()).showSearchView();
            ((MainActivity) requireActivity()).closeFragmentAdd();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) requireActivity()).setToolbarSaveButtonListener1(v ->  {
            database = FirebaseDatabase.getInstance();
            patientsRef = database.getReference("users/patients");
            Patient patient = new Patient();
            VitalSigns vitalSigns = new VitalSigns();


            String SfirstName = firstName.getText().toString();
            String SlastName = lastName.getText().toString();
            String SmiddleName = middleName.getText().toString();
            String SbirthDate = birthDate.getText().toString();
            String SphoneNumber = phoneNumber.getText().toString();
            String Sdiagnosis = diagnosis.getText().toString();
            String Sroom = room.getText().toString();
            String Smedications = medications.getText().toString();
            String Sallergies = allergies.getText().toString();
            String Stemperature = temperature.getText().toString();
            String SheartRate = heartRate.getText().toString();
            String SrespiratoryRate = respiratoryRate.getText().toString();
            String SbloodPressure = bloodPressure.getText().toString();
            String SoxygenSaturation = oxygenSaturation.getText().toString();
            String SbloodGlucose = bloodGlucose.getText().toString();

                if (SfirstName.isEmpty() || SmiddleName.isEmpty() || SlastName.isEmpty() || Sroom.isEmpty()) {
                    if(!isAlreadyShown)
                    {
                        Toast.makeText(getActivity(), "Заполните все необходимые поля", Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(() -> isAlreadyShown = false, 3000);
                        isAlreadyShown = true;
                    }
                    return;
                }
            if (isMale.isChecked()) {
                patient.setSex("Мужчина");

            } else if (isFemale.isChecked()) {
                patient.setSex("Женщина");
            }
            else {
                if (!isAlreadyShown) {
                    Toast.makeText(getActivity(), "Выберите пол пациента!", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> isAlreadyShown = false, 3000);
                    isAlreadyShown = true;
                }
                return;
            }

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String userId = Objects.requireNonNull(user).getUid();

            DatabaseReference doctorRef = FirebaseDatabase.getInstance().getReference("users/doctors").child(userId);

            doctorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Doctor doctor = dataSnapshot.getValue(Doctor.class);
                    String mainDoctor = Objects.requireNonNull(doctor).getLastName() + " " + doctor.getFirstName().charAt(0) + "." + doctor.getMiddleName().charAt(0) + ".";

                    patient.setFirstName(SfirstName);
                    patient.setLastName(SlastName);
                    patient.setMiddleName(SmiddleName);
                    patient.setBirthDate(SbirthDate);
                    patient.setPhoneNumber(SphoneNumber);
                    patient.setDiagnosis(Sdiagnosis);
                    patient.setRoom(Sroom);
                    patient.setMedications(Smedications);
                    patient.setAllergies(Sallergies);

                    Calendar calendar = Calendar.getInstance();
                    Date today = calendar.getTime();

                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy");
                    String dateToStr = format.format(today);

                    patient.setAdmissionDate(dateToStr);



                    patient.setMainDoctor(mainDoctor);
                    vitalSigns.setTemperature(Stemperature);
                    vitalSigns.setHeartRate(SheartRate);
                    vitalSigns.setRespiratoryRate(SrespiratoryRate);
                    vitalSigns.setBloodPressure(SbloodPressure);
                    vitalSigns.setOxygenSaturation(SoxygenSaturation);
                    vitalSigns.setBloodGlucose(SbloodGlucose);

                    patient.setVitalSigns(vitalSigns);

                    String id = patientsRef.push().getKey();

                    patient.setId(id);

                    patientsRef.child(Objects.requireNonNull(id)).setValue(patient);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            ((MainActivity) requireActivity()).closeFragmentAdd();
            ((MainActivity) requireActivity()).showSearchView();
            ((MainActivity) requireActivity()).setTextForSearch();
            ((MainActivity) requireActivity()).closeSaveButton1();
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (hidden) {
            firstName.setText("");
            lastName.setText("");
            middleName.setText("");
            birthDate.setText("");
            phoneNumber.setText("");
            diagnosis.setText("");
            room.setText("");
            medications.setText("");
            allergies.setText("");
            temperature.setText("");
            heartRate.setText("");
            respiratoryRate.setText("");
            bloodPressure.setText("");
            oxygenSaturation.setText("");
            bloodGlucose.setText("");
            ((MainActivity) requireActivity()).closeSaveButton1();

            isMale.setChecked(false);
            isFemale.setChecked(false);

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
        }
    }
}