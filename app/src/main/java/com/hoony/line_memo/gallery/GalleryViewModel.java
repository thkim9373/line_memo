package com.hoony.line_memo.gallery;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hoony.line_memo.db.pojo.ImageData;
import com.hoony.line_memo.gallery.pojo.CheckableImageData;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewModel extends AndroidViewModel {

    public GalleryViewModel(@NonNull Application application) {
        super(application);

        initImageDataList();
    }

    private void initImageDataList() {
        Cursor cursor = getApplication().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Media._ID,
                        MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATE_ADDED
                },
                null,
                null,
                MediaStore.Images.Media.DATE_ADDED + " DESC"
        );
        if (cursor != null) {
            List<CheckableImageData> imageDataList = getImageDataListFromCursor(cursor);
            setImageDateListMutableData(imageDataList);
            cursor.close();
        }
    }

    private MutableLiveData<List<CheckableImageData>> imageDateListMutableData = new MutableLiveData<>();

    MutableLiveData<List<CheckableImageData>> getImageDateListMutableData() {
        return imageDateListMutableData;
    }

    private void setImageDateListMutableData(List<CheckableImageData> imageDataList) {
        this.imageDateListMutableData.setValue(imageDataList);
    }

    private List<CheckableImageData> getImageDataListFromCursor(Cursor cursor) {
        List<CheckableImageData> result = new ArrayList<>();

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

        while (cursor.moveToNext()) {
            result.add(
                    new CheckableImageData(
                            ImageData.GALLERY,
                            Uri.withAppendedPath(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    String.valueOf(cursor.getLong(idColumn))
                            ).toString()
                    )
            );
        }
        return result;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.imageDateListMutableData = null;
    }
}
