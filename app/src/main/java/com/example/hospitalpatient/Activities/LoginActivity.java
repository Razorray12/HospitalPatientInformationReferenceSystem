package com.example.hospitalpatient.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hospitalpatient.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private boolean alreadyShownToast = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login_activity), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);

            startActivity(intent);
            finish();
        }

        editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextPassword = findViewById(R.id.editTextTextPassword);

        Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(view -> loginUser());

        TextView textViewRegisterLink = findViewById(R.id.textViewRegisterLink);
        textViewRegisterLink.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        ConstraintLayout loginFocusLayout = findViewById(R.id.login_activity);
        loginFocusLayout.setOnClickListener(view -> {
            loginFocusLayout.clearFocus();

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });
    }

    private void loginUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            if (!alreadyShownToast) {
                Toast.makeText(LoginActivity.this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(() -> alreadyShownToast = false, 3000);
                alreadyShownToast = true;
            }
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    ProgressBar progressBar = findViewById(R.id.progressBarLogin);
                    View viewFocus = findViewById(R.id.viewFocus);

                    viewFocus.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);

                    if (task.isSuccessful()) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                        startActivity(intent);
                        finish();
                    } else {
                        String errorMessage = Objects.requireNonNull(task.getException()).getMessage();
                        if (Objects.requireNonNull(errorMessage).contains("incorrect") || errorMessage.contains("6") || errorMessage.contains("format")) {
                            if (!alreadyShownToast) {
                                Toast.makeText(LoginActivity.this, "Неверный логин или пароль!", Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(() -> alreadyShownToast = false, 3000);

                                alreadyShownToast = true;
                            }
                        }
                        else {
                            if (!alreadyShownToast) {
                                Toast.makeText(LoginActivity.this, "Ошибка входа: " + errorMessage, Toast.LENGTH_SHORT).show();
                                new Handler().postDelayed(() -> alreadyShownToast = false, 3000);

                                alreadyShownToast = true;
                            }
                        }
                    }
                });
    }
}