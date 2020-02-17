package com.hoony.line_memo.db.table.memo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "memo")
public class Memo {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "content")
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @NonNull
    @Override
    public String toString() {
        return "Memo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
