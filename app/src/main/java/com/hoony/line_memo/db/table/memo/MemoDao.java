package com.hoony.line_memo.db.table.memo;

import com.hoony.line_memo.db.table.base.BaseDao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface MemoDao extends BaseDao<Memo> {
    @Query("SELECT * FROM memo")
    List<Memo> getAll();
}
