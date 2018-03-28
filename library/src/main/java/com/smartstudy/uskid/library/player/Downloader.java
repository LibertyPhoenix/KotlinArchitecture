package com.smartstudy.uskid.library.player;

import android.os.Environment;

import com.orhanobut.logger.Logger;
import com.smartstudy.uskid.library.utils.ConstUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 缓存音视频
 *
 * @author 王宏杰
 * @date 2018/3/23
 */
public class Downloader {
    private static final String DIR_PATH = Environment.getExternalStorageDirectory() + "/" + ConstUtils.APP_ROOT_DIR; // 下载目录


    private boolean isDownloading = false;


    public interface DownloadListener {
        public void downloadFinish();
    }

    private DownloadListener mDownloadListener;

    public void setDownloadListener(DownloadListener downloadListener) {
        this.mDownloadListener = downloadListener;
    }

    public Downloader() {

    }

    public void download(String address) {
        isDownloading = true;
        new Thread(new DownloadTask(address)).start();
    }

    public boolean isDownloading() {
        return isDownloading;
    }


    private class DownloadTask implements Runnable {
        private URL url; // 目标下载地址
        private File dataFile; // 本地文件
        private File tempFile; // 用来存储每个线程下载的进度的临时文件
        private int totalLength; // 服务端文件总长度

        private DownloadTask(String address) {
            try {
                url = new URL(address);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } // 记住下载地址
            dataFile = new File(
                    DIR_PATH, address.substring(
                    address
                            .lastIndexOf("/") + 1
            )
            ); // 本地文件地址
            tempFile = new File(dataFile.getAbsolutePath() + ".temp"); // 临时文件地址(用来记录下载进度)
        }

        @Override
        public void run() {
            if (download(url, totalLength, tempFile)) {
                mDownloadListener.downloadFinish();
                tempFile.renameTo(dataFile);
                isDownloading = false;
            }
        }


    }

    private boolean download(URL url, int totalLength, File tempFile) {
        OutputStream os = null;
        InputStream is = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(3000);
            totalLength = conn.getContentLength(); // 获取服务端发送过来的文件长度
            is = conn.getInputStream(); // 获取连接的输入流
            os = new BufferedOutputStream(
                    new FileOutputStream(tempFile),
                    1024 * 32
            );
            final byte[] bytes = new byte[1024 * 32];
            int count;
            int current = 0;
            while ((count = is.read(bytes, 0, 1024 * 32)) != -1) {
                os.write(bytes, 0, count);
                current += count;
                Logger.d("下载进度" + current * 100 / totalLength);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


}
