package com.droidapp.filedownloader;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.droidapp.filedownloader.FileDownloader.OnFileDownloadProgressChangedListener;


public class MainActivity extends Activity implements OnClickListener{

	private FileDownloader mFileDownloader;
	private Button mStartDownload,mStopDownload;
	private DownloadFileAsync mDownloadFileAsync;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mStartDownload=(Button)findViewById(R.id.btn_startdownload);
        mStopDownload=(Button)findViewById(R.id.btn_stopdownload);   
        mStartDownload.setOnClickListener(this);
        mStopDownload.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
    	// TODO Auto-generated method stub
    	switch (v.getId()) {
		case R.id.btn_startdownload:
			mDownloadFileAsync=new DownloadFileAsync();
			mDownloadFileAsync.execute("URL of the file to download");
			break;
		case R.id.btn_stopdownload:
			mDownloadFileAsync.stopDownload();
			break;
		default:
			break;
		}
    }
    
    class DownloadFileAsync extends AsyncTask<String,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
        	startDownload(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }
        public void startDownload(String url){
        	mFileDownloader =new FileDownloader(url,mChangedListener);
            mFileDownloader.startDownload();
        }
        public void stopDownload(){
        	mFileDownloader.stopDownloading();
        }
    }
    
    /**
     * Get the download progress callbacks here
     * */
    
    OnFileDownloadProgressChangedListener mChangedListener=new OnFileDownloadProgressChangedListener() {
		
		@Override
		public void onProgressChanged(float currentProgress) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onFileDownloaded(String currentPath) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onFailure() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onDownloadStart() {
			// TODO Auto-generated method stub
			
		}
	};
}
