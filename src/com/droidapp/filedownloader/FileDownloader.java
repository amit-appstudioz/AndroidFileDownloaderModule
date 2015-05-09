package com.droidapp.filedownloader;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class FileDownloader {

	int mDownloadedFileSize = 0;
	int mTotalFileSize = 0;
	String dwnload_file_path ;
	String fileExtention;
	String downloadedFileName;
	String localStoreFilePath;
    Handler mHandler;
    private static final String TAG=FileDownloader.class.getSimpleName();
    
    private OnFileDownloadProgressChangedListener mDownloadChangedListener;
    public  boolean mStopDownload=false;
    

	public FileDownloader(String filepath,OnFileDownloadProgressChangedListener listener) {
        this.mHandler=new Handler(Looper.getMainLooper());
		this.dwnload_file_path=filepath;
		this.fileExtention=filepath.substring(filepath.lastIndexOf("."),filepath.length());
        this.mDownloadChangedListener=listener;
	}
	
	/**Start doownloading of the file*/
	public void startDownload(){
        /**start downloading file*/
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mDownloadChangedListener.onDownloadStart();
            }
        });
		downloadFile();
	}
	
	public void stopDownloading(){
		this.mStopDownload=true;
	}
	
	int downloadFile(){
        
        try {
            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
 
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);            
            urlConnection.connect();
 
            //set the path where we want to save the file           
            File SDCardRoot = Environment.getExternalStorageDirectory(); 
            //create a new file, to save the downloaded file 
            //file path of the downloaded path
            downloadedFileName="Survival_App"+SystemClock.elapsedRealtime()+fileExtention;
            
            File file = new File(SDCardRoot,downloadedFileName);
            //Assigning the local full file path
            localStoreFilePath=file.getAbsolutePath();
            
            FileOutputStream fileOutput = new FileOutputStream(file);
 
           
            InputStream inputStream = urlConnection.getInputStream();


            mTotalFileSize = urlConnection.getContentLength();
            //create a buffer...
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
 
            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
                mDownloadedFileSize += bufferLength;
                /**Check whether to cancel this download or not*/
                if(mStopDownload){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mDownloadChangedListener.onFailure();

                        }
                    });
                    break;
                }

                /**
                 * Post the current progress on the service*/
                 mHandler.post(new Runnable() {
                     @Override
                     public void run() {
                         float per = ((float) mDownloadedFileSize / mTotalFileSize) * 100;
                         mDownloadChangedListener.onProgressChanged(per);
                     }
                 });
            }
            //close the output stream when complete //
            fileOutput.close();
            if(!mStopDownload){
                /** Notify through the listener that the file is downloaded*/
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mDownloadChangedListener.onFileDownloaded(localStoreFilePath);
                    }
                });
            }
        }
        catch (Exception e) {
            showError("Error : Please check your internet connection " + e);
        }
        return  0;
    }
     
    

	void showError(final String err){
	    mHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.e(TAG,""+err);
            }
        });
    }

	
    /**This is to track the downloading status */
    public interface OnFileDownloadProgressChangedListener{
        public void onDownloadStart();
        public void onProgressChanged(float currentProgress);
        public void onFileDownloaded(String currentPath);
        public void onFailure();
    }
}


