package org.jackhuang.myui.util;

import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import cpw.mods.fml.common.FMLLog;

public class HttpDownloader implements Runnable {
    private static final int MAX_BUFFER_SIZE = 2048;
    public static final String STATUSES[] = {"Downloading",
    "Paused", "Complete", "Cancelled", "Error"};
    public static final int DOWNLOADING = 0;
    public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;
    
    private URL url;
    private int size;
    private int downloaded;
    private ActionListener listener;
    private String filePath;
    public HttpDownloader(URL url, String filePath, ActionListener l) {        
        this.url = url;
        size = -1;
        downloaded = 0;
        this.filePath = filePath;
        this.listener = l;
    }
    public String getUrl() {
        return url.toString();
    }
    public long getSize() {
        return size;
    }
    public float getProgress() {
        return ((float) downloaded / size) * 100;
    }
    private String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }
    
    @Override
    public void run() {
        RandomAccessFile file = null;
        InputStream stream = null;
        
            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();
            
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Range",
                    "bytes=" + downloaded + "-");
            connection.connect();
            if (connection.getResponseCode() / 100 != 2) {
                throw new Exception();
            }
            int contentLength = connection.getContentLength();
            if (contentLength < 1) {
                throw new Exception();
            }
            if (size == -1) {
                size = contentLength;
            }
            file = new RandomAccessFile(filePath, "rw");
            file.seek(downloaded);
            
            stream = connection.getInputStream();
            while (true) {
                byte buffer[] = new byte[MAX_BUFFER_SIZE];
                int read = stream.read(buffer);
                if (read == -1)
                    break;
                file.write(buffer, 0, read);
                downloaded += read;
            }
            if(listener != null)
                listener.actionPerformed(null);
    }
}