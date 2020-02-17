package com.hoony.line_memo.repository.task;

import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.db.table.memo.MemoDao;

import java.util.List;
import java.util.concurrent.Callable;

public class GetAllMemoTask implements Callable<List<Memo>> {

    private final MemoDao memoDao;

    public GetAllMemoTask(MemoDao memoDao) {
        this.memoDao = memoDao;
    }

    public interface GetAllMemoTaskCallback {
        void onGetAllMemoTaskSuccess(List<Memo> memoList);

        void onGetAllMemoTaskFail(Exception e);
    }

    @Override
    public List<Memo> call() {
        return memoDao.getAll();
    }
}
