package com.hoony.line_memo.gallery.pojo;

import android.widget.Checkable;

import com.hoony.line_memo.db.pojo.ImageData;

public class CheckableImageData extends ImageData implements Checkable {

    private boolean isChecked = false;

    public CheckableImageData(int kind, String uriPath) {
        super(kind, uriPath);
    }

    @Override
    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void toggle() {
        isChecked = !isChecked;
    }
}
