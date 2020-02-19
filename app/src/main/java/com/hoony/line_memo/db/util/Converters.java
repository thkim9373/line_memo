package com.hoony.line_memo.db.util;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hoony.line_memo.db.pojo.ImageData;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Converters {
    @TypeConverter
    public static List<ImageData> fromString(String value) {
        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

//    @TypeConverter
//    public static String fromArrayList(ArrayList<String> list) {
//        Gson gson = new Gson();
//        return gson.toJson(list);
//    }

    @TypeConverter
    public static String fromArrayList(List<ImageData> imageDataList) {
        Gson gson = new Gson();
        return gson.toJson(imageDataList);
    }
}
