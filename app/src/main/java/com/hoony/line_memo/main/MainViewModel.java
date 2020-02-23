package com.hoony.line_memo.main;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.hoony.line_memo.db.pojo.ImageData;
import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.main.fragments.list.pojo.CheckableMemo;
import com.hoony.line_memo.repository.AppRepository;
import com.hoony.line_memo.repository.task.DeleteMemoListTask;
import com.hoony.line_memo.repository.task.DeleteMemoTask;
import com.hoony.line_memo.repository.task.GetAllMemoTask;
import com.hoony.line_memo.repository.task.InsertMemoTask;
import com.hoony.line_memo.repository.task.UpdateMemoTask;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MainViewModel extends AndroidViewModel
        implements GetAllMemoTask.GetAllMemoTaskCallback,
        InsertMemoTask.InsertMemoTaskCallback,
        UpdateMemoTask.UpdateMemoTaskCallback,
        DeleteMemoTask.DeleteMemoTaskCallback,
        DeleteMemoListTask.DeleteMemoListTaskCallback {

    public static final int FRAGMENT_LIST = 0;
    public static final int FRAGMENT_VIEWER = 1;
    public static final int FRAGMENT_EDITOR = 2;

    public static final int LIST_MODE_DEFAULT = 0;
    public static final int LIST_MODE_SELECT = 1;

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

    private MutableLiveData<Boolean> isUpdate = new MutableLiveData<>();

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

    public boolean isUpdate() {
        return isUpdate.getValue() != null ? isUpdate.getValue() : false;
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
        this.isUpdate.setValue(false);
    }

    public void createEditMemoMutableData() {
        this.editMemoMutableData.setValue(new Memo());
        this.isUpdate.setValue(false);
    }

    @IntDef({FRAGMENT_LIST, FRAGMENT_VIEWER, FRAGMENT_EDITOR})
    @Retention(RetentionPolicy.SOURCE)
    @interface FragmentIndex {
    }

    public void setFragmentIndex(@FragmentIndex int fragmentIndex) {
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
        this.isUpdate.setValue(true);
    }

    public void setMemoContent(String content) {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return;
        memo.setContent(content);
        this.isUpdate.setValue(true);
    }

    public void addImages(List<ImageData> imageDataList) {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return;

        if (memo.getImageDataList() == null) memo.setImageDataList(new ArrayList<>());
        memo.getImageDataList().addAll(imageDataList);
        this.isUpdate.setValue(true);
    }

    public void addImage(ImageData imageData) {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return;

        if (memo.getImageDataList() == null) memo.setImageDataList(new ArrayList<>());
        memo.getImageDataList().add(imageData);
        this.isUpdate.setValue(true);
    }

    public void removeImage() {
        this.isUpdate.setValue(true);
    }

    public final static int MEMO_NO_UPDATE = -1;
    public final static int MEMO_NO_CONTENT = 0;
    public final static int MEMO_SAVE_COMPLETE = 1;

    public int saveMemo() {
        Memo memo = this.editMemoMutableData.getValue();
        if (memo == null) return MEMO_NO_CONTENT;

        if ((TextUtils.equals(memo.getTitle(), "") || memo.getTitle() == null) &&
                (TextUtils.equals(memo.getContent(), "") || memo.getContent() == null) &&
                memo.getImageDataList().size() == 0) return MEMO_NO_CONTENT;
        if (!this.isUpdate()) return MEMO_NO_UPDATE;

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
        return MEMO_SAVE_COMPLETE;
    }

    public void deleteMemoList(List<Memo> memoList) {

        for (Memo memo : memoList) {
            for (ImageData imageData : memo.getImageDataList()) {
                if (imageData.getType() == ImageData.CAMERA) {  //  카메라로 찍은 이미지를 삭제한다.
                    try {
                        String filePath = imageData.getFilePath();
                        File file = new File(filePath);
                        if (file.exists()) {
                            //noinspection ResultOfMethodCallIgnored
                            file.delete();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        appRepository.deleteMemoList(memoList, MainViewModel.this);
    }

    public void deleteMemo() {
        Memo memo = this.readMemoMutableData.getValue();
        if (memo == null) return;

        for (ImageData imageData : memo.getImageDataList()) {
            if (imageData.getType() == ImageData.CAMERA) {  //  카메라로 찍은 이미지를 삭제한다.
                try {
                    String filePath = imageData.getFilePath();
                    File file = new File(filePath);
                    if (file.exists()) {
                        //noinspection ResultOfMethodCallIgnored
                        file.delete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        appRepository.deleteMemo(memo, MainViewModel.this);
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
    public void onInsertMemoTaskSuccess(int type, long l) {
        if (this.readMemoMutableData.getValue() != null)
            this.readMemoMutableData.getValue().setId((int) l);

        setFragmentIndex(FRAGMENT_VIEWER);
        getAllMemo();
    }

    @Override
    public void onInsertMemoTaskFail(Exception e) {
        Log.d("Hoony", e.toString());
    }

    @Override
    public void onUpdateMemoTaskSuccess() {
        setFragmentIndex(FRAGMENT_VIEWER);
        getAllMemo();
    }

    @Override
    public void onUpdateMemoTaskFail(Exception e) {
        Log.d("Hoony", e.toString());
    }

    @Override
    public void onDeleteMemoTaskSuccess() {
        getAllMemo();
        this.setFragmentIndex(FRAGMENT_LIST);
    }

    @Override
    public void onDeleteMemoTaskFail(Exception e) {
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
