package com.example.killers.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.killers.Adapter;
import com.example.killers.ApiClient;
import com.example.killers.MainActivity;
import com.example.killers.Model.Articles;
import com.example.killers.Model.Headlines;
import com.example.killers.R;
import com.example.killers.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class NewsFragment extends Fragment {


    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    final String API_KEY = "a0b0c9382557410a9734e063fbe0cd5f";
    Adapter adapter;
    List<Articles> articles = new ArrayList<>();

    Button btnLogout;
    SessionManager sessionManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.newsfragment, container, false);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefresh); //потяни чтобы обновить
        recyclerView = view.findViewById(R.id.recyclerView);

        btnLogout = view.findViewById(R.id.btnLogout);
        sessionManager = new SessionManager(getContext().getApplicationContext());

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("ВЫЙТИ");
                builder.setMessage("Вы точно хотите выйти?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessionManager.setLogin(false);
                        sessionManager.setEmail("");
                        startActivity(new Intent(getContext().getApplicationContext(), MainActivity.class));
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        String country = getCountry();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            retrieveJson(country,API_KEY);
        });
        retrieveJson(country, API_KEY);

        return view;
    }


    public void retrieveJson(String country, String apiKey) {

        swipeRefreshLayout.setRefreshing(true);
        Call<Headlines> call = ApiClient.getInstance().getApi().getHeadlines(country, apiKey);
        call.enqueue(new Callback<Headlines>() {
            @Override
            public void onResponse(Call<Headlines> call, Response<Headlines> response) {
                if (response.isSuccessful() && response.body().getArticles() != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    articles.clear();
                    articles = response.body().getArticles();
                    adapter = new Adapter(getContext(), articles, NewsFragment.this);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<Headlines> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText((Context) getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public String getCountry() {
        Locale locale = Locale.getDefault();
        String country = locale.getCountry();
        return country.toLowerCase();
    }

}
