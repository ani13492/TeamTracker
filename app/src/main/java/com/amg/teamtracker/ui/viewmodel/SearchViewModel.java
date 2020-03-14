package com.amg.teamtracker.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.amg.teamtracker.api.ApiService;
import com.amg.teamtracker.data.model.Results;
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
    private final MutableLiveData<Results> teamHistoryMutableLiveData = new MutableLiveData<>();

    public void setTeams(Teams teams) {
        teamsMutableLiveData.setValue(teams);
    }

    public LiveData<Teams> getTeams() {
        return teamsMutableLiveData;
    }

    public void setTeamHistory(Results results) { teamHistoryMutableLiveData.setValue(results); }

    public LiveData<Results> getTeamHistory()   { return teamHistoryMutableLiveData; }


    /**
     * Get a list of all teams that match the user provided team name
     * @param teamName
     */
    public void searchTeam(String teamName)    {
        Call<Teams> enteredTeam = apiService.searchTeam(teamName);
        enteredTeam.enqueue(new Callback<Teams>() {
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

    /**
     * Get Team history of team based on team's id
     * @param teamId
     */
    public void getTeamHistory(String teamId)   {
        Call<Results> teamHistory = apiService.getTeamHistory(teamId);
        teamHistory.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if(response.isSuccessful() &&  response.body() != null) {
                    setTeamHistory(response.body());
                }
                else {
                    Toasty.error(getApplication(),"Error").show();
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {

            }
        });
    }
}
