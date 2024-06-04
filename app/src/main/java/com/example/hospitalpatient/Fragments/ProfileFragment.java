package com.example.hospitalpatient.Fragments;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.example.hospitalpatient.Activities.LoginActivity;
import com.example.hospitalpatient.Activities.MainActivity;
import com.example.hospitalpatient.R;
import com.facebook.shimmer.ShimmerFrameLayout;
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

public class ProfileFragment extends Fragment {

    private boolean alreadyShownToast = false;
    private boolean isDoctor = false;
    private LinearLayout profileLayout;
    private NestedScrollView scrollView;
    private View rootView;

    private FirebaseUser user;
    private DatabaseReference userRef;
    private String userId;


    TextView emailTextView;
    TextView firstNameTextView;
    TextView lastNameTextView;
    TextView middleNameTextView;
    TextView experienceTextView;
    TextView specializationTextView;
    TextView specializationInvisibleTextView;

    EditText emailEditText;
    EditText firstNameEditText;
    EditText lastNameEditText;
    EditText middleNameEditText;
    EditText experienceEditText;
    EditText specializationEditText;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        scrollView = rootView.findViewById(R.id.scroll_profile);
        profileLayout = rootView.findViewById(R.id.linear_profile);

        emailTextView = profileLayout.findViewById(R.id.text_email);
        firstNameTextView = profileLayout.findViewById(R.id.text_first_name);
        lastNameTextView = profileLayout.findViewById(R.id.text_last_name);
        middleNameTextView = profileLayout.findViewById(R.id.text_middle_name);
        experienceTextView = profileLayout.findViewById(R.id.text_experience);
        specializationTextView = profileLayout.findViewById(R.id.text_specialization);
        specializationInvisibleTextView = profileLayout.findViewById(R.id.specialization_for_invisible);

        emailEditText = profileLayout.findViewById(R.id.edit_email);
        firstNameEditText = profileLayout.findViewById(R.id.edit_first_name);
        lastNameEditText = profileLayout.findViewById(R.id.edit_last_name);
        middleNameEditText = profileLayout.findViewById(R.id.edit_middle_name);
        experienceEditText = profileLayout.findViewById(R.id.edit_experience);
        specializationEditText = profileLayout.findViewById(R.id.edit_specialization);

        scrollView.setOnClickListener(view -> {
            scrollView.clearFocus();

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

        profileLayout.setOnClickListener(view -> {
            profileLayout.clearFocus();

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

                if (isDoctor) {
                    userRef = FirebaseDatabase.getInstance().getReference("users/doctors").child(userId);
                } else {
                    userRef = FirebaseDatabase.getInstance().getReference("users/nurses").child(userId);
                }

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String email = user.getEmail();
                        String firstName = snapshot.child("firstName").getValue(String.class);
                        String lastName = snapshot.child("lastName").getValue(String.class);
                        String middleName = snapshot.child("middleName").getValue(String.class);
                        String experience = snapshot.child("experience").getValue(String.class);

                        if(isDoctor) {
                            String specialization = snapshot.child("specialization").getValue(String.class);
                            specializationEditText.setText(specialization);
                            specializationTextView.setText(specialization);
                        }
                        else {
                            specializationInvisibleTextView.setVisibility(View.GONE);
                            specializationTextView.setVisibility(View.GONE);
                            specializationEditText.setVisibility(View.GONE);
                        }

                        emailEditText.setText(email);
                        firstNameEditText.setText(firstName);
                        lastNameEditText.setText(lastName);
                        middleNameEditText.setText(middleName);
                        experienceEditText.setText(experience);

                        emailTextView.setText(email);
                        firstNameTextView.setText(firstName);
                        lastNameTextView.setText(lastName);
                        middleNameTextView.setText(middleName);
                        experienceTextView.setText(experience);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    ShimmerFrameLayout shimmerLinearLayout = rootView.findViewById(R.id.goat_profile);

                    shimmerLinearLayout.setVisibility(View.GONE);
                }, 1000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        Button signOutButton = profileLayout.findViewById(R.id.sign_out);
        TextView deleteAccountView = profileLayout.findViewById(R.id.delete_account);

        signOutButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);

            builder.setView(dialogView);

            AlertDialog alertDialog = builder.create();

            TextView positiveButton = dialogView.findViewById(R.id.positiveButton);
            TextView negativeButton = dialogView.findViewById(R.id.negativeButton);

            positiveButton.setOnClickListener(v -> {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                alertDialog.dismiss();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            });

            negativeButton.setOnClickListener(v -> alertDialog.dismiss());

            alertDialog.show();

            Objects.requireNonNull(alertDialog.getWindow())
                    .setBackgroundDrawableResource(R.drawable.alertdialog_background);
        });

        deleteAccountView.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View dialogView = inflater.inflate(R.layout.custom_alert_delete_dialog, null);

            builder.setView(dialogView);

            AlertDialog alertDialog = builder.create();

            TextView positiveButton = dialogView.findViewById(R.id.positiveButtonDelete);
            TextView negativeButton = dialogView.findViewById(R.id.negativeButtonDelete);

            positiveButton.setOnClickListener(v -> {
                alertDialog.dismiss();
                userRef.removeValue()
                        .addOnSuccessListener(aVoid -> {
                        })
                        .addOnFailureListener(e -> {
                        });

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();

                user.delete();
                Toast.makeText(requireContext(), "Аккаунт удален!", Toast.LENGTH_SHORT).show();


                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                requireActivity().finish();
            });

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

            emailTextView.setVisibility(View.GONE);
            emailEditText.setVisibility(View.VISIBLE);
            firstNameTextView.setVisibility(View.GONE);
            firstNameEditText.setVisibility(View.VISIBLE);
            lastNameTextView.setVisibility(View.GONE);
            lastNameEditText.setVisibility(View.VISIBLE);
            middleNameTextView.setVisibility(View.GONE);
            middleNameEditText.setVisibility(View.VISIBLE);
            specializationTextView.setVisibility(View.GONE);
            if (isDoctor) {
                specializationEditText.setVisibility(View.VISIBLE);
            }
            experienceTextView.setVisibility(View.GONE);
            experienceEditText.setVisibility(View.VISIBLE);

            item.setVisible(false);

            ((MainActivity) requireActivity()).showSaveButton();
            ((MainActivity) requireActivity()).showCloseEditButton();
            ((MainActivity) requireActivity()).showCloseEditButton();

            return true;
        } else if (id == R.id.action_close_edit) {

            emailTextView.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.GONE);
            firstNameTextView.setVisibility(View.VISIBLE);
            firstNameEditText.setVisibility(View.GONE);
            lastNameTextView.setVisibility(View.VISIBLE);
            lastNameEditText.setVisibility(View.GONE);
            middleNameTextView.setVisibility(View.VISIBLE);
            middleNameEditText.setVisibility(View.GONE);
            if (isDoctor) {
                specializationTextView.setVisibility(View.VISIBLE);
            }
            specializationEditText.setVisibility(View.GONE);
            experienceTextView.setVisibility(View.VISIBLE);
            experienceEditText.setVisibility(View.GONE);

            item.setVisible(false);

            ((MainActivity) requireActivity()).showEditButton();
            ((MainActivity) requireActivity()).closeSaveButton();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) requireActivity()).setToolbarSaveButtonListener(v ->  {

            String email = emailEditText.getText().toString();
            if (!isValidEmail(email)) {
                if (!alreadyShownToast) {
                    Toast.makeText(requireContext(), "Введите корректный email адрес!", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> alreadyShownToast = false, 3000);
                    alreadyShownToast = true;
                }
                return;
            }
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String middleName = middleNameEditText.getText().toString();
            String experience = experienceEditText.getText().toString();
            String specialization = specializationEditText.getText().toString();

            Map<String, Object> userUpdates = new HashMap<>();
            userUpdates.put("email", email);
            userUpdates.put("firstName", firstName);
            userUpdates.put("lastName", lastName);
            userUpdates.put("middleName", middleName);
            userUpdates.put("experience", experience);
            if (isDoctor) {
                userUpdates.put("specialization", specialization);
            }

            if (isDoctor) {
                userRef = FirebaseDatabase.getInstance().getReference("users/doctors").child(userId);
            } else {
                userRef = FirebaseDatabase.getInstance().getReference("users/nurses").child(userId);
            }

            userRef.updateChildren(userUpdates);

            emailTextView.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.GONE);
            firstNameTextView.setVisibility(View.VISIBLE);
            firstNameEditText.setVisibility(View.GONE);
            lastNameTextView.setVisibility(View.VISIBLE);
            lastNameEditText.setVisibility(View.GONE);
            middleNameTextView.setVisibility(View.VISIBLE);
            middleNameEditText.setVisibility(View.GONE);
            if (isDoctor) {
                specializationTextView.setVisibility(View.VISIBLE);
            }
            specializationEditText.setVisibility(View.GONE);
            experienceTextView.setVisibility(View.VISIBLE);
            experienceEditText.setVisibility(View.GONE);


            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(rootView.getWindowToken(), 0);

            ((MainActivity) requireActivity()).closeSaveButton();
            ((MainActivity) requireActivity()).closeCloseEditButton();
            ((MainActivity) requireActivity()).showEditButton();
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        return email.matches(emailRegex);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            emailTextView.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.GONE);
            firstNameTextView.setVisibility(View.VISIBLE);
            firstNameEditText.setVisibility(View.GONE);
            lastNameTextView.setVisibility(View.VISIBLE);
            lastNameEditText.setVisibility(View.GONE);
            middleNameTextView.setVisibility(View.VISIBLE);
            middleNameEditText.setVisibility(View.GONE);
            if (isDoctor) {
                specializationTextView.setVisibility(View.VISIBLE);
            }
            specializationEditText.setVisibility(View.GONE);
            experienceTextView.setVisibility(View.VISIBLE);
            experienceEditText.setVisibility(View.GONE);

            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(requireView().getWindowToken(), 0);

        }
    }

    public void onHideEditText() {
        emailTextView.setVisibility(View.VISIBLE);
        emailEditText.setVisibility(View.GONE);
        firstNameTextView.setVisibility(View.VISIBLE);
        firstNameEditText.setVisibility(View.GONE);
        lastNameTextView.setVisibility(View.VISIBLE);
        lastNameEditText.setVisibility(View.GONE);
        middleNameTextView.setVisibility(View.VISIBLE);
        middleNameEditText.setVisibility(View.GONE);
        if (isDoctor) {
            specializationTextView.setVisibility(View.VISIBLE);
        }
        specializationEditText.setVisibility(View.GONE);
        experienceTextView.setVisibility(View.VISIBLE);
        experienceEditText.setVisibility(View.GONE);
    }
}
