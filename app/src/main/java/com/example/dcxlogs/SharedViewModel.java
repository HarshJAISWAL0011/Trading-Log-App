package com.example.dcxlogs;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> textViewText = new MutableLiveData<>();
    private final MutableLiveData<String> searchViewText = new MutableLiveData<>();

    public void setTextViewText(String newText) {
        textViewText.setValue(newText);
    }
    public void setSearchViewText(String newText) {
        searchViewText.setValue(newText);
        Log.d ( "check+++",newText );
    }

    public LiveData<String> getTextViewText() {
        return textViewText;
    }
    public LiveData<String> getSearchViewText() { return searchViewText; }
}
