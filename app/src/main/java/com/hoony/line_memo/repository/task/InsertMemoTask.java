package com.hoony.line_memo.repository.task;

import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.db.table.memo.MemoDao;

import java.util.concurrent.Callable;

public class InsertMemoTask implements Callable {

    private final MemoDao memoDao;
    private final Memo memo;

    public InsertMemoTask(MemoDao memoDao, Memo memo) {
        this.memoDao = memoDao;
        this.memo = memo;
    }

    public interface InsertMemoTaskCallback {
        void onInsertMemoTaskSuccess();

        void onInsertMemoTaskFail(Exception e);
    }

    @Override
    public Object call() {
        memoDao.insert(memo);
        return null;
    }
}
