package com.hoony.line_memo.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.repository.AppRepository;
import com.hoony.line_memo.repository.task.DeleteMemoTask;
import com.hoony.line_memo.repository.task.GetAllMemoTask;
import com.hoony.line_memo.repository.task.InsertMemoTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainViewModel extends AndroidViewModel
        implements GetAllMemoTask.GetAllMemoTaskCallback,
        InsertMemoTask.InsertMemoTaskCallback,
        DeleteMemoTask.DeleteMemoTaskCallback {

    public MainViewModel(@NonNull Application application) {
        super(application);
        appRepository = AppRepository.getINSTANCE(application);
        getAllMemo();
    }

    private final AppRepository appRepository;

    private MutableLiveData<List<Memo>> memoListMutableData = new MutableLiveData<>();

    private MutableLiveData<Memo> targetMemoMutableData = new MutableLiveData<>();

    public MutableLiveData<List<Memo>> getMemoListMutableData() {
        return memoListMutableData;
    }

    public MutableLiveData<Memo> getTargetMemoMutableData() {
        return targetMemoMutableData;
    }

    public void setTargetMemoMutableData(int position) {
        if (this.memoListMutableData.getValue() == null) return;

        Memo memo = this.memoListMutableData.getValue().get(position);
        this.targetMemoMutableData.setValue(memo);
    }

    private void getAllMemo() {
        appRepository.getAllMemo(MainViewModel.this);
    }

    void saveMemo(String title, String content) {
        Memo memo = new Memo();
        memo.setTitle(title);
        memo.setContent(content);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String date = dateFormat.format(new Date());

        memo.setDate(date);
        appRepository.saveMemo(memo, MainViewModel.this);
    }

    void deleteMemo(List<Memo> memoList) {
        appRepository.deleteMemo(memoList, MainViewModel.this);
    }

    @Override
    public void onGetAllMemoTaskSuccess(List<Memo> memoList) {
        memoListMutableData.setValue(memoList);
    }

    @Override
    public void onGetAllMemoTaskFail(Exception e) {

    }

    @Override
    public void onDeleteMemoTaskSuccess() {

    }

    @Override
    public void onDeleteMemoTaskFail(Exception e) {

    }

    @Override
    public void onInsertMemoTaskSuccess() {

    }

    @Override
    public void onInsertMemoTaskFail(Exception e) {

    }
}
