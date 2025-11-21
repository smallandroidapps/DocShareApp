package com.docshare.docshare.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.docshare.docshare.data.home.HomeRepository;
import com.docshare.docshare.ui.home.model.DocumentItem;
import com.docshare.docshare.ui.home.model.TagItem;

import java.util.List;
import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    private final HomeRepository repository = new HomeRepository();

    private final MutableLiveData<List<DocumentItem>> recentLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<DocumentItem>> recommendedLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<TagItem>> tagsLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>(false);

    public LiveData<List<DocumentItem>> getRecent() { return recentLiveData; }
    public LiveData<List<DocumentItem>> getRecommended() { return recommendedLiveData; }
    public LiveData<List<TagItem>> getTags() { return tagsLiveData; }
    public LiveData<Boolean> getLoading() { return loadingLiveData; }

    public void loadInitialHomeData() {
        loadingLiveData.postValue(true);
        tagsLiveData.postValue(repository.getTags());
        recentLiveData.postValue(repository.getRecentDocuments());
        recommendedLiveData.postValue(repository.getRecommendedDocuments());
        loadingLiveData.postValue(false);
    }

    public void refreshHomeData() {
        loadInitialHomeData();
    }

    public void applyTagFilter(String tagName) {
        List<DocumentItem> baseRecent = repository.getRecentDocuments();
        List<DocumentItem> baseRecs = repository.getRecommendedDocuments();
        recentLiveData.postValue(repository.filterByTag(baseRecent, tagName));
        recommendedLiveData.postValue(repository.filterByTag(baseRecs, tagName));
        // also update selection state for tags
        List<TagItem> current = tagsLiveData.getValue();
        if (current != null) {
            List<TagItem> updated = new ArrayList<>();
            for (TagItem t : current) {
                boolean sel = t.name.equals(tagName);
                updated.add(new TagItem(t.id, t.name, sel));
            }
            tagsLiveData.postValue(updated);
        }
    }
}
