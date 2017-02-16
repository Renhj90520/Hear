package com.jamendo.renhaojie.hear.utils.download;

import java.util.ArrayList;

/**
 * Created by Ren Haojie on 2017/2/14.
 */

public class DownloadManager implements DownloadCompleteListener {
    private ArrayList<DownloadJob> prepareQueue;
    private ArrayList<DownloadJob> downloadedList;
    private ArrayList<DownloadJob> allList;

    private boolean isDownloading = false;

    public void addDownloadJob(DownloadJob job) {
        if (prepareQueue == null) {
            prepareQueue = new ArrayList<>();
        }
        if (allList == null) {
            allList = new ArrayList<>();
        }

        if (!allList.contains(job)) {
            job.setDownloadCompleteListener(this);
            prepareQueue.add(job);
            allList.add(job);
        }
    }

    public void moveIntoDownloaded(DownloadJob job) {
        if (downloadedList == null) {
            downloadedList = new ArrayList<>();
        }

        if (!downloadedList.contains(job)) {
            downloadedList.add(job);
        }
        if (prepareQueue.contains(job)) {
            prepareQueue.remove(job);
        }
    }

    public void doDownload() {
        if (prepareQueue == null || prepareQueue.isEmpty() || isDownloading)
            return;

        DownloadJob job = prepareQueue.get(0);
        job.start();
        isDownloading = true;
    }

    public ArrayList<DownloadJob> getPrepareQueue() {
        return prepareQueue;
    }

    public ArrayList<DownloadJob> getDownloadedList() {
        return downloadedList;
    }

    public ArrayList<DownloadJob> getAllList() {
        return allList;
    }

    @Override
    public void onDownloadComplete(DownloadJob job) {
        isDownloading = false;
        moveIntoDownloaded(job);
        doDownload();
    }
}
