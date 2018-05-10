package com.ragazm.bakingapp.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ragazm.bakingapp.bakingapp.model.Ingredient;
import com.ragazm.bakingapp.bakingapp.model.Recipe;
import com.ragazm.bakingapp.bakingapp.model.Step;
import com.ragazm.bakingapp.bakingapp.widget.WidgetService;

import java.util.List;


/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link IngredientDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class IngredientListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    private boolean mTwoPane;
    private static Recipe recipe;
    private List<Step> steps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);


        recipe = new Recipe();


        Intent incomingIntent = getIntent();
        Bundle bundle = incomingIntent.getBundleExtra("recipeExtra");
        recipe =  bundle.getParcelable("recipe");

        Log.w("Bundle ",recipe.toString());
        steps = recipe.getSteps();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle(getTitle());


        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(getApplicationContext(),this, steps, mTwoPane));
    }

    /**
     * Link for reference, populate recyclerview rows with different datasets
     * https://stackoverflow.com/questions/36567782/recyclerview-with-2-lists-in-one-view
     *
     * SimpleItemRecyclerViewAdapter is stock adapter from AS template
     */
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private final int TITLE_TYPE = 0;
        private final int INGREDIENT_TYPE = 1;
        private final int STEP_TYPE = 2;

        private final IngredientListActivity mParentActivity;
        private final List<Step> mValues;
        private final boolean mTwoPane;
        private Context context;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Step item = (Step) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(IngredientDetailFragment.ARG_ITEM_ID, item);
                    IngredientDetailFragment fragment = new IngredientDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, IngredientDetailActivity.class);
                    intent.putExtra(IngredientDetailFragment.ARG_ITEM_ID, item);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(Context context, IngredientListActivity parent,
                                      List<Step> items,
                                      boolean twoPane) {
            this.context = context;
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public int getItemViewType(int position) {
            switch (position) {
                case 0:
                case 2:
                    return TITLE_TYPE;
                case 1:
                    return INGREDIENT_TYPE;
                default:
                    return STEP_TYPE;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            switch (viewType) {
                case TITLE_TYPE:
                    return getTitleViewHolder(context, parent);
                case INGREDIENT_TYPE:
                    return getIngredientViewHolder(context, parent);
                default:
                    return getStepViewHolder(context, parent);
            }
        }
        private TitleViewHolder getTitleViewHolder(Context context, ViewGroup viewGroup) {

            int layoutIdForListItem = R.layout.title_row;

            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

            return new TitleViewHolder(view);
        }

        private IngredientViewHolder getIngredientViewHolder(Context context, ViewGroup viewGroup) {

            int layoutIdForListItem = R.layout.recipe_detail_ingredient_cardview_item;

            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

            return new IngredientViewHolder(view);
        }

        private StepViewHolder getStepViewHolder(Context context, ViewGroup viewGroup) {

            int layoutIdForListItem = R.layout.recipe_list_content;

            LayoutInflater inflater = LayoutInflater.from(context);

            View view = inflater.inflate(layoutIdForListItem, viewGroup, false);

            return new StepViewHolder(view);
        }



        @Override
        public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case TITLE_TYPE:
                    TitleViewHolder titleViewHolder = (TitleViewHolder) holder;

                    String title = context.getString(position == 0 ? R.string.ingredients : R.string.steps);

                    titleViewHolder.textIngredientsTitle.setText(title);
                    break;
                case INGREDIENT_TYPE:
                    break;
                case STEP_TYPE:
                    int offsetPosition = position - 3;

                    StepViewHolder stepViewHolder = (StepViewHolder) holder;

                    Step step = recipe.getSteps().get(offsetPosition);

                    stepViewHolder.mIdView.setText(step.getShortDescription());


                    holder.itemView.setTag(mValues.get(offsetPosition));

                    holder.itemView.setOnClickListener(mOnClickListener);

                    break;
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class StepViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;


            StepViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.id_text);

            }
        }

        class TitleViewHolder extends RecyclerView.ViewHolder {
            TextView textIngredientsTitle;

            private TitleViewHolder(View itemView) {
                super(itemView);
                textIngredientsTitle = itemView.findViewById(R.id.titleIngredient);

            }

        }
        class IngredientViewHolder extends RecyclerView.ViewHolder {


            LinearLayout linearLayout;

            private IngredientViewHolder(View itemView) {
                super(itemView);
                linearLayout = itemView.findViewById(R.id.ingredients_container);
                appendIngredients(linearLayout);
            }

            void appendIngredients(LinearLayout parentLayout) {
                LayoutInflater inflater = LayoutInflater.from(context);
                View view;
                for (Ingredient ingredient : recipe.getIngredients()) {

                    view = inflater.inflate(R.layout.recipe_detail_ingredient_item, parentLayout, false);

                    TextView ingredientText = view.findViewById(R.id.ingredient);

                    TextView quantityMeasureText = view.findViewById(R.id.quantity_measure);

                    ingredientText.setText(ingredient.getIngredient());

                    quantityMeasureText.setText(String.format("%s %s", ingredient.getQuantity(), ingredient.getMeasure()));

                    parentLayout.addView(view);
                }
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_name) {
            WidgetService.updateWidget(this, recipe);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
