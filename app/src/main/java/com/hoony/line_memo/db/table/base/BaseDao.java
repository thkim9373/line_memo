package com.hoony.line_memo.db.table.base;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import java.util.List;

@Dao
public interface BaseDao<T> {

    @Insert
    void insert(T obj);

    @Update
    void update(T obj);

    @Delete
    void delete(T obj);

    @Delete
    void deleteAll(List<T> list);
}
