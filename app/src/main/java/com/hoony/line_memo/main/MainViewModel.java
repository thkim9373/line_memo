package com.hoony.line_memo.main;

import android.app.Application;
import android.util.Log;

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

    public static final int NEW_MEMO = 0;
    public static final int EDIT_MEMO = 1;

    public MainViewModel(@NonNull Application application) {
        super(application);
        appRepository = AppRepository.getINSTANCE(application);
        getAllMemo();
        this.fragmentIndex.setValue(FRAGMENT_LIST);
    }

    private final AppRepository appRepository;

    private MutableLiveData<Integer> fragmentIndex = new MutableLiveData<>();

    private MutableLiveData<List<Memo>> memoListMutableData = new MutableLiveData<>();

    private MutableLiveData<Memo> readMemoMutableData = new MutableLiveData<>();

    private MutableLiveData<Memo> editMemoMutableData = new MutableLiveData<>();

    MutableLiveData<Integer> getFragmentIndex() {
        return fragmentIndex;
    }

    public MutableLiveData<List<Memo>> getMemoListMutableData() {
        return memoListMutableData;
    }

    public MutableLiveData<Memo> getReadMemoMutableData() {
        return readMemoMutableData;
    }

    public MutableLiveData<Memo> getEditMemoMutableData() {
        return editMemoMutableData;
    }

    public void initEditMemoMutableData() {
        Memo editMemo = new Memo();

        Memo memo = this.readMemoMutableData.getValue();
        if (memo != null) {
            editMemo.setId(memo.getId());
            editMemo.setTitle(memo.getTitle());
            editMemo.setContent(memo.getContent());
            editMemo.setDate(memo.getDate());

            List<ImageData> imageDataList = new ArrayList<>(memo.getImageDataList());
            editMemo.setImageDataList(imageDataList);
        }

        this.editMemoMutableData.setValue(editMemo);
    }

    public void createEditMemoMutableData() {
        this.editMemoMutableData.setValue(new Memo());
    }

    public void setFragmentIndex(int fragmentIndex) {
        this.fragmentIndex.setValue(fragmentIndex);
    }

    public void setReadMemoMutableData(int position) {
        if (this.memoListMutableData.getValue() == null) return;

        Memo memo = this.memoListMutableData.getValue().get(position);
        this.readMemoMutableData.setValue(memo);
    }

    private void getAllMemo() {
        appRepository.getAllMemo(MainViewModel.this);
    }

    public void setMemoTitle(String title) {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return;
        memo.setTitle(title);
    }

    public void setMemoContent(String content) {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return;
        memo.setContent(content);
    }

    public void addImages(List<ImageData> imageDataList) {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return;

        if (memo.getImageDataList() == null) memo.setImageDataList(new ArrayList<>());
        memo.getImageDataList().addAll(imageDataList);
    }

    public void addImages(ImageData imageData) {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return;

        if (memo.getImageDataList() == null) memo.setImageDataList(new ArrayList<>());
        memo.getImageDataList().add(imageData);
    }

    public void saveMemo() {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String date = dateFormat.format(new Date());

        memo.setDate(date);

        List<Memo> memoList = this.memoListMutableData.getValue();
        boolean isContain = false;

        if (memoList != null) {
            for (Memo m : memoList) {
                if (memo.getId() == m.getId()) {
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                memoList.add(memo);
            }
        }

        if (isContain) {
            appRepository.updateMemo(memo, MainViewModel.this);
        } else {
            appRepository.saveMemo(memo, MainViewModel.this);
        }

        this.readMemoMutableData.setValue(memo);
    }

    public void deleteMemo(int position) {
        if (this.memoListMutableData.getValue() == null) return;
        Memo memo = this.memoListMutableData.getValue().get(position);
        appRepository.deleteMemo(memo, MainViewModel.this);
    }

    @Override
    public void onGetAllMemoTaskSuccess(List<Memo> memoList) {
        memoListMutableData.setValue(memoList);
    }

    @Override
    public void onGetAllMemoTaskFail(Exception e) {
        Log.d("Hoony", e.toString());
    }

    @Override
    public void onDeleteMemoTaskSuccess() {
        getAllMemo();
    }

    @Override
    public void onDeleteMemoTaskFail(Exception e) {
        Log.d("Hoony", e.toString());
    }

    @Override
    public void onInsertMemoTaskSuccess() {
        getAllMemo();
    }

    @Override
    public void onInsertMemoTaskFail(Exception e) {
        Log.d("Hoony", e.toString());
    }

    @Override
    public void onUpdateMemoTaskSuccess() {
        getAllMemo();
    }

    @Override
    public void onUpdateMemoTaskFail(Exception e) {
        Log.d("Hoony", e.toString());
    }
}
