package com.hoony.line_memo.memo.write;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.repository.AppRepository;
import com.hoony.line_memo.repository.task.InsertMemoTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MemoWriteViewModel extends ViewModel implements InsertMemoTask.InsertMemoTaskCallback {

    public MemoWriteViewModel(@NonNull Application application) {
        repository = AppRepository.getINSTANCE(application);
    }

    private final AppRepository repository;
    private MutableLiveData<Boolean> isCompleteMutableDate = new MutableLiveData<>();

    MutableLiveData<Boolean> getIsCompleteMutableDate() {
        return isCompleteMutableDate;
    }

    void saveMemo(String title, String content) {
        Memo memo = new Memo();
        memo.setTitle(title);
        memo.setContent(content);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String date = dateFormat.format(new Date());

        memo.setDate(date);
        repository.saveMemo(memo, MemoWriteViewModel.this);
    }

    @Override
    public void onInsertMemoTaskSuccess() {
        isCompleteMutableDate.setValue(true);
    }

    @Override
    public void onInsertMemoTaskFail(Exception e) {

    }
}
