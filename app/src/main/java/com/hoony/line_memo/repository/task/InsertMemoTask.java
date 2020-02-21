package com.hoony.line_memo.repository.task;

import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.db.table.memo.MemoDao;

import java.util.concurrent.Callable;

public class InsertMemoTask implements Callable<Long> {

    private final MemoDao memoDao;
    private final Memo memo;

    public InsertMemoTask(MemoDao memoDao, Memo memo) {
        this.memoDao = memoDao;
        this.memo = memo;
    }

    public interface InsertMemoTaskCallback {
        void onInsertMemoTaskSuccess(int type, long l);

        void onInsertMemoTaskFail(Exception e);
    }

    @Override
    public Long call() {
        return memoDao.insert(memo);
    }
}
