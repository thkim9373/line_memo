package com.hoony.line_memo.db;

import android.app.Application;

import com.hoony.line_memo.db.table.memo.Memo;
import com.hoony.line_memo.db.table.memo.MemoDao;
import com.hoony.line_memo.db.util.Converters;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Memo.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Application application) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(application, AppDatabase.class, "memo-db").build();
        }
        return INSTANCE;
    }

    public abstract MemoDao memoDao();
}
