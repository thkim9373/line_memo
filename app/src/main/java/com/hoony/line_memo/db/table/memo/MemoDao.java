package com.hoony.line_memo.db.table.memo;

import androidx.room.Dao;
import androidx.room.Query;

import com.hoony.line_memo.db.table.base.BaseDao;

import java.util.List;

@Dao
public interface MemoDao extends BaseDao<Memo> {
    @Query("SELECT * FROM memo ORDER BY date DESC, id DESC")
    List<Memo> getAll();
}
