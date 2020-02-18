package com.hoony.line_memo.repository.task;

import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.db.table.memo.MemoDao;

import java.util.concurrent.Callable;

public class UpdateMemoTask implements Callable {

    private final MemoDao memoDao;
    private final Memo memo;

    public UpdateMemoTask(MemoDao memoDao, Memo memo) {
        this.memoDao = memoDao;
        this.memo = memo;
    }

    public interface UpdateMemoTaskCallback {
        void onUpdateMemoTaskSuccess();

        void onUpdateMemoTaskFail(Exception e);
    }

    @Override
    public Object call() {
        memoDao.update(memo);
        return null;
    }
}
