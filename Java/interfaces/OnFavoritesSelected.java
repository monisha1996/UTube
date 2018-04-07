package com.smedic.tubtub.interfaces;

import com.smedic.tubtub.model.YouTubeVideo;



public interface OnFavoritesSelected {
    void onFavoritesSelected(YouTubeVideo video, boolean isChecked);
}
