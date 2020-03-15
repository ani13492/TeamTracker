package com.amg.teamtracker.ui.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amg.teamtracker.R;
import com.amg.teamtracker.data.model.Team;
import com.amg.teamtracker.data.model.Teams;
import com.amg.teamtracker.ui.activities.MainActivity;
import com.amg.teamtracker.ui.adapters.TeamListAdapter;
import com.amg.teamtracker.ui.viewmodel.SearchViewModel;
import com.amg.teamtracker.utils.SystemUtils;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.amg.teamtracker.utils.Constants.TEAM_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    private SearchViewModel viewModel;
    private RecyclerView teamList;
    private EditText searchEditText;
    ProgressBar progressBar;
    private TextView fillerText;
    private int shortAnimationDuration;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        viewModel.getTeams().removeObservers(this);
        viewModel.getTeams().observe(getViewLifecycleOwner(), new Observer<Teams>() {
            @Override
            public void onChanged(Teams teams) {
                TeamListAdapter adapter = new TeamListAdapter(getContext(),teams,viewModel,getViewLifecycleOwner());
                viewModel.animateRecyclerView(teamList);
                teamList.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
        return inflater.inflate(R.layout.fragment_search, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadActionBar();
        // Search EditText
        searchEditText = view.findViewById(R.id.search_team_edittext);
        setupSearchEditText(searchEditText);

        // Setup Recyclerview
        teamList = view.findViewById(R.id.team_list);
        teamList.setLayoutManager(new LinearLayoutManager(getContext()));
        teamList.setHasFixedSize(true);

        // ProgressBar
        progressBar = view.findViewById(R.id.progress_bar);
        fillerText = view.findViewById(R.id.filler_text);
        shortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

    }

    @SuppressLint({"CheckResult", "ClickableViewAccessibility"})
    private void setupSearchEditText(EditText searchEditText) {

        // Clear edittext when user click on the cancel icon
        searchEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_RIGHT = 2;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (searchEditText.getRight() - searchEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        searchEditText.setText("");
                        teamList.setAdapter(null);
                        return true;
                    }
                }
                return false;
            }
        });

        // Setup debounce observable
        Observable<String> searchTeamObservable = RxTextView.textChanges(searchEditText)
                .skipInitialValue()
                .map(CharSequence::toString)
                .debounce(900, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        searchTeamObservable.subscribe(team -> {
            if(team != null && !team.isEmpty()) {
                // ProgressBar logic
                viewModel.isLoading().observe(requireActivity(), new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean isLoading) {
                        if(isLoading)   {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
                SystemUtils.hideSoftKeyboard(requireActivity(), searchEditText);
                viewModel.searchTeam(team);
            }
        }, error -> Log.d("Error",error.getMessage()));

    }

    private void loadActionBar()  {
        if(((MainActivity)getActivity()) != null && ((MainActivity)getActivity()).getSupportActionBar() != null)    {
            ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
            actionBar.setTitle(getResources().getString(R.string.app_name));
        }
    }
}
