package com.hoony.line_memo.db.pojo;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class ImageData implements Parcelable {

    public static final int CAMERA = 0;
    public static final int GALLERY = 1;
    public static final int URL = 2;

    private int kind;
    private Uri uri;
    private String Name;

    public ImageData(int kind, Uri uri) {
        this.kind = kind;
        this.uri = uri;
    }

    ImageData(Uri uri, String name) {
        this.uri = uri;
        Name = name;
    }

    private ImageData(Parcel in) {
        kind = in.readInt();
        uri = in.readParcelable(Uri.class.getClassLoader());
        Name = in.readString();
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

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(kind);
        dest.writeParcelable(uri, flags);
        dest.writeString(Name);
    }
}
