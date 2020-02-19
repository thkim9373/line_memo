package com.hoony.line_memo.gallery;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.hoony.line_memo.db.pojo.ImageData;

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
            List<ImageData> imageDataList = getImageDataListFromCursor(cursor);
            setImageDateListMutableData(imageDataList);
            cursor.close();
        }
    }

    private MutableLiveData<List<ImageData>> imageDateListMutableData = new MutableLiveData<>();

    private MutableLiveData<List<ImageData>> selectedImageDataMutableData = new MutableLiveData<>();

    MutableLiveData<List<ImageData>> getImageDateListMutableData() {
        return imageDateListMutableData;
    }

    MutableLiveData<List<ImageData>> getSelectedImageDataMutableData() {
        return selectedImageDataMutableData;
    }

    private void setImageDateListMutableData(List<ImageData> imageDataList) {
        this.imageDateListMutableData.setValue(imageDataList);
    }

    private List<ImageData> getImageDataListFromCursor(Cursor cursor) {
        List<ImageData> result = new ArrayList<>();

        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);

        while (cursor.moveToNext()) {
            result.add(
                    new ImageData(
                            ImageData.GALLERY,
                            Uri.withAppendedPath(
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    String.valueOf(cursor.getLong(idColumn))
                            )
                    )
            );
        }

        return result;
    }

    void addSelectedItem(int position) {
        if (this.imageDateListMutableData.getValue() != null) {
            ImageData selectedImageData = this.imageDateListMutableData.getValue().get(position);

            if (this.selectedImageDataMutableData.getValue() == null) {
                this.selectedImageDataMutableData.setValue(new ArrayList<>());
            }
            this.selectedImageDataMutableData.getValue().add(selectedImageData);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        this.imageDateListMutableData = null;
    }
}
