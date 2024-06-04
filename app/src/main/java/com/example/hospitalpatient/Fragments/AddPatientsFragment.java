package com.example.hospitalpatient.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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


public class AddPatientsFragment extends Fragment {
    private LinearLayout addLayout;
    private NestedScrollView addscrollView;

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

        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_close_edit) {
            Fragment currentFragment = requireActivity().getSupportFragmentManager().findFragmentByTag("add_patient_fragment");
            SearchFragment searchFragment = (SearchFragment) requireActivity().getSupportFragmentManager().findFragmentByTag("search_fragment");

            requireActivity().getSupportFragmentManager().beginTransaction().hide(Objects.requireNonNull(currentFragment)).show(Objects.requireNonNull(searchFragment)).commit();

            ((MainActivity) requireActivity()).closeSaveButton();
            ((MainActivity) requireActivity()).setTextForSearch();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) requireActivity()).setToolbarSaveButtonListener(() -> {
            Toast.makeText(getContext(),"lox", Toast.LENGTH_SHORT).show();
        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        ((MainActivity) requireActivity()).setToolbarSaveButtonListener(() -> {
//            Toast.makeText(getContext(),"lox", Toast.LENGTH_SHORT).show();
//        });
//    }
}