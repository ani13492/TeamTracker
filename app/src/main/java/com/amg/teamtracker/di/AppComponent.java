package com.amg.teamtracker.di;

import com.amg.teamtracker.ui.activities.MainActivity;
import com.amg.teamtracker.ui.viewmodel.SearchViewModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(SearchViewModel searchViewModel);
    void inject(MainActivity mainActivity);
}
