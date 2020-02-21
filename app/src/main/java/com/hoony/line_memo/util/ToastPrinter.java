package com.hoony.line_memo.util;

import android.content.Context;
import android.widget.Toast;

public class ToastPrinter {
    private static Toast mToast;

    public static void show(String msg, Context context) {
        if (mToast != null) mToast.cancel();
        mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
