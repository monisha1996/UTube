package com.smedic.tubtub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.smedic.tubtub.MainActivity;
import com.smedic.tubtub.R;
import com.smedic.tubtub.adapters.VideosAdapter;
import com.smedic.tubtub.database.YouTubeSqlDb;
import com.smedic.tubtub.interfaces.ItemEventsListener;
import com.smedic.tubtub.interfaces.OnFavoritesSelected;
import com.smedic.tubtub.interfaces.OnItemSelected;
import com.smedic.tubtub.model.YouTubeVideo;
import com.smedic.tubtub.utils.Config;

import java.util.ArrayList;

public class RecentlyWatchedFragment extends BaseFragment implements
        ItemEventsListener<YouTubeVideo> {

    private static final String TAG = "SMEDIC RecentlyWatched";
    private ArrayList<YouTubeVideo> recentlyPlayedVideos;

    private RecyclerView recentlyPlayedListView;
    private VideosAdapter videoListAdapter;
    private OnItemSelected itemSelected;
    private OnFavoritesSelected onFavoritesSelected;
    private Context context;
    private RelativeLayout nothingFoundMessageHolder;

    public RecentlyWatchedFragment() {
        // Required empty public constructor
    }

    public static RecentlyWatchedFragment newInstance() {
        return new RecentlyWatchedFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recentlyPlayedVideos = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        recentlyPlayedListView = (RecyclerView) v.findViewById(R.id.fragment_list_items);
        recentlyPlayedListView.setLayoutManager(new LinearLayoutManager(context));
        videoListAdapter = new VideosAdapter(context, recentlyPlayedVideos);
        videoListAdapter.setOnItemEventsListener(this);
        recentlyPlayedListView.setAdapter(videoListAdapter);

        nothingFoundMessageHolder = (RelativeLayout) v.findViewById(R.id.nothing_found_holder);
        //disable swipe to refresh for this tab
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        recentlyPlayedVideos.clear();
        recentlyPlayedVideos.addAll(YouTubeSqlDb.getInstance().videos(YouTubeSqlDb.VIDEOS_TYPE.RECENTLY_WATCHED).readAll());
        updateList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.context = context;
            itemSelected = (MainActivity) context;
            onFavoritesSelected = (MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.context = null;
        itemSelected = null;
        onFavoritesSelected = null;
    }

    /**
     * Clears recently played list items
     */
    public void clearRecentlyPlayedList() {
        recentlyPlayedVideos.clear();
        updateList();
    }

    @Override
    public void onShareClicked(String itemId) {
        share(Config.SHARE_VIDEO_URL + itemId);
    }

    @Override
    public void onFavoriteClicked(YouTubeVideo video, boolean isChecked) {
        onFavoritesSelected.onFavoritesSelected(video, isChecked); // pass event to MainActivity
    }

    @Override
    public void onItemClick(YouTubeVideo video) {
        itemSelected.onPlaylistSelected(recentlyPlayedVideos, recentlyPlayedVideos.indexOf(video));
    }

    @Override
    public void updateList() {
        videoListAdapter.notifyDataSetChanged();
        if (videoListAdapter.getItemCount() > 0) {
            nothingFoundMessageHolder.setVisibility(View.GONE);
            recentlyPlayedListView.setVisibility(View.VISIBLE);
        } else {
            nothingFoundMessageHolder.setVisibility(View.VISIBLE);
            recentlyPlayedListView.setVisibility(View.GONE);
        }
    }
}
