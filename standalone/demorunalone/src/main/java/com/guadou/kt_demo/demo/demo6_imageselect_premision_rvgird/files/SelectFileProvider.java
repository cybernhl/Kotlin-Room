package com.guadou.kt_demo.demo.demo6_imageselect_premision_rvgird.files;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.StatFs;
import android.provider.DocumentsContract.*;
import android.provider.DocumentsProvider;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.os.EnvironmentCompat;

import com.guadou.kt_demo.R;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

/**
 * 选择文件
 */
public class SelectFileProvider extends DocumentsProvider {

    private final static String[] DEFAULT_ROOT_PROJECTION = new String[]{Root.COLUMN_ROOT_ID, Root.COLUMN_SUMMARY,
            Root.COLUMN_FLAGS, Root.COLUMN_TITLE, Root.COLUMN_DOCUMENT_ID, Root.COLUMN_ICON,
            Root.COLUMN_AVAILABLE_BYTES};

    private final static String[] DEFAULT_DOCUMENT_PROJECTION = new String[]{Document.COLUMN_DOCUMENT_ID,
            Document.COLUMN_DISPLAY_NAME, Document.COLUMN_FLAGS, Document.COLUMN_MIME_TYPE, Document.COLUMN_SIZE,
            Document.COLUMN_LAST_MODIFIED};

    public static final String AUTOHORITY = "com.guadou.kt_demo.selectfileprovider.authorities";

    //是否有权限
    private static boolean hasPermission(@Nullable Context context) {
        if (context == null) {
            return false;
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @Override
    public Cursor queryRoots(String[] projection) throws FileNotFoundException {
        if (!hasPermission(getContext())) {
            return null;
        }

        //创建一个查询cursor, 来设置需要查询的项, 如果"projection"为空, 那么使用默认项
        final MatrixCursor result = new MatrixCursor(projection != null ? projection : DEFAULT_ROOT_PROJECTION);
        // 添加home路径
        File homeDir = Environment.getExternalStorageDirectory();
        YYLogUtils.w("homeDir:" + homeDir);
        if (TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {

            final MatrixCursor.RowBuilder row = result.newRow();
            row.add(Root.COLUMN_ROOT_ID, homeDir.getAbsolutePath());
            row.add(Root.COLUMN_DOCUMENT_ID, homeDir.getAbsolutePath());
            row.add(Root.COLUMN_TITLE, "Home");
            row.add(Root.COLUMN_FLAGS, Root.FLAG_LOCAL_ONLY | Root.FLAG_SUPPORTS_CREATE | Root.FLAG_SUPPORTS_IS_CHILD);
            row.add(Root.COLUMN_ICON, R.mipmap.ic_launcher);
        }

        // 添加SD卡路径
        File sdCard = new File("/storage/extSdCard");
        String storageState = EnvironmentCompat.getStorageState(sdCard);
        if (TextUtils.equals(storageState, Environment.MEDIA_MOUNTED) ||
                TextUtils.equals(storageState, Environment.MEDIA_MOUNTED_READ_ONLY)) {

            final MatrixCursor.RowBuilder row = result.newRow();
            row.add(Root.COLUMN_ROOT_ID, sdCard.getAbsolutePath());
            row.add(Root.COLUMN_DOCUMENT_ID, sdCard.getAbsolutePath());
            row.add(Root.COLUMN_TITLE, "SD卡");
            row.add(Root.COLUMN_FLAGS, Root.FLAG_LOCAL_ONLY);
            row.add(Root.COLUMN_ICON, R.mipmap.ic_launcher_round);
            row.add(Root.COLUMN_SUMMARY, sdCard.getAbsolutePath());
            row.add(Root.COLUMN_AVAILABLE_BYTES, new StatFs(sdCard.getAbsolutePath()).getAvailableBytes());

        }
        return result;
    }

    @Override
    public boolean isChildDocument(String parentDocumentId, String documentId) {
        return documentId.startsWith(parentDocumentId);
    }

    @Override
    public Cursor queryDocument(String documentId, String[] projection) throws FileNotFoundException {
        // 判断是否缺少权限
        if (!hasPermission(getContext())) {
            return null;
        }

        // 创建一个查询cursor, 来设置需要查询的项, 如果"projection"为空, 那么使用默认项
        final MatrixCursor result = new MatrixCursor(projection != null ? projection : DEFAULT_DOCUMENT_PROJECTION);
        includeFile(result, new File(documentId));
        return result;
    }

    @Override
    public Cursor queryChildDocuments(String parentDocumentId, String[] projection, String sortOrder) throws FileNotFoundException {
        // 判断是否缺少权限
        if (!hasPermission(getContext())) {
            return null;
        }

        // 创建一个查询cursor, 来设置需要查询的项, 如果"projection"为空, 那么使用默认项
        final MatrixCursor result = new MatrixCursor(projection != null ? projection : DEFAULT_DOCUMENT_PROJECTION);
        final File parent = new File(parentDocumentId);
        String absolutePath = parent.getAbsolutePath();
        boolean isDirectory = parent.isDirectory();
        boolean canRead = parent.canRead();
        boolean canWrite = parent.canWrite();
        File[] files = parent.listFiles();

        YYLogUtils.w("parent:" + parent + " absolutePath:" + absolutePath + " isDirectory:" +
                isDirectory + " canRead:" + canRead + " canWrite:" + canWrite + " files:" + files);

        if (isDirectory && canRead && files != null && files.length > 0) {
            for (File file : parent.listFiles()) {
                // 不显示隐藏的文件或文件夹
                if (!file.getName().startsWith(".")) {
                    // 添加文件的名字, 类型, 大小等属性
                    includeFile(result, file);
                }
            }
        }

        return result;
    }

    private void includeFile(final MatrixCursor result, final File file) throws FileNotFoundException {
        final MatrixCursor.RowBuilder row = result.newRow();
        row.add(Document.COLUMN_DOCUMENT_ID, file.getAbsolutePath());
        row.add(Document.COLUMN_DISPLAY_NAME, file.getName());
        String mimeType = getDocumentType(file.getAbsolutePath());
        row.add(Document.COLUMN_MIME_TYPE, mimeType);
        int flags = file.canWrite()
                ? Document.FLAG_SUPPORTS_DELETE | Document.FLAG_SUPPORTS_WRITE | Document.FLAG_SUPPORTS_RENAME
                | (mimeType.equals(Document.MIME_TYPE_DIR) ? Document.FLAG_DIR_SUPPORTS_CREATE : 0) : 0;
        if (mimeType.startsWith("image/"))
            flags |= Document.FLAG_SUPPORTS_THUMBNAIL;
        row.add(Document.COLUMN_FLAGS, flags);
        row.add(Document.COLUMN_SIZE, file.length());
        row.add(Document.COLUMN_LAST_MODIFIED, file.lastModified());
    }


    @Override
    public String getDocumentType(String documentId) throws FileNotFoundException {
        if (!hasPermission(getContext())) {
            return null;
        }

        File file = new File(documentId);
        if (file.isDirectory()) {
            //如果是文件夹-先返回再说
            return Document.MIME_TYPE_DIR;
        }

        final int lastDot = file.getName().lastIndexOf('.');
        if (lastDot >= 0) {
            //如果文件有后缀-直接返回后缀名的类型
            final String extension = file.getName().substring(lastDot + 1);
            final String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            if (mime != null) {
                return mime;
            }
        }
        return "application/octet-stream";
    }


    @Override
    public ParcelFileDescriptor openDocument(String documentId, String mode, @Nullable CancellationSignal signal) throws FileNotFoundException {
        return null;
    }

    @Override
    public boolean onCreate() {
        return true;
    }

}
