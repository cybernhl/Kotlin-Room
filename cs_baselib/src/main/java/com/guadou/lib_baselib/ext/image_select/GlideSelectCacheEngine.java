package com.guadou.lib_baselib.ext.image_select;

import android.content.Context;

import com.luck.picture.lib.engine.CacheResourcesEngine;

import java.io.File;

/**
 * 缓存的图片加载类
 */
 class GlideSelectCacheEngine implements CacheResourcesEngine {
    /**
     * glide版本号,请根据用户集成为准 这里只是模拟
     */
    private final static int GLIDE_VERSION = 4;

    @Override
    public String onCachePath(Context context, String url) {
        File cacheFile;
        if (GLIDE_VERSION >= 4) {
            // Glide 4.x
            cacheFile = ImageSelectCacheUtils.getCacheFileTo4x(context, url);
        } else {
            // Glide 3.x
            cacheFile = ImageSelectCacheUtils.getCacheFileTo3x(context, url);
        }
        return cacheFile != null ? cacheFile.getAbsolutePath() : "";
    }


    private GlideSelectCacheEngine() {
    }

    private static GlideSelectCacheEngine instance;

    public static GlideSelectCacheEngine createCacheEngine() {
        if (null == instance) {
            synchronized (GlideSelectCacheEngine.class) {
                if (null == instance) {
                    instance = new GlideSelectCacheEngine();
                }
            }
        }
        return instance;
    }
}
