package wangfeixixi.lbs;

import java.util.List;

/**
 * POI 搜索结果监听器
 */
public interface OnSearchedListener {
    void onSearched(List<LocationInfo> results);

    void onError(int rCode);
}
