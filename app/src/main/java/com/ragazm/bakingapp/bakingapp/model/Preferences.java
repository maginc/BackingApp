package com.ragazm.bakingapp.bakingapp.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.ragazm.bakingapp.bakingapp.R;

/**
 * Created by Andris on 010 10.05.18.
 */
public class Preferences {
    private static final String PREFERENCE_NAME = "preference";

    public static void saveRecipe(Context context, Recipe recipe) {
        SharedPreferences.Editor preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        preferences.putString(context.getString(R.string.recipe_key), Recipe.toBase64String(recipe));
        preferences.apply();
    }

    public static Recipe loadRecipe(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String recipeBase64 = preferences.getString(context.getString(R.string.recipe_key), "");
        return "".equals(recipeBase64) ? null : Recipe.fromBase64(preferences.getString(context.getString(R.string.recipe_key), ""));
    }
}