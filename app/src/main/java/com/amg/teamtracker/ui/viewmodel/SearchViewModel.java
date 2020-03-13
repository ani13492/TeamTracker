package com.amg.teamtracker.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.amg.teamtracker.api.ApiService;
import com.amg.teamtracker.data.model.Team;
import com.amg.teamtracker.data.model.Teams;
import com.amg.teamtracker.di.AppComponent;
import com.amg.teamtracker.di.AppModule;
import com.amg.teamtracker.di.DaggerAppComponent;

import javax.inject.Inject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {

    private AppComponent appComponent;
    @Inject
    public ApiService apiService;

    public SearchViewModel(@NonNull Application application) {
        super(application);
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(application)).build();
        appComponent.inject(this);
    }

    private final MutableLiveData<Teams> teamsMutableLiveData = new MutableLiveData<>();

    public void setTeams(Teams teams) {
        teamsMutableLiveData.setValue(teams);
    }

    public LiveData<Teams> getTeams() {
        return teamsMutableLiveData;
    }

    public void searchTeam(String teamName)    {
        MutableLiveData<Teams> teams = new MutableLiveData<>();
        Call<Teams> response = apiService.searchTeam(teamName);
        response.enqueue(new Callback<Teams>() {
            @Override
            public void onResponse(Call<Teams> call, Response<Teams> response) {
                if(response.isSuccessful() && response.body() != null)  {
                    setTeams(response.body());
                }
                else {
                    Toasty.error(getApplication(),"Error").show();
                }
            }

            @Override
            public void onFailure(Call<Teams> call, Throwable t) {

            }
        });
    }
}
