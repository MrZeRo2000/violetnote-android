package com.romanpulov.violetnote.model;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PassDataViewModel extends AndroidViewModel {
    public static class PassDataResult {
        private final PassDataA mPassData;
        private final String mLoadErrorText;

        public PassDataA getPassData() {
            return mPassData;
        }

        public String getLoadErrorText() {
            return mLoadErrorText;
        }

        public PassDataResult(PassDataA passData, String loadErrorText) {
            this.mPassData = passData;
            this.mLoadErrorText = loadErrorText;
        }
    }

    public static final class SearchPassDataResult extends PassDataResult {
        private final String mSearchText;

        public String getSearchText() {
            return mSearchText;
        }

        public SearchPassDataResult(PassDataA passData, String loadErrorText, String mSearchText) {
            super(passData, loadErrorText);
            this.mSearchText = mSearchText;
        }
    }

    public static final class PassDataLoaded {
        private final PassDataA mPassData;
        private final String mPassword;

        public PassDataA getPassData() {
            return mPassData;
        }

        public String getPassword() {
            return mPassword;
        }

        public PassDataLoaded(PassDataA mPassData, String mPassword) {
            this.mPassData = mPassData;
            this.mPassword = mPassword;
        }
    }

    private Context getContext() {
        return getApplication().getApplicationContext();
    }

    private ExecutorService mLoadExecutorService;

    private String mPassword;

    public void setPassword(String password) {
        this.mPassword = password;
    }

    // PassData loaded from file
    private PassDataLoaded mPassDataLoaded;

    private final MutableLiveData<PassDataResult> mPassDataResult = new MutableLiveData<>();

    public MutableLiveData<PassDataResult> getPassDataResult() {
        return mPassDataResult;
    }

    // PassData selected by category
    private PassDataA mPassDataSelectedByCategory;

    private final MutableLiveData<PassDataResult> mPassDataSelectedByCategoryResult = new MutableLiveData<>();

    public MutableLiveData<PassDataResult> getPassDataSelectedByCategory() {
        return mPassDataSelectedByCategoryResult;
    }

    public void selectPassDataByCategory(PassCategoryA passCategory) {
        mPassDataSelectedByCategory = PassDataA.newCategoryInstance(mPassDataLoaded.getPassData(), passCategory);
        mPassDataSelectedByCategoryResult.setValue(new PassDataResult(mPassDataSelectedByCategory, null));
    }

    public void loadPassDataSelectedByCategory() {
        if ((mPassDataLoaded != null) && (mPassDataLoaded.getPassword().equals(mPassword))) {
            mPassDataSelectedByCategoryResult.setValue(new PassDataResult(mPassDataSelectedByCategory, null));
        } else {
            mPassDataSelectedByCategoryResult.setValue(new PassDataResult(null, getContext().getString(R.string.ui_error_wrong_password)));
        }
    }

    public static final class PassDataSearch {
        private final PassDataA mPassData;
        private final String mSearchText;

        public PassDataA getPassData() {
            return mPassData;
        }

        public String getSearchText() {
            return mSearchText;
        }

        public PassDataSearch(PassDataA mPassData, String mSearchText) {
            this.mPassData = mPassData;
            this.mSearchText = mSearchText;
        }
    }

    // PassData search result
    private PassDataSearch mPassDataSearch;

    private final MutableLiveData<SearchPassDataResult> mPassDataSearchResult = new MutableLiveData<>();

    public MutableLiveData<SearchPassDataResult> getPassDataSearchResult() {
        return mPassDataSearchResult;
    }

    public void searchPassData(String searchString, boolean isSearchSystem, boolean isSearchUser) {
        mPassDataSearch = new PassDataSearch(PassDataA.newSearchInstance(mPassDataLoaded.getPassData(), searchString, isSearchSystem, isSearchUser), searchString);
        SearchPassDataResult searchPassDataResult = new SearchPassDataResult(mPassDataSearch.getPassData(), null, mPassDataSearch.getSearchText());
        mPassDataSearchResult.setValue(searchPassDataResult);
    }

    public void loadSearchPassData() {
        if ((mPassDataLoaded != null) && (mPassDataLoaded.getPassword().equals(mPassword))) {
            mPassDataSearchResult.setValue(new SearchPassDataResult(mPassDataSearch.getPassData(), null, mPassDataSearch.getSearchText()));
        } else {
            mPassDataSearchResult.setValue(new SearchPassDataResult(null, getContext().getString(R.string.ui_error_wrong_password), null));
        }
    }

    // PassData note
    private PassDataA mPassDataNote;

    private final MutableLiveData<PassDataResult> mPassDataNoteResult = new MutableLiveData<>();

    public MutableLiveData<PassDataResult> getPassDataNoteResult() {
        return mPassDataNoteResult;
    }

    public void selectPassDataNote(PassNoteA passNote) {
        mPassDataNote = PassDataA.newNoteInstance(mPassDataLoaded.getPassData(), passNote);
        mPassDataNoteResult.setValue(new PassDataResult(mPassDataNote, null));
    }

    public void loadPassDataNote() {
        if ((mPassDataLoaded != null) && (mPassDataLoaded.getPassword().equals(mPassword))) {
            mPassDataNoteResult.setValue(new PassDataResult(mPassDataNote, null));
        } else {
            mPassDataNoteResult.setValue(new SearchPassDataResult(null, getContext().getString(R.string.ui_error_wrong_password), null));
        }
    }

    public PassDataViewModel(@NonNull Application application) {
        super(application);
    }

    public void loadPassData() {
        if (mPassDataLoaded == null) {
            if (mLoadExecutorService == null) {
                mLoadExecutorService = Executors.newFixedThreadPool(1);
            }

            mLoadExecutorService.submit(() -> {
                DocumentPassDataLoader.DocumentPassDataLoadResult passDataLoadResult =
                        DocumentPassDataLoader.loadPassData(getContext(), mPassword);

                if (passDataLoadResult.getPassData() != null) {
                    mPassDataLoaded = new PassDataLoaded(passDataLoadResult.getPassData(), mPassword);
                }

                mPassDataResult.postValue(new PassDataResult(passDataLoadResult.getPassData(), passDataLoadResult.getErrorMessage()));

            });
        } else {
            if (mPassDataLoaded.getPassword().equals(mPassword)) {
                mPassDataResult.setValue(new PassDataResult(mPassDataLoaded.getPassData(), null));
            } else {
                mPassDataResult.setValue(new PassDataResult(null, getContext().getString(R.string.ui_error_wrong_password)));
            }
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();

        if (mLoadExecutorService != null) {
            mLoadExecutorService.shutdownNow();
        }
    }
}
