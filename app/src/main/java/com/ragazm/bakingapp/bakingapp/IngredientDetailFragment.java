package com.ragazm.bakingapp.bakingapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.ragazm.bakingapp.bakingapp.model.Step;
import com.squareup.picasso.Picasso;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link IngredientListActivity}
 * in two-pane mode (on tablets) or a {@link IngredientDetailActivity}
 * on handsets.
 */
public class IngredientDetailFragment extends Fragment implements ExoPlayer.EventListener{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "";
    public static final String STEP_KEY = "step_key";
    private static final String POSITION_KEY = "pos_key";
    private static final String PLAY_WHEN_READY_KEY = "play_when_ready_key";

    /**
     * The dummy content this fragment is presenting.
     */
    private Step mItem;
    private ExoPlayer player;
    private long mCurrentPosition = 0;
    private boolean mPlayWhenReady = true;
    public SimpleExoPlayerView playerView;
    public Context context;
    private boolean fullScreenMode;
    private long videoLastPosition;



    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public IngredientDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Bundle bundle = this.getArguments();


           mItem = bundle.getParcelable(IngredientDetailFragment.ARG_ITEM_ID );

          Activity activity = this.getActivity();
           CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
           if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.shortDescription);
           }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredient_detail, container, false);
        ImageView thumbnailImage = rootView.findViewById(R.id.thumbnailImage);
        playerView = rootView.findViewById(R.id.video_player);

        if (savedInstanceState != null && savedInstanceState.containsKey(POSITION_KEY)) {
            mCurrentPosition = savedInstanceState.getLong(POSITION_KEY);
            mPlayWhenReady = savedInstanceState.getBoolean(PLAY_WHEN_READY_KEY);
        }

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(mItem.description);
        }

        assert mItem != null;
        if(!mItem.getThumbnailURL().isEmpty()){

            Picasso.with(getContext())
                    .load(mItem.getThumbnailURL())
                    .into(thumbnailImage);

            thumbnailImage.setVisibility(View.VISIBLE);
        }
     //   initializePlayer(mItem.videoURL);

        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putLong(POSITION_KEY, mCurrentPosition);
        outState.putBoolean(PLAY_WHEN_READY_KEY, mPlayWhenReady);
        super.onSaveInstanceState(outState);


    }

    /**
     * https://stackoverflow.com/questions/41848293/google-exoplayer-guide
     *
     */
    private void initializePlayer(String path) {
        if (player == null && !path.equals("")) {
            // 1. Create a default TrackSelector
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector =
                    new DefaultTrackSelector(videoTrackSelectionFactory);

            // 2. Create a default LoadControl
            LoadControl loadControl = new DefaultLoadControl();
            // 3. Create the player
            player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);


            playerView.setPlayer(player);
            playerView.setKeepScreenOn(true);
            playerView.setVisibility(View.VISIBLE);
            // Produces DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), getString(R.string.app_name)), bandwidthMeter);

            // Produces Extractor instances for parsing the media data.
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // This is the MediaSource representing the media to be played.
            MediaSource videoSource = new ExtractorMediaSource(Uri.parse(path),
                    dataSourceFactory, extractorsFactory, null, null);
            // Prepare the player with the source.

            player.addListener(this);
            player.prepare(videoSource);
            playerView.requestFocus();
            player.setPlayWhenReady(true);      // to play video when ready. Use false to pause a video
        }
    }



    @Override
    public void onDestroy() {

        if(player != null){
            mCurrentPosition = player.getCurrentPosition();
            player.release();
        }
        super.onDestroy();
          //it is important to release a player
    }

    @Override
    public void onPause() {

        if (player != null) {
            mPlayWhenReady = player.getPlayWhenReady();
            mCurrentPosition = player.getCurrentPosition();

            player.stop();
            player.release();
            player = null;
        }
        super.onPause();
    }
    @Override
    public void onStop(){

        if (player != null) {
            mPlayWhenReady = player.getPlayWhenReady();
            mCurrentPosition = player.getCurrentPosition();
            player.stop();
            player.release();
            player = null;
        }
        super.onStop();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        // show user that something went wrong. I am showing dialog but you can use your way
        AlertDialog.Builder adb = new AlertDialog.Builder(this.context);
        adb.setTitle("Could not able to stream video");
        adb.setMessage("It seems that something is going wrong.\nPlease try again.");
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog ad = adb.create();
        ad.show();

    }
    @Override
    public void onResume() {

        initializePlayer(mItem.videoURL);
        if (mCurrentPosition != 0) {
            player.seekTo(mCurrentPosition);

        }
        super.onResume();
    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }




}
