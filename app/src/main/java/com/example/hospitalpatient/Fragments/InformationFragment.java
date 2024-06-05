package com.example.hospitalpatient.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.hospitalpatient.Activities.LoginActivity;
import com.example.hospitalpatient.Activities.MainActivity;
import com.example.hospitalpatient.Entities.Patient;
import com.example.hospitalpatient.Entities.VitalSigns;
import com.example.hospitalpatient.R;
import com.example.hospitalpatient.ViewModels.PatientViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class InformationFragment extends Fragment {

    private LinearLayout informationLayout;
    private NestedScrollView informantionScrollView;
    private LinearLayout linearLayout;
    private View rootView;
    private boolean isDoctor;
    private boolean isRotated = false;
    private ObjectAnimator rotate;
    private ImageButton buttonShowSigns;
    private PatientViewModel patientViewModel;
    private TextView deletePatient;

    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference patientsRef;
    private DatabaseReference deletePatientsRef;
    private String userId;

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

    private TextView TfirstName;
    private TextView TlastName;
    private TextView TmiddleName;
    private TextView TbirthDate;
    private TextView TphoneNumber;
    private TextView Tdiagnosis;
    private TextView Troom;
    private TextView Tmedications;
    private TextView Tallergies;
    private TextView Ttemperature;
    private TextView TheartRate;
    private TextView TrespiratoryRate;
    private TextView TbloodPressure;
    private TextView ToxygenSaturation;
    private TextView TbloodGlucose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_information, container, false);

        informationLayout = rootView.findViewById(R.id.linear_information);
        informantionScrollView = rootView.findViewById(R.id.information_scroll);

        informationLayout.setOnClickListener(view -> {
            informationLayout.clearFocus();

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        informantionScrollView.setOnClickListener(view -> {
            informantionScrollView.clearFocus();

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        deletePatient = rootView.findViewById(R.id.delete_patient);

        firstName = rootView.findViewById(R.id.edit_first_name);
        lastName = rootView.findViewById(R.id.edit_last_name);
        middleName = rootView.findViewById(R.id.edit_middle_name);
        birthDate = rootView.findViewById(R.id.edit_birthDate);
        phoneNumber = rootView.findViewById(R.id.edit_phone);
        diagnosis = rootView.findViewById(R.id.edit_diagnosis);
        room = rootView.findViewById(R.id.edit_room);
        medications = rootView.findViewById(R.id.edit_medications);
        allergies = rootView.findViewById(R.id.edit_allergies);
        temperature = rootView.findViewById(R.id.edit_vitalsigns);
        heartRate = rootView.findViewById(R.id.edit_vitalsigns1);
        respiratoryRate = rootView.findViewById(R.id.edit_vitalsigns2);
        bloodPressure = rootView.findViewById(R.id.edit_vitalsigns3);
        oxygenSaturation = rootView.findViewById(R.id.edit_vitalsigns4);
        bloodGlucose = rootView.findViewById(R.id.edit_vitalsigns5);

        TfirstName = rootView.findViewById(R.id.text_first_name);
        TlastName = rootView.findViewById(R.id.text_last_name);
        TmiddleName = rootView.findViewById(R.id.text_middle_name);
        TbirthDate = rootView.findViewById(R.id.text_birthdate);
        TphoneNumber = rootView.findViewById(R.id.text_phone_name);
        Tdiagnosis = rootView.findViewById(R.id.text_diagnosis);
        Troom = rootView.findViewById(R.id.text_room);
        Tmedications = rootView.findViewById(R.id.text_medications);
        Tallergies = rootView.findViewById(R.id.text_allergies);
        Ttemperature = rootView.findViewById(R.id.text_temperature);
        TheartRate = rootView.findViewById(R.id.text_chss);
        TrespiratoryRate = rootView.findViewById(R.id.text_respiratory);
        TbloodPressure = rootView.findViewById(R.id.text_ad);
        ToxygenSaturation = rootView.findViewById(R.id.text_saturation);
        TbloodGlucose = rootView.findViewById(R.id.text_glucose);
        isMale = rootView.findViewById(R.id.checkBoxMale);
        isFemale = rootView.findViewById(R.id.checkBoxFemale);

        buttonShowSigns = rootView.findViewById(R.id.button_plus_signs);


        buttonShowSigns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                linearLayout = rootView.findViewById(R.id.linear_layout);
                if (!isRotated) {
                    rotate = ObjectAnimator.ofFloat(buttonShowSigns, "rotation", 0f, 180f);
                    isRotated = true;

                    ObjectAnimator fadeIn = ObjectAnimator.ofFloat(linearLayout, "alpha",  0f, 1f);
                    fadeIn.setDuration(300);

                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(fadeIn);
                    animatorSet.start();

                    linearLayout.setVisibility(View.VISIBLE);

                    informantionScrollView.post(() -> informantionScrollView.fullScroll(View.FOCUS_DOWN));
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

        user = FirebaseAuth.getInstance().getCurrentUser();
        userId = Objects.requireNonNull(user).getUid();
        DatabaseReference doctorsRef = FirebaseDatabase.getInstance().getReference("users/doctors");

        doctorsRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    isDoctor = true;
                }
                patientViewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);

                patientViewModel.getSelectedPatient().observe(getViewLifecycleOwner(), patient -> {
                    if (patient.getSex().equals("Мужчина")) {
                        isMale.setChecked(true);
                    } else {
                        isFemale.setChecked(true);
                    }
                    TfirstName.setText(patient.getFirstName());
                    TlastName.setText(patient.getLastName());
                    TmiddleName.setText(patient.getMiddleName());
                    TbirthDate.setText(patient.getBirthDate());
                    TphoneNumber.setText(patient.getPhoneNumber());
                    Tdiagnosis.setText(patient.getDiagnosis());
                    Troom.setText(patient.getRoom());
                    Tmedications.setText(patient.getMedications());
                    Tallergies.setText(patient.getAllergies());
                    Ttemperature.setText(patient.getVitalSigns().getTemperature());
                    TheartRate.setText(patient.getVitalSigns().getTemperature());
                    TrespiratoryRate.setText(patient.getVitalSigns().getRespiratoryRate());
                    TbloodPressure.setText(patient.getVitalSigns().getBloodPressure());
                    ToxygenSaturation.setText(patient.getVitalSigns().getOxygenSaturation());
                    TbloodGlucose.setText(patient.getVitalSigns().getBloodGlucose());

                    firstName.setText(patient.getFirstName());
                    lastName.setText(patient.getLastName());
                    middleName.setText(patient.getMiddleName());
                    birthDate.setText(patient.getBirthDate());
                    phoneNumber.setText(patient.getPhoneNumber());
                    diagnosis.setText(patient.getDiagnosis());
                    room.setText(patient.getRoom());
                    medications.setText(patient.getMedications());
                    allergies.setText(patient.getAllergies());
                    temperature.setText(patient.getVitalSigns().getTemperature());
                    heartRate.setText(patient.getVitalSigns().getTemperature());
                    respiratoryRate.setText(patient.getVitalSigns().getRespiratoryRate());
                    bloodPressure.setText(patient.getVitalSigns().getBloodPressure());
                    oxygenSaturation.setText(patient.getVitalSigns().getOxygenSaturation());
                    bloodGlucose.setText(patient.getVitalSigns().getBloodGlucose());
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        deletePatient.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View dialogView = inflater.inflate(R.layout.custom_alert_delete_patient, null);

            builder.setView(dialogView);

            AlertDialog alertDialog = builder.create();

            TextView positiveButton = dialogView.findViewById(R.id.positiveButtonDelete);
            TextView negativeButton = dialogView.findViewById(R.id.negativeButtonDelete);

            PatientViewModel patientViewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);
            Patient patient = patientViewModel.getSelectedPatient().getValue();
            deletePatientsRef = FirebaseDatabase.getInstance().getReference("users/patients");

            if (patient != null) {
                String patientId = patient.getId();

                positiveButton.setOnClickListener(v -> {
                    alertDialog.dismiss();
                    deletePatientsRef.child(patientId).removeValue()
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(requireContext(), "Информация удалена!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(requireContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
                            });

                    ((MainActivity) requireActivity()).closeBackButton();
                    ((MainActivity) requireActivity()).showSearchView();
                    ((MainActivity) requireActivity()).setTextForSearch();
                    InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
                    ((MainActivity) requireActivity()).closeFragmentInformation();
                });
            }

            negativeButton.setOnClickListener(v -> alertDialog.dismiss());

            alertDialog.show();

            Objects.requireNonNull(alertDialog.getWindow())
                    .setBackgroundDrawableResource(R.drawable.alertdialog_background);
        });

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit) {
            firstName.setVisibility(View.VISIBLE);
            lastName.setVisibility(View.VISIBLE);
            middleName.setVisibility(View.VISIBLE);
            birthDate.setVisibility(View.VISIBLE);
            phoneNumber.setVisibility(View.VISIBLE);
            diagnosis.setVisibility(View.VISIBLE);
            room.setVisibility(View.VISIBLE);
            medications.setVisibility(View.VISIBLE);
            allergies.setVisibility(View.VISIBLE);
            temperature.setVisibility(View.VISIBLE);
            heartRate.setVisibility(View.VISIBLE);
            respiratoryRate.setVisibility(View.VISIBLE);
            bloodPressure.setVisibility(View.VISIBLE);
            oxygenSaturation.setVisibility(View.VISIBLE);
            bloodGlucose.setVisibility(View.VISIBLE);

            TfirstName.setVisibility(View.GONE);
            TlastName.setVisibility(View.GONE);
            TmiddleName.setVisibility(View.GONE);
            TbirthDate.setVisibility(View.GONE);
            TphoneNumber.setVisibility(View.GONE);
            Tdiagnosis.setVisibility(View.GONE);
            Troom.setVisibility(View.GONE);
            Tmedications.setVisibility(View.GONE);
            Tallergies.setVisibility(View.GONE);
            Ttemperature.setVisibility(View.GONE);
            TheartRate.setVisibility(View.GONE);
            TrespiratoryRate.setVisibility(View.GONE);
            TbloodPressure.setVisibility(View.GONE);
            ToxygenSaturation.setVisibility(View.GONE);
            TbloodGlucose.setVisibility(View.GONE);

            ((MainActivity) requireActivity()).closeBackButton();
            item.setVisible(false);
            ((MainActivity) requireActivity()).showCloseEditButton();
            ((MainActivity) requireActivity()).showSaveButton2();

            return true;
        }

        if (id == R.id.action_close_edit1) {
            firstName.setVisibility(View.VISIBLE);
            lastName.setVisibility(View.VISIBLE);
            middleName.setVisibility(View.VISIBLE);
            birthDate.setVisibility(View.VISIBLE);
            phoneNumber.setVisibility(View.VISIBLE);
            diagnosis.setVisibility(View.VISIBLE);
            room.setVisibility(View.VISIBLE);
            medications.setVisibility(View.VISIBLE);
            allergies.setVisibility(View.VISIBLE);
            temperature.setVisibility(View.VISIBLE);
            heartRate.setVisibility(View.VISIBLE);
            respiratoryRate.setVisibility(View.VISIBLE);
            bloodPressure.setVisibility(View.VISIBLE);
            oxygenSaturation.setVisibility(View.VISIBLE);
            bloodGlucose.setVisibility(View.VISIBLE);

            TfirstName.setVisibility(View.GONE);
            TlastName.setVisibility(View.GONE);
            TmiddleName.setVisibility(View.GONE);
            TbirthDate.setVisibility(View.GONE);
            TphoneNumber.setVisibility(View.GONE);
            Tdiagnosis.setVisibility(View.GONE);
            Troom.setVisibility(View.GONE);
            Tmedications.setVisibility(View.GONE);
            Tallergies.setVisibility(View.GONE);
            Ttemperature.setVisibility(View.GONE);
            TheartRate.setVisibility(View.GONE);
            TrespiratoryRate.setVisibility(View.GONE);
            TbloodPressure.setVisibility(View.GONE);
            ToxygenSaturation.setVisibility(View.GONE);
            TbloodGlucose.setVisibility(View.GONE);

            ((MainActivity) requireActivity()).closeBackButton();
            item.setVisible(false);
            ((MainActivity) requireActivity()).showCloseEditButton();

            return true;
        } else if (id == R.id.action_close_edit) {
            ((MainActivity) requireActivity()).showBackButton();
            ((MainActivity) requireActivity()).showEditButton();
            ((MainActivity) requireActivity()).closeSaveButton2();

            firstName.setVisibility(View.GONE);
            lastName.setVisibility(View.GONE);
            middleName.setVisibility(View.GONE);
            birthDate.setVisibility(View.GONE);
            phoneNumber.setVisibility(View.GONE);
            diagnosis.setVisibility(View.GONE);
            room.setVisibility(View.GONE);
            medications.setVisibility(View.GONE);
            allergies.setVisibility(View.GONE);
            temperature.setVisibility(View.GONE);
            heartRate.setVisibility(View.GONE);
            respiratoryRate.setVisibility(View.GONE);
            bloodPressure.setVisibility(View.GONE);
            oxygenSaturation.setVisibility(View.GONE);
            bloodGlucose.setVisibility(View.GONE);

            TfirstName.setVisibility(View.VISIBLE);
            TlastName.setVisibility(View.VISIBLE);
            TmiddleName.setVisibility(View.VISIBLE);
            TbirthDate.setVisibility(View.VISIBLE);
            TphoneNumber.setVisibility(View.VISIBLE);
            Tdiagnosis.setVisibility(View.VISIBLE);
            Troom.setVisibility(View.VISIBLE);
            Tmedications.setVisibility(View.VISIBLE);
            Tallergies.setVisibility(View.VISIBLE);
            Ttemperature.setVisibility(View.VISIBLE);
            TheartRate.setVisibility(View.VISIBLE);
            TrespiratoryRate.setVisibility(View.VISIBLE);
            TbloodPressure.setVisibility(View.VISIBLE);
            ToxygenSaturation.setVisibility(View.VISIBLE);
            TbloodGlucose.setVisibility(View.VISIBLE);

            item.setVisible(false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) requireActivity()).setToolbarSaveButtonListener2(v ->  {
            PatientViewModel patientViewModel = new ViewModelProvider(requireActivity()).get(PatientViewModel.class);
            Patient originalPatient = patientViewModel.getSelectedPatient().getValue();
            Patient updatedPatient = new Patient();

            updatedPatient.setMainDoctor(Objects.requireNonNull(originalPatient).getMainDoctor());
            updatedPatient.setSex(originalPatient.getSex());
            updatedPatient.setId(Objects.requireNonNull(originalPatient).getId());
            updatedPatient.setAdmissionDate(originalPatient.getAdmissionDate());
            updatedPatient.setFirstName(firstName.getText().toString());
            updatedPatient.setLastName(lastName.getText().toString());
            updatedPatient.setMiddleName(middleName.getText().toString());
            updatedPatient.setBirthDate(birthDate.getText().toString());
            updatedPatient.setPhoneNumber(phoneNumber.getText().toString());
            updatedPatient.setDiagnosis(diagnosis.getText().toString());
            updatedPatient.setRoom(room.getText().toString());
            updatedPatient.setMedications(medications.getText().toString());
            updatedPatient.setAllergies(allergies.getText().toString());

            VitalSigns vitalSigns = new VitalSigns();
            vitalSigns.setTemperature(temperature.getText().toString());
            vitalSigns.setHeartRate(heartRate.getText().toString());
            vitalSigns.setRespiratoryRate(respiratoryRate.getText().toString());
            vitalSigns.setBloodPressure(bloodPressure.getText().toString());
            vitalSigns.setOxygenSaturation(oxygenSaturation.getText().toString());
            vitalSigns.setBloodGlucose(bloodGlucose.getText().toString());

            updatedPatient.setVitalSigns(vitalSigns);

            patientViewModel.selectPatient(updatedPatient);

            database = FirebaseDatabase.getInstance();
            patientsRef = database.getReference("users/patients");
            patientsRef.child(updatedPatient.getId()).setValue(updatedPatient);

            firstName.setVisibility(View.GONE);
            lastName.setVisibility(View.GONE);
            middleName.setVisibility(View.GONE);
            birthDate.setVisibility(View.GONE);
            phoneNumber.setVisibility(View.GONE);
            diagnosis.setVisibility(View.GONE);
            room.setVisibility(View.GONE);
            medications.setVisibility(View.GONE);
            allergies.setVisibility(View.GONE);
            temperature.setVisibility(View.GONE);
            heartRate.setVisibility(View.GONE);
            respiratoryRate.setVisibility(View.GONE);
            bloodPressure.setVisibility(View.GONE);
            oxygenSaturation.setVisibility(View.GONE);
            bloodGlucose.setVisibility(View.GONE);

            TfirstName.setVisibility(View.VISIBLE);
            TlastName.setVisibility(View.VISIBLE);
            TmiddleName.setVisibility(View.VISIBLE);
            TbirthDate.setVisibility(View.VISIBLE);
            TphoneNumber.setVisibility(View.VISIBLE);
            Tdiagnosis.setVisibility(View.VISIBLE);
            Troom.setVisibility(View.VISIBLE);
            Tmedications.setVisibility(View.VISIBLE);
            Tallergies.setVisibility(View.VISIBLE);
            Ttemperature.setVisibility(View.VISIBLE);
            TheartRate.setVisibility(View.VISIBLE);
            TrespiratoryRate.setVisibility(View.VISIBLE);
            TbloodPressure.setVisibility(View.VISIBLE);
            ToxygenSaturation.setVisibility(View.VISIBLE);
            TbloodGlucose.setVisibility(View.VISIBLE);

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

            ((MainActivity) requireActivity()).closeSaveButton2();
            ((MainActivity) requireActivity()).showEditButton();
            ((MainActivity) requireActivity()).closeCloseEditButton();
            ((MainActivity) requireActivity()).showBackButton();
        });

        ((MainActivity) requireActivity()).setToolbarBackButtonListener(v -> {
            ((MainActivity) requireActivity()).closeBackButton();
            ((MainActivity) requireActivity()).showSearchView();
            ((MainActivity) requireActivity()).setTextForSearch();
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
            ((MainActivity) requireActivity()).closeFragmentInformation();
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        firstName.setVisibility(View.GONE);
        lastName.setVisibility(View.GONE);
        middleName.setVisibility(View.GONE);
        birthDate.setVisibility(View.GONE);
        phoneNumber.setVisibility(View.GONE);
        diagnosis.setVisibility(View.GONE);
        room.setVisibility(View.GONE);
        medications.setVisibility(View.GONE);
        allergies.setVisibility(View.GONE);
        temperature.setVisibility(View.GONE);
        heartRate.setVisibility(View.GONE);
        respiratoryRate.setVisibility(View.GONE);
        bloodPressure.setVisibility(View.GONE);
        oxygenSaturation.setVisibility(View.GONE);
        bloodGlucose.setVisibility(View.GONE);

        TfirstName.setVisibility(View.VISIBLE);
        TlastName.setVisibility(View.VISIBLE);
        TmiddleName.setVisibility(View.VISIBLE);
        TbirthDate.setVisibility(View.VISIBLE);
        TphoneNumber.setVisibility(View.VISIBLE);
        Tdiagnosis.setVisibility(View.VISIBLE);
        Troom.setVisibility(View.VISIBLE);
        Tmedications.setVisibility(View.VISIBLE);
        Tallergies.setVisibility(View.VISIBLE);
        Ttemperature.setVisibility(View.VISIBLE);
        TheartRate.setVisibility(View.VISIBLE);
        TrespiratoryRate.setVisibility(View.VISIBLE);
        TbloodPressure.setVisibility(View.VISIBLE);
        ToxygenSaturation.setVisibility(View.VISIBLE);
        TbloodGlucose.setVisibility(View.VISIBLE);

        if(isRotated) {
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

        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);
    }
}