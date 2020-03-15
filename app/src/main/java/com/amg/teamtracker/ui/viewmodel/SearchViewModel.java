package com.amg.teamtracker.ui.viewmodel;

import android.app.Application;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.amg.teamtracker.api.ApiService;
import com.amg.teamtracker.data.model.Results;
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
    final private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
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
        isLoading.setValue(true);
        Call<Teams> enteredTeam = apiService.searchTeam(teamName);
        enteredTeam.enqueue(new Callback<Teams>() {
            @Override
            public void onResponse(Call<Teams> call, Response<Teams> response) {
                isLoading.setValue(false);
                if(response.isSuccessful() && response.body() != null)  {
                    if(response.body().getTeams() != null && response.body().getTeams().size() > 0) {
                        setTeams(response.body());
                    }
                    else {
                        Toasty.error(getApplication(),"No data found").show();
                    }
                }
                else {
                    Toasty.error(getApplication(),"Error").show();
                }
            }

            @Override
            public void onFailure(Call<Teams> call, Throwable t) {
                isLoading.setValue(false);
            }
        });
    }

    /**
     * Get Team history of team based on team's id
     * @param teamId
     */
    public void getTeamHistory(String teamId)   {
        isLoading.setValue(true);
        Call<Results> teamHistory = apiService.getTeamHistory(teamId);
        teamHistory.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                isLoading.setValue(false);
                if(response.isSuccessful() &&  response.body() != null && response.body().getResults() != null && response.body().getResults().size() > 0) {
                    setTeamHistory(response.body());
                }
                else {
                    Toasty.error(getApplication(),"No history available").show();
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                isLoading.setValue(false);
            }
        });
    }

    public LiveData<Boolean> isLoading()  {
        return isLoading;
    }

    public void animateRecyclerView(RecyclerView recyclerView) {
        if(recyclerView != null) {
            recyclerView.getViewTreeObserver().addOnPreDrawListener(
                    new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            recyclerView.getViewTreeObserver().removeOnPreDrawListener(this);

                            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                                View v = recyclerView.getChildAt(i);
                                v.setAlpha(0.0f);
                                v.animate().alpha(1.0f)
                                        .setDuration(500)
                                        .setStartDelay(i * 250)
                                        .start();
                            }
                            return true;
                        }
                    });
        }
    }
}
