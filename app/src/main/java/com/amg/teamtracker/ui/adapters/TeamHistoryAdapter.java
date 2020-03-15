package com.amg.teamtracker.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.amg.teamtracker.R;
import com.amg.teamtracker.data.model.Result;
import com.amg.teamtracker.data.model.Results;

public class TeamHistoryAdapter extends RecyclerView.Adapter<TeamHistoryViewHolder> {

    private Results teamHistoryResults;
    private Context context;

    public TeamHistoryAdapter(Context context, Results teamHistoryResults) {
        this.context = context;
        this.teamHistoryResults = teamHistoryResults;
    }

    @NonNull
    @Override
    public TeamHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.team_history_item,parent,false);
        return new TeamHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamHistoryViewHolder holder, int position) {
        Result result = teamHistoryResults.getResults().get(position);
        holder.bindResult(result);
    }

    @Override
    public int getItemCount() {
        if(teamHistoryResults != null && teamHistoryResults.getResults() != null && teamHistoryResults.getResults().size() > 0) {
            return teamHistoryResults.getResults().size();
        }
        return 0;
    }
}

class TeamHistoryViewHolder extends RecyclerView.ViewHolder {

    private TextView leagueName, eventDate, eventName, eventScore, homeScorers, awayScorers;

    public TeamHistoryViewHolder(@NonNull View itemView) {
        super(itemView);
        this.leagueName = itemView.findViewById(R.id.league_name);
        this.eventDate = itemView.findViewById(R.id.event_date);
        this.eventScore = itemView.findViewById(R.id.event_score);
        this.homeScorers = itemView.findViewById(R.id.home_scorers);
        this.awayScorers = itemView.findViewById(R.id.away_scorers);
        this.eventName = itemView.findViewById(R.id.event_name);
    }

    public void bindResult(Result result)   {
        if(result != null)  {
            if(result.getStrLeague() != null)   leagueName.setText(result.getStrLeague());
            if(result.getStrDate() != null)   eventDate.setText(result.getDateEvent());
            if(result.getStrEvent() != null)   eventName.setText(result.getStrEvent());
            if(result.getEventScore() != null)   eventScore.setText(result.getEventScore());
            if(result.getHomeGoalDetails() != null)   homeScorers.setText(result.getHomeGoalDetails());
            if(result.getAwayGoalDetails() != null)   awayScorers.setText(result.getAwayGoalDetails());
        }
    }
}
