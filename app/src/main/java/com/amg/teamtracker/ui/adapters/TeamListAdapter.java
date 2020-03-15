package com.amg.teamtracker.ui.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.amg.teamtracker.R;
import com.amg.teamtracker.data.model.Results;
import com.amg.teamtracker.data.model.Team;
import com.amg.teamtracker.data.model.Teams;
import com.amg.teamtracker.ui.activities.MainActivity;
import com.amg.teamtracker.ui.fragments.TeamHistoryFragment;
import com.amg.teamtracker.ui.viewmodel.SearchViewModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import static com.amg.teamtracker.utils.Constants.MIN_DEBOUNCE_DELAY;
import static com.amg.teamtracker.utils.Constants.TEAM_BADGE_IMAGE_URL;
import static com.amg.teamtracker.utils.Constants.TEAM_HISTORY_OBJECT;
import static com.amg.teamtracker.utils.Constants.TEAM_NAME;
import static com.amg.teamtracker.utils.Constants.TEAM_STADIUM_IMAGE_URL;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListViewHolder> {

    private Teams teams;
    private Context context;
    private LifecycleOwner lifecycleOwner;
    private SearchViewModel viewModel;

    public TeamListAdapter(Context context, Teams teams, SearchViewModel viewModel, LifecycleOwner lifecycleOwner) {
        this.context = context;
        this.teams = teams;
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public TeamListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.team_item,parent,false);
        return new TeamListViewHolder(view,context,teams,viewModel,lifecycleOwner);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamListViewHolder holder, int position) {
        Team team = teams.getTeams().get(position);
        holder.bindTeam(team);
    }

    @Override
    public int getItemCount() {
        if(teams != null && teams.getTeams() != null && teams.getTeams().size() > 0)    {
            return teams.getTeams().size();
        }
        return 0;
    }
}

class TeamListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Teams teams;
    private TextView teamName, teamSport;
    private ImageView teamImage;
    private SearchViewModel viewModel;
    private Context context;
    private LifecycleOwner lifecycleOwner;
    private long mLastClickTime;

    public TeamListViewHolder(@NonNull View itemView,Context context, Teams teams, SearchViewModel viewModel, LifecycleOwner lifecycleOwner) {
        super(itemView);
        this.context = context;
        this.teams = teams;
        this.teamName  = itemView.findViewById(R.id.team_name);
        this.teamSport = itemView.findViewById(R.id.team_sport);
        this.teamImage = itemView.findViewById(R.id.team_image);
        this.viewModel = viewModel;
        this.lifecycleOwner = lifecycleOwner;
        itemView.setOnClickListener(this);
    }

    public void bindTeam(Team team) {
        if(team != null)    {
            teamName.setText(team.getStrTeam());
            teamSport.setText(team.getStrSport());
            Picasso.with(teamImage.getContext()).load(team.getStrTeamBadge()).into(teamImage);
        }
    }

    @Override
    public void onClick(View v) {
        // Handle multiple consecutive taps
        long lastClickTime = mLastClickTime;
        long now = System.currentTimeMillis();
        mLastClickTime = now;
        int index = -1;
        index = getAdapterPosition();
        if(now - lastClickTime > MIN_DEBOUNCE_DELAY) {
            Team chosenTeam = teams.getTeams().get(index);
            if (context instanceof MainActivity) {
                viewModel.getTeamHistory(chosenTeam.getIdTeam());
                viewModel.getTeamHistory().observe(lifecycleOwner, new Observer<Results>() {
                    @Override
                    public void onChanged(Results results) {
                        if(results != null && results.getResults() != null && results.getResults().size() > 0) {
                            viewModel.setTeamHistory(null);
                            Bundle bundle = new Bundle();
                            bundle.putString(TEAM_STADIUM_IMAGE_URL,chosenTeam.getStrStadiumThumb());
                            bundle.putString(TEAM_BADGE_IMAGE_URL,chosenTeam.getStrTeamBadge());
                            bundle.putString(TEAM_NAME,chosenTeam.getStrTeam());
                            bundle.putSerializable(TEAM_HISTORY_OBJECT,new Gson().toJson(results));
                            ((MainActivity)context).replaceFragment(new TeamHistoryFragment(),bundle);
                        }
                    }
                });
            }
        }
    }
}
