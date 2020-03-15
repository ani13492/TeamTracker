package com.amg.teamtracker.api;

import com.amg.teamtracker.data.model.Results;
import com.amg.teamtracker.data.model.Teams;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    // Get teams based on search query
    @GET("searchteams.php")
    Call<Teams> searchTeam(@Query("t") String team);

    // Get last 5 game results of team {id}
    @GET("eventslast.php")
    Call<Results> getTeamHistory(@Query("id") String id);
}
