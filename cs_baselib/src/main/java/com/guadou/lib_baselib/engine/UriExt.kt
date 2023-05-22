package com.guadou.lib_baselib.engine

import android.net.Uri
import android.os.Build
import com.guadou.lib_baselib.ext.commContext
import java.io.File


fun Any.getFileUri(file: File): Uri {

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        PermissionFileProvider.getUriForFile(commContext(), commContext().packageName + ".file.path.share", file)
    } else Uri.fromFile(file)
}