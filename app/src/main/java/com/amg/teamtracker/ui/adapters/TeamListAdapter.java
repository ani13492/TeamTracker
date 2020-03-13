package com.amg.teamtracker.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amg.teamtracker.R;
import com.amg.teamtracker.data.model.Team;
import com.amg.teamtracker.data.model.Teams;

import es.dmoral.toasty.Toasty;

public class TeamListAdapter extends RecyclerView.Adapter<TeamListViewHolder> {

    private Teams teams;
    private Context context;

    public TeamListAdapter(Context context, Teams teams) {
        this.context = context;
        this.teams = teams;
    }

    @NonNull
    @Override
    public TeamListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.team_item,parent,false);
        return new TeamListViewHolder(view,teams);
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
    private TextView teamName;
    private TextView teamSport;

    public TeamListViewHolder(@NonNull View itemView, Teams teams) {
        super(itemView);
        this.teams = teams;
        this.teamName  = itemView.findViewById(R.id.team_name);
        this.teamSport = itemView.findViewById(R.id.team_sport);
    }

    public void bindTeam(Team team) {
        if(team != null)    {
            teamName.setText(team.getStrTeam());
            teamSport.setText(team.getStrSport());
        }
    }

    @Override
    public void onClick(View v) {
        int index = getAdapterPosition();
        Team chosenTeam = teams.getTeams().get(index);
        Toasty.error(v.getContext(),chosenTeam.getStrCountry()).show();
    }
}
