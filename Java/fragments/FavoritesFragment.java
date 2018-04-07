package com.smedic.tubtub.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.smedic.tubtub.MainActivity;
import com.smedic.tubtub.R;
import com.smedic.tubtub.adapters.VideosAdapter;
import com.smedic.tubtub.database.YouTubeSqlDb;
import com.smedic.tubtub.interfaces.ItemEventsListener;
import com.smedic.tubtub.interfaces.OnItemSelected;
import com.smedic.tubtub.model.YouTubeVideo;
import com.smedic.tubtub.utils.Config;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends BaseFragment implements ItemEventsListener<YouTubeVideo> {

    private static final String TAG = "SMEDIC Favorites";
    private List<YouTubeVideo> favoriteVideos;

    private RecyclerView favoritesListView;
    private VideosAdapter videoListAdapter;
    private OnItemSelected itemSelected;
    private Context context;
    private RelativeLayout nothingFoundMessageHolder;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteVideos = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_list, container, false);
        favoritesListView = (RecyclerView) v.findViewById(R.id.fragment_list_items);
        favoritesListView.setLayoutManager(new LinearLayoutManager(context));

        videoListAdapter = new VideosAdapter(context, favoriteVideos);
        videoListAdapter.setOnItemEventsListener(this);
        favoritesListView.setAdapter(videoListAdapter);

        nothingFoundMessageHolder = (RelativeLayout) v.findViewById(R.id.nothing_found_holder);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        favoriteVideos.clear();
        favoriteVideos.addAll(YouTubeSqlDb.getInstance().videos(YouTubeSqlDb.VIDEOS_TYPE.FAVORITE).readAll());
        updateList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            this.itemSelected = (MainActivity) context;
            this.context = context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.itemSelected = null;
        this.context = null;
    }

    /**
     * Clears recently played list items
     */
    public void clearFavoritesList() {
        favoriteVideos.clear();
        updateList();
    }

    public void addToFavoritesList(YouTubeVideo video) {
        YouTubeSqlDb.getInstance().videos(YouTubeSqlDb.VIDEOS_TYPE.FAVORITE).create(video);
    }

    public void removeFromFavorites(YouTubeVideo video) {
        YouTubeSqlDb.getInstance().videos(YouTubeSqlDb.VIDEOS_TYPE.FAVORITE).delete(video.getId());
        favoriteVideos.remove(video);
        updateList();
    }

    @Override
    public void onShareClicked(String itemId) {
        share(Config.SHARE_VIDEO_URL + itemId);
    }

    @Override
    public void onFavoriteClicked(YouTubeVideo video, boolean isChecked) {
        if (isChecked) {
            addToFavoritesList(video);
        } else {
            removeFromFavorites(video);
        }
    }

    @Override
    public void onItemClick(YouTubeVideo video) {
        YouTubeSqlDb.getInstance().videos(YouTubeSqlDb.VIDEOS_TYPE.RECENTLY_WATCHED).create(video);
        itemSelected.onPlaylistSelected(favoriteVideos, favoriteVideos.indexOf(video));
    }

    @Override
    public void updateList() {
        Log.d(TAG, "updateList: ");
        videoListAdapter.notifyDataSetChanged();
        if (videoListAdapter.getItemCount() > 0) {
            Log.d(TAG, "updateList: show");
            nothingFoundMessageHolder.setVisibility(View.GONE);
            favoritesListView.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "updateList: hide");
            nothingFoundMessageHolder.setVisibility(View.VISIBLE);
            favoritesListView.setVisibility(View.GONE);
        }
    }
}
