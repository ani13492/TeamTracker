package com.amg.teamtracker.ui.fragments;


import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amg.teamtracker.R;
import com.amg.teamtracker.data.model.Results;
import com.amg.teamtracker.ui.activities.MainActivity;
import com.amg.teamtracker.ui.adapters.TeamHistoryAdapter;
import com.amg.teamtracker.ui.viewmodel.SearchViewModel;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static android.view.View.GONE;
import static com.amg.teamtracker.utils.Constants.TEAM_BADGE_IMAGE_URL;
import static com.amg.teamtracker.utils.Constants.TEAM_HISTORY_OBJECT;
import static com.amg.teamtracker.utils.Constants.TEAM_NAME;
import static com.amg.teamtracker.utils.Constants.TEAM_STADIUM_IMAGE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamHistoryFragment extends Fragment {

    private SearchViewModel viewModel;
    private RecyclerView teamHistoryList;
    private ImageView teamStadiumImage;

    public TeamHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        return inflater.inflate(R.layout.fragment_team_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup Recyclerview
        teamHistoryList = view.findViewById(R.id.team_history_list);
        teamHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        teamHistoryList.setHasFixedSize(true);
        viewModel.animateRecyclerView(teamHistoryList);



        teamStadiumImage = view.findViewById(R.id.team_stadium_image);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(TEAM_HISTORY_OBJECT)) {
            if(bundle.getString(TEAM_STADIUM_IMAGE_URL) != null) {
                Picasso.with(teamStadiumImage.getContext()).load(getArguments().getString(TEAM_STADIUM_IMAGE_URL)).into(teamStadiumImage);
            }
            else {
                teamStadiumImage.setVisibility(GONE);
                teamHistoryList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }
            loadActionBar();

            Results teamHistoryResults = new Gson().fromJson(getArguments().getSerializable(TEAM_HISTORY_OBJECT).toString(), Results.class);
            TeamHistoryAdapter adapter = new TeamHistoryAdapter(getContext(), teamHistoryResults);
            teamHistoryList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void loadActionBar()  {
        if(getActivity() != null && ((MainActivity)getActivity()).getSupportActionBar() != null)    {
            ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
            Bundle bundle = getArguments();
            if(actionBar != null && bundle != null && bundle.containsKey(TEAM_NAME)) {
                actionBar.setTitle(getArguments().getString(TEAM_NAME)+"- Match History");
            }
        }
    }
}
