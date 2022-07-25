/**
 * @copyright Capgemini
 */
package com.capgemini.sesp.ast.android.module.others;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.capgemini.sesp.ast.android.BuildConfig;
import com.capgemini.sesp.ast.android.module.util.ConstantsAstSep;
import com.jakewharton.DiskLruCache;
import com.jakewharton.DiskLruCache.Editor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

import static com.capgemini.sesp.ast.android.module.util.exception.SespLogHandler.writeLog;

/**
 * <p>
 * The disk based cache implementation to store
 * smaller downgraded and decoded bitmap thumbnails to be stored.
 * </p>
 * <p/>
 * <p>
 * This is an LRU cache so if the cache overflows the first entries
 * would be removed and it would never exhaust the entire disk-space
 * </p>
 *
 * @author Capgemini
 * @version 1.0
 * @since 20th November, 2014
 */
public final class DiskLRUCacheImage {

    // The core reference by which this is implemented
    private DiskLruCache diskLruCache = null;
    /*
     *  Compression format can be one of the following
     *  1. JPEG
     *  2. PNG
     *  3. WEBP
     */
    private static final String TAG = "DiskLRUCacheImage";
    private static transient final CompressFormat COMPRESSFORMAT = CompressFormat.JPEG;

    // Default max cache size = 50 MB, user could modify this from screen option
    private static final int CACHEMAXSIZEINBYTE = 50 * 1024 * 1024;

    private final transient int COMPRESSQUALITY = 70;
    // Buffer size in bytes
    private final transient int BUFFER_SIZE = 512 * 1024;

    private transient final int APP_VERSION = 1;
    private transient final int VALUE_COUNT = 1;

    // For the entire application there should be only one reference
    private static DiskLRUCacheImage staticImage = null;

    private final transient String CACHETESTDISKTAG = "cache_test_DISK_";

    public static DiskLRUCacheImage getDiskCache(final Context ctx) throws Exception {
        if (staticImage == null) {
            staticImage = new DiskLRUCacheImage(ctx, CACHEMAXSIZEINBYTE, COMPRESSFORMAT);
        }
        return staticImage;
    }

    public static DiskLRUCacheImage getDiskCache() {
        return staticImage;
    }

    private DiskLRUCacheImage(final Context context, final int cacheSize,
                              final CompressFormat compressFormat) throws Exception {
        final File cacheDir = getDiskCacheLocation();
        if (cacheDir != null && cacheDir.isDirectory() && cacheDir.canWrite()) {
            diskLruCache = DiskLruCache.open(cacheDir, APP_VERSION, VALUE_COUNT, BUFFER_SIZE);
        } else if (cacheDir != null) {
            if (BuildConfig.DEBUG) {
                Log.d("DiskLRUCacheImage<init>", "Directory " + cacheDir.getAbsolutePath() + " not valid or not writable");
            }
            throw new Exception("Invalid disk cache location " + cacheDir.getAbsolutePath());
        } else {
            if (BuildConfig.DEBUG) {
                Log.d("DiskLRUCacheImage<init>", "Invalid disk cache location NULL");
            }
            throw new Exception("Invalid disk cache location NULL");
        }
    }

    private File getDiskCacheLocation() {
        File loc = null;

		/*
         * Logic to locate SESP disk image cache location
		 *
		 * Step 1: Check if SD Card is mounted
		 * Step 2: If SD Card is found to be mounted and writable then create or use disk cache directory
		 * 		/mnt/sdcard/SESPDiskCache
		 * Step 3: If SD Card is not mounted or found to be read only then
		 * 		use application's data directory itself
		 *
		 */
        try {

            // External SD Card is present and has write access
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                loc = new File(Environment.getExternalStorageDirectory(), ConstantsAstSep.OrderHandlerConstants.SESPDISKCACHEDIRNAME);
            } else {
                // External SD card not attached or not writable
                Log.d("getDiskCacheLocation", "External SD card not attached");
                // Try with app's private data directory
                loc = new File(Environment.getDataDirectory(), ConstantsAstSep.OrderHandlerConstants.SESPDISKCACHEDIRNAME);
            }

            if (loc != null) {
                if (BuildConfig.DEBUG) {
                    Log.d("DiskLRUCacheImage", "Disk cache dir set to " + loc.getAbsolutePath());
                }
                loc.mkdirs();
            }
        } catch (Exception e) {
            writeLog(TAG + " :getDiskCacheLocation() ", e);
        }
        return loc;
    }

    /**
     * Store bitmap data to disk cache
     *
     * @param bitmap {@link Bitmap}
     * @param editor {@link Editor}
     * @return boolean (success status of storage)
     * @throws IOException
     * @throws FileNotFoundException
     */
    private boolean writeBitmapToFile(Bitmap bitmap, Editor editor)
            throws IOException {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(editor.newOutputStream(0), BUFFER_SIZE);
            return bitmap.compress(COMPRESSFORMAT, COMPRESSQUALITY, out);
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * The disk LRU cache is capable of
     * handling keys with a-z,0-9 regex format
     *
     * @param key {@link String}
     * @return String formatted key
     */

    public String convertToRegex(final String key) {
        String keyMod = key;
        try{
        if (key != null && !"".equals(key)) {
            keyMod = key.replace(File.separatorChar, '0');
            keyMod = keyMod.replace(' ', '0');
            keyMod = keyMod.replace('.', '0');
            keyMod = keyMod.replace("\r", "0");
            keyMod = keyMod.replace("\n", "0");
            keyMod = keyMod.toLowerCase(Locale.getDefault());
            keyMod = keyMod.replaceAll("[^\\x00-\\x7f]+", "");
            keyMod = keyMod.replace('-', '0');
            keyMod = keyMod.trim();
        }
        } catch (Exception e) {
            writeLog(TAG + " :convertToRegex() ", e);
        }
        return keyMod;
    }

    /**
     * Publicly exposed method to store bitmap data
     * to disk cache
     *
     * @param key  {@link String}
     * @param data {@link Bitmap}
     */

    public void put(String key, Bitmap data) {
        final String modKey = convertToRegex(key);
        Editor editor = null;
        if (diskLruCache != null) {
            try {
                editor = diskLruCache.edit(modKey);
                if (editor == null) {
                    return;
                }

                if (writeBitmapToFile(data, editor)) {
                    diskLruCache.flush();
                    editor.commit();

                    if (BuildConfig.DEBUG) {
                        Log.d(CACHETESTDISKTAG, "image put on disk cache " + modKey);
                    }
                } else {
                    editor.abort();
                    if (BuildConfig.DEBUG) {
                        Log.d(CACHETESTDISKTAG, "ERROR on: image put on disk cache " + modKey);
                    }
                }
            } catch (final IOException e) {
                if (BuildConfig.DEBUG) {
                    Log.d(CACHETESTDISKTAG, "ERROR on: image put on disk cache " + modKey);
                }
                try {
                    if (editor != null) {
                        editor.abort();
                    }
                } catch (IOException ignored) {
                    writeLog(TAG + " :put() ", ignored);
                }
            }
        }
    }

    /**
     * Publicly exposed method to
     * retrieve the bitmap for the given key
     *
     * @param key {@link String}
     * @return {@link Bitmap}
     */

    public Bitmap getBitmap(final String key) throws IllegalArgumentException {
        final String modKey = convertToRegex(key);
        Bitmap bitmap = null;

        if (BuildConfig.DEBUG) {
            Log.d("getBitmap", "'" + modKey + "'");
        }

        DiskLruCache.Snapshot snapshot = null;
        if (diskLruCache != null) {
            try {
                snapshot = diskLruCache.get(modKey);
                if (snapshot == null) {
                    return null;
                }
                final InputStream in = snapshot.getInputStream(0);
                if (in != null) {
                    final BufferedInputStream buffIn = new BufferedInputStream(in, BUFFER_SIZE);
                    bitmap = compressImage(BitmapFactory.decodeStream(buffIn));
                }
            } catch (final IOException e) {
                writeLog(TAG + " :getBitmap() ", e);
            } finally {
                if (snapshot != null) {
                    snapshot.close();
                }
            }

            if (BuildConfig.DEBUG) {
                Log.d(CACHETESTDISKTAG, bitmap == null ? "" : "image read from disk " + modKey);
            }
        }

        return bitmap;
    }

    /**
     * Compress the bitmap to display in the gridview
     *
     * @param bitmap
     * @return
     */
    private Bitmap compressImage(Bitmap bitmap) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(COMPRESSFORMAT, 70, bos);
            byte[] array = bos.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
        } catch (Exception e) {
            writeLog(TAG + " :compressImage()  ", e);
        }
        return bitmap;
    }

    /**
     * Clean up disk cache
     * <p/>
     * Can be invoked manually by user
     * or programmatically on schedule basis if required
     *
     * @return boolean completion status
     */

    public boolean clearDiskCache() {
        boolean status = false;

        try {
            if (diskLruCache != null) {
                diskLruCache.delete();
                // No need to call close - delete internally calls close
                //diskLruCache.close();
                diskLruCache = null;
                status = true;
            }
        } catch (final Exception ex) {
            writeLog(TAG + " :clearDiskCache() ", ex);
        }
        return status;
    }

    public void adjustCacheMaxSize(final long maxSize) {
        try{
        if (diskLruCache != null) {
            diskLruCache.setMaxSize(maxSize);
        }
        } catch (final Exception ex) {
            writeLog(TAG + " :adjustCacheMaxSize() ", ex);
        }
    }
}
