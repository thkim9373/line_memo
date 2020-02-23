package com.hoony.line_memo.db.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageData implements Parcelable {

    public static final int CAMERA = 0;
    public static final int GALLERY = 1;
    public static final int URL = 2;

    private int type;
    private String uriPath;
    private String filePath;

    public ImageData(int type, String uriPath) {
        this.type = type;
        this.uriPath = uriPath;
    }

    public ImageData(int type, String uriPath, String filePath) {
        this.type = type;
        this.uriPath = uriPath;
        this.filePath = filePath;
    }

    public int getType() {
        return type;
    }

    public String getUriPath() {
        return uriPath;
    }

    public String getFilePath() {
        return filePath;
    }

    protected ImageData(Parcel in) {
        type = in.readInt();
        uriPath = in.readString();
        filePath = in.readString();
    }

    public static final Creator<ImageData> CREATOR = new Creator<ImageData>() {
        @Override
        public ImageData createFromParcel(Parcel in) {
            return new ImageData(in);
        }

        @Override
        public ImageData[] newArray(int size) {
            return new ImageData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeString(uriPath);
        parcel.writeString(filePath);
    }
}
