package com.hoony.line_memo.db.table.memo;

import com.hoony.line_memo.db.pojo.ImageData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "memo")
public class Memo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "content")
    private String content;

    @ColumnInfo(name = "image_list")
    private List<ImageData> imageDataList = new ArrayList<>();

    public Memo() {

    }

    @Ignore
    public Memo(String title, String date, String content, List<ImageData> imageDataList) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.imageDataList = imageDataList;
    }

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

    public List<ImageData> getImageDataList() {
        return imageDataList;
    }

    public void setImageDataList(List<ImageData> imageDataList) {
        this.imageDataList = imageDataList;
    }

    @NonNull
    @Override
    public String toString() {
        return "Memo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                ", imageDataList=" + imageDataList +
                '}';
    }
}
