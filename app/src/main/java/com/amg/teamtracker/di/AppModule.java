package com.amg.teamtracker.di;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.amg.teamtracker.R;
import com.amg.teamtracker.api.ApiService;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class AppModule {

    private final Application application;

    public AppModule(final @NonNull Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return application.getApplicationContext();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(Context context) {
        return new Retrofit.Builder().baseUrl(context.getResources().getString(R.string.base_url))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient()).build();
    }

    @Provides
    @Singleton
    ApiService provideApiService(final Retrofit retrofit) {
        return retrofit.create(ApiService.class);
    }

}
