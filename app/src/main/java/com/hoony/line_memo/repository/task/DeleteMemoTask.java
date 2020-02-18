package com.hoony.line_memo.repository.task;

import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.db.table.memo.MemoDao;

import java.util.List;
import java.util.concurrent.Callable;

public class DeleteMemoTask implements Callable {

    private final MemoDao memoDao;
    private final List<Memo> memoList;

    public DeleteMemoTask(MemoDao memoDao, List<Memo> memoList) {
        this.memoDao = memoDao;
        this.memoList = memoList;
    }

    public interface DeleteMemoTaskCallback {
        void onDeleteMemoTaskSuccess();

        void onDeleteMemoTaskFail(Exception e);
    }

    @Override
    public Object call() {
        memoDao.deleteAll(memoList);
        return null;
    }
}
