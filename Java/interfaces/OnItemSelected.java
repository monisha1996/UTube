package com.smedic.tubtub.interfaces;

import com.smedic.tubtub.model.YouTubeVideo;

import java.util.List;


public interface OnItemSelected {
    void onVideoSelected(YouTubeVideo video);

    void onPlaylistSelected(List<YouTubeVideo> playlist, int position);
}
