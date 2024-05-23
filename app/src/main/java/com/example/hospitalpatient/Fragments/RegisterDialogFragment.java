package com.example.hospitalpatient.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.hospitalpatient.Activities.LoginActivity;
import com.example.hospitalpatient.Entities.Doctor;
import com.example.hospitalpatient.Entities.Nurse;
import com.example.hospitalpatient.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterDialogFragment extends DialogFragment {

    private EditText emailEditText;
    private EditText passwordEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.RoundedCornersDialog);

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_dialog_registration, null);

        emailEditText = view.findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = view.findViewById(R.id.editTextTextPassword);

        Button buttonOnLoginActivity = view.findViewById(R.id.onLoginActivity);
        buttonOnLoginActivity.setOnClickListener(v -> registerUser());

        ImageButton closeLoginButton = view.findViewById(R.id.CloseRegister);
        closeLoginButton.setOnClickListener(v -> {
            Dialog dialog = getDialog();
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(1000, ViewGroup.LayoutParams.WRAP_CONTENT);
            getDialog().getWindow().setGravity(android.view.Gravity.CENTER);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getDialog() != null && getDialog().getWindow() != null) {
            Window window = getDialog().getWindow();
            window.setWindowAnimations(R.style.RoundedCornersDialog);
            WindowManager.LayoutParams params = window.getAttributes();
            params.dimAmount = 0.7f;
            window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            window.setAttributes(params);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void registerUser() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length()<6){
            Toast.makeText(requireContext(), "Пароль должен быть не менее 6 символов!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidEmail(email)) {
            Toast.makeText(requireContext(), "Введите корректный email адрес!", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (firebaseUser != null) {
                            String userId = firebaseUser.getUid();
                            Bundle args = getArguments();
                            saveUserToDataBase(userId, args.getString("firstName"), args.getString("lastName"), args.getString("middleName"),
                                    args.getString("experience"), args.getString("specialization"));
                            Intent intent = new Intent(requireContext(), LoginActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    } else {
                        Toast.makeText(requireContext(), "Пользователь уже зарегистрирован!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserToDataBase(String userId, String firstName, String lastName, String middleName, String experience, String specialization) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");

        Bundle args = getArguments();

        if (args != null) {
            String userType = args.getString("userType");
            if ("Доктор".equals(userType)) {
                Doctor doctor = new Doctor(userId, firstName, lastName, middleName, experience, specialization);
                usersRef.child("doctors").child(userId).setValue(doctor);
            } else if ("Медсестра".equals(userType)) {
                Nurse nurse = new Nurse(userId, firstName, lastName, middleName, experience);
                usersRef.child("nurses").child(userId).setValue(nurse);
            }
        }
    }
}


