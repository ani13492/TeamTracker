package com.amg.teamtracker.api;

import com.amg.teamtracker.data.model.Teams;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("searchteams.php")
    Call<Teams> searchTeam(@Query("t") String team);
}
