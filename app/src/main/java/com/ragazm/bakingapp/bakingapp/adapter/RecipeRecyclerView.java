package com.ragazm.bakingapp.bakingapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ragazm.bakingapp.bakingapp.IngredientListActivity;
import com.ragazm.bakingapp.bakingapp.R;
import com.ragazm.bakingapp.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Andris on 011 11.04.18.
 */
public class RecipeRecyclerView extends RecyclerView.Adapter<RecipeRecyclerView.ViewHolder> {
    private ArrayList<Recipe> recipes;
    private Context context;



    public RecipeRecyclerView(ArrayList<Recipe> recipes, Context context) {
        this.recipes = recipes;
        this.context = context;
    }

    @NonNull
    @Override
    public RecipeRecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeRecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Recipe recipe = recipes.get(position);
        String imageUrl = recipes.get(position).getImage();
        Uri builtUri = Uri.parse(imageUrl).buildUpon().build();

        Picasso.with(context)
                .load(builtUri)
                .into(holder.imageRecipe);

        holder.textRecipe.setText(recipe.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable("recipe", recipes.get(position));
                Log.w("recipe", recipes.get(position).toString());
             Intent intent = new Intent(context, IngredientListActivity.class);
             intent.putExtra("recipeExtra",bundle );
             context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageRecipe;
        TextView textRecipe;

        public ViewHolder(View itemView) {
            super(itemView);
            imageRecipe = itemView.findViewById(R.id.imageRecipe);
            textRecipe = itemView.findViewById(R.id.textRecipe);

            }
    }
}
