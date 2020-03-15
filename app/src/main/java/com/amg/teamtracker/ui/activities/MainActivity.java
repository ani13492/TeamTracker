package com.amg.teamtracker.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.widget.ProgressBar;
import com.amg.teamtracker.R;
import com.amg.teamtracker.data.model.Team;
import com.amg.teamtracker.ui.viewmodel.SearchViewModel;
import com.amg.teamtracker.utils.Constants;

import static com.amg.teamtracker.utils.Constants.TEAM_BADGE_IMAGE_URL;
import static com.amg.teamtracker.utils.Constants.TEAM_STADIUM_IMAGE_URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void replaceFragment(Fragment next, Bundle bundle)    {
        FragmentManager manager = getSupportFragmentManager();
        // Create new fragment and transaction
        FragmentTransaction transaction = manager.beginTransaction();
        // Replace Fragement and add old fragment to back stack
        if(manager.findFragmentByTag(next.getTag()) == null) {
            next.setArguments(bundle);
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            transaction.replace(R.id.fragment_container, next);
            transaction.addToBackStack(null);
            // Commit the transaction
            transaction.commit();
        }
    }

}
