package com.ragazm.bakingapp.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.ragazm.bakingapp.bakingapp.model.Preferences;
import com.ragazm.bakingapp.bakingapp.model.Recipe;

/**
 * Created by Andris on 007 07.05.18.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return new WidgetDataProvider(getApplicationContext());
    }

    public static void updateWidget(Context context, Recipe recipe) {
        Preferences.saveRecipe(context, recipe);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, BackingWidget.class));
        BackingWidget.updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }
}