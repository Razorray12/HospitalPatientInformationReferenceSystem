package com.example.hospitalpatient.Activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hospitalpatient.Fragments.RegisterDialogFragment;
import com.example.hospitalpatient.R;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextFirstName;
    private EditText editTextLastName;
    private EditText editTextMiddleName;
    private EditText editTextExperience;
    private EditText editTextSpecialization;
    private CheckBox checkBoxNurse;
    private CheckBox checkBoxDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        editTextFirstName = findViewById(R.id.editTextTextFirstName);
        editTextLastName = findViewById(R.id.editTextTextLastName);
        editTextMiddleName = findViewById(R.id.editTextTextMiddleName);
        editTextExperience = findViewById(R.id.editTextTextExperience);
        editTextSpecialization = findViewById(R.id.editTextTextSpecialization);
        checkBoxNurse = findViewById(R.id.checkBoxNurse);
        checkBoxDoctor = findViewById(R.id.checkBoxDoctor);

        Button buttonOnRegisterDialogFragment = findViewById(R.id.buttonOnRegisterDialogFragment);
        buttonOnRegisterDialogFragment.setOnClickListener(view -> {
            String firstName = editTextFirstName.getText().toString().trim();
            String lastName = editTextLastName.getText().toString().trim();
            String middleName = editTextMiddleName.getText().toString().trim();
            String experience = editTextExperience.getText().toString().trim();
            String specialization = editTextSpecialization.getText().toString().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || experience.isEmpty() || middleName.isEmpty()) {
                Toast.makeText(RegistrationActivity.this, "Заполните все обязательные поля!", Toast.LENGTH_SHORT).show();
                return;
            }

            String userType = "";
            if (checkBoxNurse.isChecked() && checkBoxDoctor.isChecked()) {
                Toast.makeText(RegistrationActivity.this, "Выберите что-то одно!", Toast.LENGTH_SHORT).show();
                return;
            } else if (checkBoxNurse.isChecked()) {
                userType = "Медсестра";
                editTextSpecialization.setEnabled(false);
                editTextSpecialization.setText("");
            } else if (checkBoxDoctor.isChecked()) {
                userType = "Доктор";
                if (TextUtils.isEmpty(specialization)) {
                    Toast.makeText(RegistrationActivity.this, "Укажите специализацию!", Toast.LENGTH_SHORT).show();
                    return;
                }
                editTextSpecialization.setEnabled(true);
            } else {
                Toast.makeText(RegistrationActivity.this, "Выберите тип пользователя!", Toast.LENGTH_SHORT).show();
                return;
            }

            RegisterDialogFragment registerDialog = new RegisterDialogFragment();
            Bundle args = new Bundle();
            args.putString("firstName", firstName);
            args.putString("lastName", lastName);
            args.putString("middleName", middleName);
            args.putString("experience", experience);
            args.putString("specialization", specialization);
            args.putString("userType", userType);
            registerDialog.setArguments(args);
            registerDialog.show(getSupportFragmentManager(), "registerDialog");
        });

        checkBoxNurse.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxDoctor.setChecked(false);
                editTextSpecialization.setEnabled(false);
                editTextSpecialization.setText("");
            }
        });

        checkBoxDoctor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkBoxNurse.setChecked(false);
                editTextSpecialization.setEnabled(true);
            } else {
                editTextSpecialization.setEnabled(false);
                editTextSpecialization.setText("");
            }
        });
    }
}
