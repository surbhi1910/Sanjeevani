package com.example.andromeda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DrawerLayout drawerLayout;
    private FirebaseAuth.AuthStateListener authStateListener;
    private static final String Locale_Preference = "Locale Preference";
    private static final String Locale_KeyValue = "Saved Locale";
    private static SharedPreferences.Editor editor;

    @Override
    protected void onStart() {
        super.onStart();
        loadLocale();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    finish();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }

            }
        };
        boolean first_run = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("first_run", true);
        if (first_run) {
            language_dialog();
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("first_run", false).apply();
        }
        SharedPreferences sharedPreferences = getSharedPreferences(Locale_Preference, MainActivity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView nav_view = findViewById(R.id.nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new home()).commit();

        nav_view.setCheckedItem(R.id.home);

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId())
                {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new home()).commit();
                        break;

                    case R.id.faqs:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Faqs()).commit();
                        break;

                    case R.id.about_us:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new about_us()).commit();
                        break;

                    case R.id.in_the_know:
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new In_the_know()).commit();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        View headerView = nav_view.getHeaderView(0);
        TextView identity = headerView.findViewById(R.id.identity);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String set = firebaseUser != null ? firebaseUser.getEmail() : null;
        identity.setText(set);
    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout){
            mAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }
        else if(item.getItemId() == R.id.choose_language){
            language_dialog();
        }
        return super.onOptionsItemSelected(item);
    }
    public void language_dialog(){
        final Dialog dialog =  new Dialog(this);
        dialog.setTitle(R.string.choose_language);
        dialog.setContentView(R.layout.languages);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        Objects.requireNonNull(dialog.getWindow()).setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        TextView hindi, english, marathi, tamil;
        hindi = dialog.findViewById(R.id.hi);
        english = dialog.findViewById(R.id.en);
        marathi = dialog.findViewById(R.id.mr);
        tamil = dialog.findViewById(R.id.bn);
        dialog.show();

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "hi").apply();
                String lang = "hi";
                changeLocale(lang);
                //recreate();
                dialog.dismiss();
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "en").apply();
                String lang = "en";
                changeLocale(lang);
                //recreate();
                dialog.dismiss();
            }
        });

        marathi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "mr").apply();
                String lang = "mr";
                changeLocale(lang);
                //recreate();
                dialog.dismiss();
            }
        });

        tamil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("LANG", "bn").apply();
                String lang = "bn";
                changeLocale(lang);
                //recreate();
                dialog.dismiss();
            }
        });

    }
    public void changeLocale(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        Locale myLocale = new Locale(lang);
        editor.putString(Locale_KeyValue, lang);
        editor.commit();
        Locale.setDefault(myLocale);
        Configuration config = getBaseContext().getResources().getConfiguration();
        config.setLocale(myLocale);
        //getBaseContext().createConfigurationContext(config);
        //config.locale = myLocale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //this.setContentView(R.layout.activity_main);
        //updateTexts();
        recreate();
    }

    public void loadLocale() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration config = getBaseContext().getResources().getConfiguration();

        String lang = settings.getString("LANG", "");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (! "".equals(lang) && ! config.getLocales().get(0).getLanguage().equals(lang)) {
                Locale locale = new Locale(lang);
                Locale.setDefault(locale);
                config.setLocale(locale);
                getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                recreate();
            }
        }
    }
}
