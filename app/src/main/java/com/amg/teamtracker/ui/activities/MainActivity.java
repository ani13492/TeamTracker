package com.amg.teamtracker.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.amg.teamtracker.R;
import com.amg.teamtracker.api.ApiService;
import com.amg.teamtracker.data.model.Teams;
import com.amg.teamtracker.di.AppComponent;
import com.amg.teamtracker.di.AppModule;
import com.amg.teamtracker.di.DaggerAppComponent;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
