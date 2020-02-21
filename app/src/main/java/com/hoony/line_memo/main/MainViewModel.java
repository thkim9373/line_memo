package com.hoony.line_memo.main;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hoony.line_memo.db.pojo.ImageData;
import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.main.fragments.list.pojo.CheckableMemo;
import com.hoony.line_memo.repository.AppRepository;
import com.hoony.line_memo.repository.task.DeleteMemoListTask;
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
        UpdateMemoTask.UpdateMemoTaskCallback,
        DeleteMemoListTask.DeleteMemoListTaskCallback {

    public static final int FRAGMENT_LIST = 0;
    public static final int FRAGMENT_READER = 1;
    public static final int FRAGMENT_EDITOR = 2;

    public static final int LIST_MODE_DEFAULT = 0;
    public static final int LIST_MODE_SELECT = 1;

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

    private MutableLiveData<List<CheckableMemo>> memoListMutableData = new MutableLiveData<>();

    private MutableLiveData<Integer> listFragmentModeMutableData = new MutableLiveData<>();

    private MutableLiveData<Memo> readMemoMutableData = new MutableLiveData<>();

    private MutableLiveData<Boolean> isEdit = new MutableLiveData<>();

    private MutableLiveData<Memo> editMemoMutableData = new MutableLiveData<>();

    MutableLiveData<Integer> getFragmentIndex() {
        return fragmentIndex;
    }

    public MutableLiveData<List<CheckableMemo>> getMemoListMutableData() {
        return memoListMutableData;
    }

    public void setListFragmentModeMutableData(int mode) {
        this.listFragmentModeMutableData.setValue(mode);
    }

    public int getListFragmentMode() {
        return this.listFragmentModeMutableData.getValue() != null ? this.listFragmentModeMutableData.getValue() : LIST_MODE_DEFAULT;
    }

    public MutableLiveData<Memo> getReadMemoMutableData() {
        return readMemoMutableData;
    }

    public boolean isEdit() {
        return isEdit.getValue() != null ? isEdit.getValue() : false;
    }

    public MutableLiveData<Memo> getEditMemoMutableData() {
        return editMemoMutableData;
    }

    public String getSelectedImageUri(int position) {
        Memo memo = this.readMemoMutableData.getValue();
        if (memo == null) return null;

        return memo.getImageDataList().get(position).getUriPath();
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
        this.isEdit.setValue(true);
    }

    public void setMemoContent(String content) {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return;
        memo.setContent(content);
        this.isEdit.setValue(true);
    }

    public void addImages(List<ImageData> imageDataList) {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return;

        if (memo.getImageDataList() == null) memo.setImageDataList(new ArrayList<>());
        memo.getImageDataList().addAll(imageDataList);
        this.isEdit.setValue(true);
    }

    public void addImage(ImageData imageData) {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return;

        if (memo.getImageDataList() == null) memo.setImageDataList(new ArrayList<>());
        memo.getImageDataList().add(imageData);
        this.isEdit.setValue(true);
    }

    public int removeImage(ImageData imageData) {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return -1;

        if (memo.getImageDataList() == null) memo.setImageDataList(new ArrayList<>());

        int targetIndex = memo.getImageDataList().indexOf(imageData);
        memo.getImageDataList().remove(imageData);
        this.isEdit.setValue(true);

        return targetIndex;
    }

    public void saveMemo() {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String date = dateFormat.format(new Date());

        memo.setDate(date);

        List<CheckableMemo> memoList = this.memoListMutableData.getValue();
        boolean isContain = false;

        if (memoList != null) {
            for (Memo m : memoList) {
                if (memo.getId() == m.getId()) {
                    isContain = true;
                    break;
                }
            }
            if (!isContain) {
                memoList.add(new CheckableMemo(memo));
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

    public void deleteMemoList(List<Memo> memoList) {
        appRepository.deleteMemoList(memoList, MainViewModel.this);
    }

    @Override
    public void onGetAllMemoTaskSuccess(List<Memo> memoList) {
        List<CheckableMemo> checkableMemoList = new ArrayList<>();
        for (Memo memo : memoList) {
            checkableMemoList.add(new CheckableMemo(memo));
        }
        memoListMutableData.setValue(checkableMemoList);
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
    public void onInsertMemoTaskSuccess(int type, long l) {
        if (this.readMemoMutableData.getValue() != null)
            this.readMemoMutableData.getValue().setId((int) l);
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

    @Override
    public void onDeleteMemoListTaskSuccess() {
        getAllMemo();
    }

    @Override
    public void onDeleteMemoListTaskFail(Exception e) {
        Log.d("Hoony", e.toString());
    }
}
