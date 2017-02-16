package com.jamendo.renhaojie.hear.activities;

import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

import com.jamendo.renhaojie.hear.databse.DbOpenHelper;
import com.jamendo.renhaojie.hear.providers.DownLoadProvider;
import com.jamendo.renhaojie.hear.utils.FileUtil;

/**
 * Created by Ren Haojie on 2017/2/9.
 */

public class AudioActivity extends AppCompatActivity {

    protected String getLocalPath(String trackId, String url) {
        Cursor downloadInfo = getContentResolver().query(Uri.parse(DownLoadProvider.CONTENT_URI + "/" + trackId),
                DbOpenHelper.DLD_ALL_COLUMNS,
                DbOpenHelper.DLD_SELECTION,
                new String[]{trackId},
                null
        );
        String localPath;
        if (downloadInfo.moveToFirst()) {
            localPath = downloadInfo.getString(downloadInfo.getColumnIndex(DbOpenHelper.DLD_FILE_PATH));
            if (!localPath.isEmpty() && FileUtil.isExists(localPath)) {
                url = localPath;
            }
        }
        return url;
    }
}
