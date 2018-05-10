package com.ragazm.bakingapp.bakingapp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ragazm.bakingapp.bakingapp.adapter.RecipeRecyclerView;
import com.ragazm.bakingapp.bakingapp.api.APIClient;
import com.ragazm.bakingapp.bakingapp.api.APIMethods;
import com.ragazm.bakingapp.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public ArrayList<Recipe> res;
    private RecyclerView recyclerView;
    private APIMethods apiMethods;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private RecipeRecyclerView adapter;
    @Nullable
    private IdlingResources mIdlingResource;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView = findViewById(R.id.recipe_list_recyclerview);
        getIdlingResource();
        loadData();

    }

    private void loadData(){

        apiMethods = APIClient.getClient().create(APIMethods.class);
        Call<ArrayList<Recipe>> call = apiMethods.getRecipe();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override

            public void onResponse(@NonNull Call<ArrayList<Recipe>> call,
                                   @NonNull Response<ArrayList<Recipe>> response) {
                ArrayList<Recipe> recipes = response.body();
                if(response.isSuccessful()) {
                    adapter = new RecipeRecyclerView(recipes,MainActivity.this);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(adapter);

                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Connection error!", Toast.LENGTH_SHORT).show();


            }
        });
    }

    @VisibleForTesting
    @NonNull
    public IdlingResources getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new IdlingResources();
        }
        return mIdlingResource;
    }
}
