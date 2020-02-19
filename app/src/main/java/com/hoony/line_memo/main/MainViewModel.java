package com.hoony.line_memo.main;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hoony.line_memo.db.pojo.ImageData;
import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.repository.AppRepository;
import com.hoony.line_memo.repository.task.DeleteMemoTask;
import com.hoony.line_memo.repository.task.GetAllMemoTask;
import com.hoony.line_memo.repository.task.InsertMemoTask;
import com.hoony.line_memo.repository.task.UpdateMemoTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainViewModel extends AndroidViewModel
        implements GetAllMemoTask.GetAllMemoTaskCallback,
        InsertMemoTask.InsertMemoTaskCallback,
        DeleteMemoTask.DeleteMemoTaskCallback,
        UpdateMemoTask.UpdateMemoTaskCallback {

    public static final int FRAGMENT_LIST = 0;
    public static final int FRAGMENT_READ = 1;
    public static final int FRAGMENT_WRITE = 2;

    public MainViewModel(@NonNull Application application) {
        super(application);
        appRepository = AppRepository.getINSTANCE(application);
        getAllMemo();
        this.fragmentIndex.setValue(FRAGMENT_LIST);
    }

    private final AppRepository appRepository;

    private MutableLiveData<Integer> fragmentIndex = new MutableLiveData<>();

    private MutableLiveData<List<Memo>> memoListMutableData = new MutableLiveData<>();

    private MutableLiveData<Memo> currentMemoMutableData = new MutableLiveData<>();

    MutableLiveData<Integer> getFragmentIndex() {
        return fragmentIndex;
    }

    public MutableLiveData<List<Memo>> getMemoListMutableData() {
        return memoListMutableData;
    }

    public MutableLiveData<Memo> getCurrentMemoMutableData() {
        return currentMemoMutableData;
    }

    public void setFragmentIndex(int fragmentIndex) {
        this.fragmentIndex.setValue(fragmentIndex);
    }

    public void setCurrentMemoMutableData(int position) {
        if (this.memoListMutableData.getValue() == null) return;

        Memo memo = this.memoListMutableData.getValue().get(position);
        this.currentMemoMutableData.setValue(memo);
    }

    private void getAllMemo() {
        appRepository.getAllMemo(MainViewModel.this);
    }

    public void addImages(List<ImageData> imageDataList) {
        Memo memo = this.currentMemoMutableData.getValue();
        if (memo != null) {
            List<ImageData> memoImageDataList = memo.getImageDataList();
            if (memoImageDataList == null) memoImageDataList = new ArrayList<>();
            memoImageDataList.addAll(imageDataList);

            memo.setImageDataList(memoImageDataList);
        }
//        this.currentMemoMutableData.setValue(memo);
    }

    public void saveMemo(String title, String content) {
        Memo memo;
        boolean isContain;
        if (currentMemoMutableData.getValue() != null) {
            memo = currentMemoMutableData.getValue();

            List<Memo> memoList = this.memoListMutableData.getValue();
            isContain = memoList != null && memoList.contains(memo);
        } else {
            memo = new Memo();
            isContain = false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String date = dateFormat.format(new Date());

        memo.setDate(date);
        memo.setTitle(title);
        memo.setContent(content);

        if (isContain) {
            appRepository.updateMemo(memo, MainViewModel.this);
        } else {
            currentMemoMutableData.setValue(memo);
            appRepository.saveMemo(memo, MainViewModel.this);
        }
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
        getAllMemo();
    }

    @Override
    public void onInsertMemoTaskFail(Exception e) {

    }

    @Override
    public void onUpdateMemoTaskSuccess() {
        getAllMemo();
    }

    @Override
    public void onUpdateMemoTaskFail(Exception e) {

    }
}
