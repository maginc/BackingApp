package com.ragazm.bakingapp.bakingapp.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.ragazm.bakingapp.bakingapp.model.Preferences;
import com.ragazm.bakingapp.bakingapp.R;
import com.ragazm.bakingapp.bakingapp.model.Recipe;


/**
 * Created by Andris on 007 07.05.18.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private Recipe recipe;

    public WidgetDataProvider(Context context) {
        mContext = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        recipe = Preferences.loadRecipe(mContext);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
       return(recipe.getIngredients().size());
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

       row.setTextViewText(R.id.txtIngredient, recipe.getIngredients().get(position).getIngredient());
        row.setTextViewText(R.id.txtQuantity, recipe.getIngredients().get(position).getQuantity().toString());

        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}