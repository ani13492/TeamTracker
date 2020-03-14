package com.amg.teamtracker.ui.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amg.teamtracker.R;
import com.amg.teamtracker.data.model.Results;
import com.amg.teamtracker.ui.adapters.TeamHistoryAdapter;
import com.amg.teamtracker.ui.viewmodel.SearchViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeamHistoryFragment extends Fragment {

    private SearchViewModel viewModel;
    private RecyclerView teamHistoryList;

    public TeamHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_team_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        viewModel.getTeamHistory("133602");


        // Setup Recyclerview
        teamHistoryList = view.findViewById(R.id.team_history_list);
        teamHistoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        teamHistoryList.setHasFixedSize(true);

        viewModel.getTeamHistory().observe(getViewLifecycleOwner(), new Observer<Results>() {
            @Override
            public void onChanged(Results results) {
                // Load recyclerview with results adapter
                TeamHistoryAdapter adapter = new TeamHistoryAdapter(getContext(),results);
                teamHistoryList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
