package com.hoony.line_memo.db.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class ImageData implements Parcelable {

    public static final int CAMERA = 0;
    public static final int GALLERY = 1;
    public static final int URL = 2;

    private int kind;
    private String uriPath;

    public ImageData(int kind, String uriPath) {
        this.kind = kind;
        this.uriPath = uriPath;
    }

    protected ImageData(Parcel in) {
        kind = in.readInt();
        uriPath = in.readString();
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

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public String getUriPath() {
        return uriPath;
    }

    public void setUriPath(String uriPath) {
        this.uriPath = uriPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(kind);
        parcel.writeString(uriPath);
    }
}
