package com.hoony.line_memo.repository.task;

import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.db.table.memo.MemoDao;

import java.util.List;
import java.util.concurrent.Callable;

public class DeleteMemoListTask implements Callable {

    private final MemoDao memoDao;
    private final List<Memo> memoList;

    public DeleteMemoListTask(MemoDao memoDao, List<Memo> memo) {
        this.memoDao = memoDao;
        this.memoList = memo;
    }

    public interface DeleteMemoListTaskCallback {
        void onDeleteMemoListTaskSuccess();

        void onDeleteMemoListTaskFail(Exception e);
    }

    @Override
    public Object call() {
        memoDao.deleteAll(memoList);
        return null;
    }
}
