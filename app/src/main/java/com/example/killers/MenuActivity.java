package com.example.killers;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.killers.fragment.CommentFragment;
import com.example.killers.fragment.CriminalFragment;
import com.example.killers.fragment.NewsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MenuActivity<selectFragment> extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment selectFragment = null;






    private NewsFragment newsFragment;
    private CriminalFragment criminalFragment;
    private CommentFragment commentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        final int commit = getSupportFragmentManager().beginTransaction().replace(R.id.fragment,
                new CommentFragment()).commit();





    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.nav_crimi:
                            selectFragment = new CriminalFragment();
                            break;

                        case R.id.nav_home:
                            selectFragment = new NewsFragment();
                            break;

                        case R.id.nav_comment:
                            selectFragment = new CommentFragment();
                            break;
                    }

                    if (selectFragment != null) {
                        final int commit = getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectFragment).commit();
                    }
                    return true;
                }
            };


}
