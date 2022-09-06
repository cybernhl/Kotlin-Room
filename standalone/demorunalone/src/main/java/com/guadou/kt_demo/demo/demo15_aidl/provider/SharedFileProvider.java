package com.guadou.kt_demo.demo.demo15_aidl.provider;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.guadou.lib_baselib.utils.log.YYLogUtils;

public class SharedFileProvider extends ContentProvider {

    public static final String AUTOHORITY = "com.guadou.kt_demo.shared.fileprovider";

    @Override
    public boolean onCreate() {
        YYLogUtils.w("可以初始化一些东西");
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        YYLogUtils.w("走到这个 query 方法中来了哟");

        String[] cols = new String[1];
        cols[0] = "name";
        return new MatrixCursor(cols, 1);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
