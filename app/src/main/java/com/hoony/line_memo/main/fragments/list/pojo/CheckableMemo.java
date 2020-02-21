package com.hoony.line_memo.main.fragments.list.pojo;

import android.widget.Checkable;

import com.hoony.line_memo.db.table.memo.Memo;

public class CheckableMemo extends Memo implements Checkable {

    private boolean isChecked = false;

    public CheckableMemo(Memo memo) {
        setId(memo.getId());
        setTitle(memo.getTitle());
        setContent(memo.getContent());
        setDate(memo.getDate());
        setImageDataList(memo.getImageDataList());
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
