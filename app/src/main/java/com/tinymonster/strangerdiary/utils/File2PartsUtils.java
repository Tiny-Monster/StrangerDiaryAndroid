package com.tinymonster.strangerdiary.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by TinyMonster on 17/01/2019.
 */

public class File2PartsUtils {
    public static List<MultipartBody.Part> files2Parts(String key,
                                                List<String> filePaths) {
        List<MultipartBody.Part> parts = new ArrayList<>(filePaths.size());
        for (String filePath : filePaths) {
            File file = new File(filePath);
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part part = MultipartBody.Part.
                    createFormData(key, file.getName(), requestBody);
            parts.add(part);
        }
        return parts;
    }
}
