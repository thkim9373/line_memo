package com.hoony.line_memo.repository;

import android.app.Application;

import com.hoony.line_memo.db.AppDatabase;
import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.db.table.memo.MemoDao;
import com.hoony.line_memo.repository.task.DeleteMemoListTask;
import com.hoony.line_memo.repository.task.DeleteMemoTask;
import com.hoony.line_memo.repository.task.GetAllMemoTask;
import com.hoony.line_memo.repository.task.InsertMemoTask;
import com.hoony.line_memo.repository.task.UpdateMemoTask;

import java.util.List;

public class AppRepository {
    private static AppRepository INSTANCE;

    public static AppRepository getINSTANCE(Application application) {
        if (INSTANCE == null) {
            INSTANCE = new AppRepository(application);
        }
        return INSTANCE;
    }

    private AppRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        memoDao = appDatabase.memoDao();
    }

    private final MemoDao memoDao;

    public void getAllMemo(GetAllMemoTask.GetAllMemoTaskCallback callback) {
        TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeGetAllMemoTaskAsync(new GetAllMemoTask(memoDao), callback);
    }

    public void saveMemo(Memo memo, InsertMemoTask.InsertMemoTaskCallback callback) {
        TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeInsertMemoTaskAsync(new InsertMemoTask(memoDao, memo), callback);
    }

    public void updateMemo(Memo memo, UpdateMemoTask.UpdateMemoTaskCallback callback) {
        TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeUpdateMemoTaskAsync(new UpdateMemoTask(memoDao, memo), callback);
    }

    public void deleteMemo(Memo memo, DeleteMemoTask.DeleteMemoTaskCallback callback) {
        TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeDeleteMemoTaskAsync(new DeleteMemoTask(memoDao, memo), callback);
    }

    public void deleteMemoList(List<Memo> memoList, DeleteMemoListTask.DeleteMemoListTaskCallback callback) {
        TaskRunner taskRunner = new TaskRunner();
        taskRunner.executeDeleteMemoListTaskAsync(new DeleteMemoListTask(memoDao, memoList), callback);
    }
}
