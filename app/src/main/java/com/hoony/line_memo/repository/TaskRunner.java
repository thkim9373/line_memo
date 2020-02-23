package com.hoony.line_memo.repository;

import android.os.Handler;
import android.os.Looper;

import com.hoony.line_memo.db.table.base.BaseDao;
import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.repository.task.DeleteMemoListTask;
import com.hoony.line_memo.repository.task.DeleteMemoTask;
import com.hoony.line_memo.repository.task.GetAllMemoTask;
import com.hoony.line_memo.repository.task.InsertMemoTask;
import com.hoony.line_memo.repository.task.UpdateMemoTask;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

class TaskRunner {
    private Executor executor = Executors.newSingleThreadExecutor();
    private Handler handler = new Handler(Looper.getMainLooper());

    void executeGetAllMemoTaskAsync(final Callable<List<Memo>> callable, GetAllMemoTask.GetAllMemoTaskCallback callback) {
        executor.execute(() -> {
            List<Memo> result;
            try {
                result = callable.call();
                handler.post(() -> callback.onGetAllMemoTaskSuccess(result));
            } catch (Exception e) {
                handler.post(() -> callback.onGetAllMemoTaskFail(e));
            }
        });
    }

    void executeInsertMemoTaskAsync(final Callable<Long> callable, InsertMemoTask.InsertMemoTaskCallback callback) {
        executor.execute(() -> {
            Long result;
            try {
                result = callable.call();
                handler.post(() -> callback.onInsertMemoTaskSuccess(BaseDao.INSERT, result));
            } catch (Exception e) {
                handler.post(() -> callback.onInsertMemoTaskFail(e));
            }
        });
    }

    void executeUpdateMemoTaskAsync(final Callable callable, UpdateMemoTask.UpdateMemoTaskCallback callback) {
        executor.execute(() -> {
            try {
                callable.call();
                handler.post(callback::onUpdateMemoTaskSuccess);
            } catch (Exception e) {
                handler.post(() -> callback.onUpdateMemoTaskFail(e));
            }
        });
    }

    void executeDeleteMemoTaskAsync(final Callable callable, DeleteMemoTask.DeleteMemoTaskCallback callback) {
        executor.execute(() -> {
            try {
                callable.call();
                handler.post(callback::onDeleteMemoTaskSuccess);
            } catch (Exception e) {
                handler.post(() -> callback.onDeleteMemoTaskFail(e));
            }
        });
    }

    void executeDeleteMemoListTaskAsync(final Callable callable, DeleteMemoListTask.DeleteMemoListTaskCallback callback) {
        executor.execute(() -> {
            try {
                callable.call();
                handler.post(callback::onDeleteMemoListTaskSuccess);
            } catch (Exception e) {
                handler.post(() -> callback.onDeleteMemoListTaskFail(e));
            }
        });
    }
}
