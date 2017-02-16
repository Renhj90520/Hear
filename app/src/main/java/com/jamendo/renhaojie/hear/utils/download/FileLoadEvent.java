package com.jamendo.renhaojie.hear.utils.download;

/**
 * Created by Ren Haojie on 2017/2/13.
 */

public class FileLoadEvent {
    long mTotalSize;
    long mBytesLoaded;

    public FileLoadEvent(long bytesLoaded, long totalSize) {
        mBytesLoaded = bytesLoaded;
        mTotalSize = totalSize;
    }

    public long getBytesLoaded() {
        return mBytesLoaded;
    }

    public long getTotalSize() {
        return mTotalSize;
    }
}
