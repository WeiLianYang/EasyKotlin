/*
 * Copyright WeiLianYang
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.william.easykt.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;

import androidx.core.content.FileProvider;

import com.william.base_component.utils.LogExtKt;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * author : WilliamYang
 * date : 2021/12/17 14:42
 * description : 图片
 */
public class ImagePickUtil {

    /**
     * 处理回调
     */
    public interface UriCallback {
        void returnUri(Uri uri);
    }

    /**
     * 针对谷歌相册返回的图片 uri，先保存到缓存目录，然后再返回对应的 uri
     */
    public static void getPickImageUri(Activity context, Uri uri, UriCallback callback) {
        String googlePrefix = "content://com.google.android.apps.photos.contentprovider";
        if (uri.toString().startsWith(googlePrefix)) {
            // 处理谷歌相册返回的图片，谷歌相册返回的uri：
            // content://com.google.android.apps.photos.contentprovider/-1/1/content%3A%2F%2Fmedia%2Fexternal%2Fimages%2Fmedia%2F47199/ORIGINAL/NONE/image%2Fjpeg/1975164776
            new Thread(() -> {
                Uri result = saveImageToCache(context, uri);
                context.runOnUiThread(() -> callback.returnUri(result));
            }).start();
        } else {
            callback.returnUri(uri);
        }
    }

    /**
     * 将谷歌相册图片保存到存储目录，然后返回 uri
     */
    private static Uri saveImageToCache(Context context, Uri uri) {
        String imageName = System.currentTimeMillis() + ".jpg";
        String parent;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            parent = context.getExternalCacheDir().getAbsolutePath();
        } else {
            parent = context.getCacheDir().getAbsolutePath();
        }
        String path = parent + File.separator + imageName;
        Uri result = null;
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            // 拷贝到缓存目录
            copyInputStream(inputStream, path);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                result = FileProvider.getUriForFile(
                        context, context.getPackageName() + ".fileprovider",
                        new File(parent, imageName)
                );
            } else {
                result = Uri.fromFile(new File(path));
            }
            LogExtKt.logD("pick image uri===> " + result.toString(), LogExtKt.TAG);
        } catch (Exception e) {
            LogExtKt.logE(e.getMessage(), LogExtKt.TAG);
        }
        return result;
    }

    /**
     * 流读写复制文件
     *
     * @param inputStream 输入流
     * @param outputPath  输出地址
     */
    private static void copyInputStream(InputStream inputStream, String outputPath) {
        LogExtKt.logD("copy input stream begin...", LogExtKt.TAG);

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(outputPath);
            byte[] bytes = new byte[1024];
            int num;
            while ((num = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, num);
                outputStream.flush();
            }
        } catch (Exception e) {
            LogExtKt.logE(e.getMessage(), LogExtKt.TAG);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                LogExtKt.logD("copy input stream end...", LogExtKt.TAG);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Uri getOutputUri(Context context, Uri uri) {
        // 获取输入图片uri的媒体类型
        String mimeType = context.getContentResolver().getType(uri);
        // 创建新的图片名称
        String imageName = System.currentTimeMillis() + "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        Uri outputUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 及以上获取图片uri
            ContentValues values = new ContentValues(3);
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, imageName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, mimeType);
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM);

            outputUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            outputUri = Uri.fromFile(new File(context.getExternalCacheDir().getAbsolutePath(), imageName));
        }
        LogExtKt.logD("getOutputUri===> " + outputUri.toString(), LogExtKt.TAG);
        return outputUri;
    }

    /**
     * 针对 Google 相册图片获取路径
     */
    public static Uri getGoogleImageUrl(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return insertToMediaStore(context, bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 将图片流读取出来保存到手机本地相册中
     **/
    public static Uri insertToMediaStore(Context inContext, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), bitmap, "temp", null);
        return Uri.parse(path);
    }

}
