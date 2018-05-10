package com.ragazm.bakingapp.bakingapp.api;

import com.ragazm.bakingapp.bakingapp.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Andris on 011 11.04.18.
 */
public interface APIMethods {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipe();

}
