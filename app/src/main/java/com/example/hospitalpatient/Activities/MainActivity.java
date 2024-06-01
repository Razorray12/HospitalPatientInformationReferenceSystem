package com.example.hospitalpatient.Activities;

import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.hospitalpatient.Fragments.SearchFragment;
import com.example.hospitalpatient.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Fragment currentFragment;
    SearchView searchView;

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
        searchView = findViewById(R.id.action_search);

        int hintColor = ContextCompat.getColor(this, R.color.search);
        searchView.setQueryHint(Html.fromHtml("<font color = \""+ hintColor + "\">" + getResources().getString(R.string.search) + "</font>"));

        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchEditText.setTextColor(getResources().getColor(R.color.handles));

        setupSearchView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.action_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.patientsListFragment);

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
            SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.patientsListFragment);
            if (searchFragment != null) {
                searchFragment.showAllPatients();
            }
            return false;
        });
    }
}