package com.hoony.line_memo.repository.task;

import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.db.table.memo.MemoDao;

import java.util.concurrent.Callable;

public class DeleteMemoTask implements Callable {

    private final MemoDao memoDao;
    private final Memo memo;

    public DeleteMemoTask(MemoDao memoDao, Memo memo) {
        this.memoDao = memoDao;
        this.memo = memo;
    }

    public interface DeleteMemoTaskCallback {
        void onDeleteMemoTaskSuccess();

        void onDeleteMemoTaskFail(Exception e);
    }

    @Override
    public Object call() {
        memoDao.delete(memo);
        return null;
    }
}
