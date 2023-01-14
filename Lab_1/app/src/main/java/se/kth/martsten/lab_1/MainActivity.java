package se.kth.martsten.lab_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import se.kth.martsten.lab_1.view.ResultViewModel;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.navigation);
        bottomNavigationView.setOnItemSelectedListener(this::navigationListener);
        loadFragment(new PlayFragment());
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
    }

    @SuppressLint("NonConstantResourceId")
    private boolean navigationListener(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.navigation_settings:
                loadFragment(new SettingsFragment());
                return true;
            case R.id.navigation_play:
                loadFragment(new PlayFragment());
                return true;
            case R.id.navigation_results:
                loadFragment(new ResultsFragment());
                return true;
        }
        return false;
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_container, fragment, "");
        fragmentTransaction.commit();
    }
}