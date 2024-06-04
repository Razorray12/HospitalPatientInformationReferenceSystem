package com.example.hospitalpatient.Activities;

import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.hospitalpatient.Fragments.AddPatientsFragment;
import com.example.hospitalpatient.Fragments.ProfileFragment;
import com.example.hospitalpatient.Fragments.SearchFragment;
import com.example.hospitalpatient.Fragments.SelectedPatientsFragment;
import com.example.hospitalpatient.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;
    private SearchFragment searchFragment;
    private ProfileFragment profileFragment;
    private AddPatientsFragment addPatientFragment;
    private Menu menu;
    private SelectedPatientsFragment selectedPatientsFragment;
    private SearchView searchView;
    private TextView nameFragment;
    private AppCompatImageButton saveButton;
    private AppCompatImageButton saveButton1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("");

        nameFragment = toolbar.findViewById(R.id.name_fragment);

        searchView = findViewById(R.id.action_search);
        saveButton = findViewById(R.id.save_button);
        saveButton1 = findViewById(R.id.save_button1);

        int hintColor = ContextCompat.getColor(this, R.color.search);
        searchView.setQueryHint(Html.fromHtml("<font color = \""+ hintColor + "\">" + getResources().getString(R.string.search) + "</font>"));

        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.handles));

        setupSearchView();

        searchFragment = new SearchFragment();
        profileFragment = new ProfileFragment();
        selectedPatientsFragment = new SelectedPatientsFragment();
        addPatientFragment = new AddPatientsFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, searchFragment, "search_fragment").commit();

        currentFragment = searchFragment;

        AppCompatImageButton patientsImageButton = findViewById(R.id.patients);
        AppCompatImageButton addedPatientsImageButton = findViewById(R.id.added_patients);
        AppCompatImageButton profileImageButton = findViewById(R.id.profile);
        patientsImageButton.setColorFilter(ContextCompat.getColor(this,R.color.white));


        patientsImageButton.setOnClickListener(v -> {
            saveButton.setVisibility(View.GONE);
            saveButton1.setVisibility(View.GONE);
            patientsImageButton.setColorFilter(ContextCompat.getColor(this,R.color.white));
            addedPatientsImageButton.setColorFilter(ContextCompat.getColor(this,R.color.handles));
            profileImageButton.setColorFilter(ContextCompat.getColor(this,R.color.handles));
            nameFragment.setText("Пациенты");
            searchView.setVisibility(View.VISIBLE);
            if (!(currentFragment instanceof SearchFragment)) {
                getSupportFragmentManager().beginTransaction().hide(currentFragment).show(searchFragment).commit();
                currentFragment = searchFragment;
                invalidateOptionsMenu();
            }
        });

        addedPatientsImageButton.setOnClickListener(v -> {
            saveButton.setVisibility(View.GONE);
            addedPatientsImageButton.setColorFilter(ContextCompat.getColor(this,R.color.white));
            patientsImageButton.setColorFilter(ContextCompat.getColor(this,R.color.handles));
            profileImageButton.setColorFilter(ContextCompat.getColor(this,R.color.handles));
            saveButton.setVisibility(View.GONE);
            searchView.setVisibility(View.VISIBLE);
            nameFragment.setText("Выбранные");
            if (!(currentFragment instanceof SelectedPatientsFragment)) {
                if (selectedPatientsFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).show(selectedPatientsFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).add(R.id.fragment_container, selectedPatientsFragment, "selected_patients_fragment").commit();
                }
                currentFragment = selectedPatientsFragment;
                invalidateOptionsMenu();
            }
        });

        profileImageButton.setOnClickListener(v -> {
            saveButton.setVisibility(View.GONE);
            showEditButton();
            profileImageButton.setColorFilter(ContextCompat.getColor(this,R.color.white));
            patientsImageButton.setColorFilter(ContextCompat.getColor(this,R.color.handles));
            addedPatientsImageButton.setColorFilter(ContextCompat.getColor(this,R.color.handles));
            closeCloseEditButton();
            nameFragment.setText("Профиль");
            searchView.setVisibility(View.GONE);

            if (!(currentFragment instanceof ProfileFragment)) {
                if (profileFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).show(profileFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).add(R.id.fragment_container, profileFragment, "profile_fragment").commit();
                }
                currentFragment = profileFragment;
                invalidateOptionsMenu();
            }
            if (profileFragment != null && profileFragment.isVisible()) {
                profileFragment.onHideEditText();
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItemEdit = menu.findItem(R.id.action_edit);
        MenuItem menuItemAdd = menu.findItem(R.id.action_add);
        MenuItem menuItemClose = menu.findItem(R.id.action_close_edit);
        MenuItem menuItemClose1 = menu.findItem(R.id.action_close_edit1);

        if (currentFragment instanceof SearchFragment) {
            menuItemAdd.setVisible(true);
            menuItemEdit.setVisible(false);
            menuItemClose.setVisible(false);
            menuItemClose1.setVisible(false);

        } else if (currentFragment instanceof ProfileFragment) {
            menuItemAdd.setVisible(false);
            menuItemEdit.setVisible(true);
        } else if (currentFragment instanceof SelectedPatientsFragment){
            menuItemAdd.setVisible(true);
            menuItemEdit.setVisible(false);
        }
        else if (currentFragment instanceof  AddPatientsFragment) {
            menuItemAdd.setVisible(false);
            menuItemClose1.setVisible(true);
            menuItemEdit.setVisible(false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            searchView.setVisibility(View.GONE);
            saveButton1.setVisibility(View.VISIBLE);
            nameFragment.setText("Добавление");
            if (!(currentFragment instanceof AddPatientsFragment)) {
                if (addPatientFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).show(addPatientFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().hide(currentFragment).add(R.id.fragment_container, addPatientFragment, "add_patient_fragment").commit();
                }
                currentFragment = addPatientFragment;
                invalidateOptionsMenu();
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.action_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("search_fragment");

                if (searchFragment != null) {
                    searchFragment.onQueryTextSubmit(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                onQueryTextSubmit(newText);
                return true;
            }
        });
        searchView.setOnCloseListener(() -> {
            SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentByTag("search_fragment");
            if (searchFragment != null) {
                searchFragment.showAllPatients();
            }
            return false;
        });

    }

    public void setToolbarSaveButtonListener(View.OnClickListener listener) {
        saveButton.setOnClickListener(listener);
    }

    public void setToolbarSaveButtonListener1(View.OnClickListener listener) {
        saveButton1.setOnClickListener(listener);
    }

    public void showSaveButton() {
        saveButton.setVisibility(View.VISIBLE);
    }

    public void closeSaveButton() { saveButton.setVisibility(View.GONE); }

    public void closeSaveButton1() { saveButton1.setVisibility(View.GONE); }

    public void showCloseEditButton() {
            MenuItem item = menu.findItem(R.id.action_close_edit);
            item.setVisible(true);
    }

    public void closeCloseEditButton() {
        MenuItem item = menu.findItem(R.id.action_close_edit);
        item.setVisible(false);
    }

    public void closeCloseEditButton1() {
        if (!(currentFragment instanceof SearchFragment)) {
            getSupportFragmentManager().beginTransaction().hide(currentFragment).show(searchFragment).commit();
            currentFragment = searchFragment;
            invalidateOptionsMenu();
        }
    }

    public void showSearchView() {
        searchView.setVisibility(View.VISIBLE);
    }

    public void showEditButton() {
        MenuItem item = menu.findItem(R.id.action_edit);
        item.setVisible(true);
    }

    public void setTextForSearch() {
        nameFragment.setText("Пациенты");
    }
}