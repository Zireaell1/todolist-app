package com.zireaell1.todolist;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

public class Utils {
    public static String getContent(Context context, Uri uri, String mediaType) {
        String[] projection = {mediaType};
        String path = "";

        ContentResolver cr = context.getContentResolver();
        Cursor metaCursor = cr.query(uri, projection, null, null, null);
        if (metaCursor != null) {
            try {
                if (metaCursor.moveToFirst()) {
                    path = metaCursor.getString(0);
                }
            } finally {
                metaCursor.close();
            }
        }
        return path;
    }
}
