package com.example.iremember;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

public class AddVideoActivity extends Activity {


	private static final int VIDEO_CAPTURE = 101;
	static final int REQUEST_IMAGE_CAPTURE = 200;
	private Uri fileUri;
	static File mediaFile;
	VideoView v;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
	private static final int RESULT_LOAD_IMAGE = 100;
	protected static final int RESULT_LOAD_VIDEO = 50;
	
	private boolean bVideoIsBeingTouched = false;
	String path;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_video);
	    v= (VideoView) findViewById(R.id.vdvVideo);
	    v.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(!bVideoIsBeingTouched){
					playVideo(fileUri.getPath());
					bVideoIsBeingTouched=true;
				}
				else{
				pauseVideo();
				bVideoIsBeingTouched=false;
				}
				
				return false;
			}
		});
	    
	    Button btnBack= (Button) findViewById(R.id.btnBack);
	    btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent main= new Intent();
				main.putExtra(MainActivity.FIRST_VALUE_ID, path);
				setResult(Activity.RESULT_OK, main);
				finish();
			}
		});
		Button btnCaptureVideo = (Button) findViewById(R.id.btnCaptureVideo);
		btnCaptureVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				captureVideo();
			}
		});
		Button btnChooseVideo = (Button) findViewById(R.id.btnChooseVideo);
		btnChooseVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectVideo();
			}
		});
	}

	public void captureVideo() {
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, VIDEO_CAPTURE);
		path= fileUri.getPath();
	}

	public void selectVideo() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(i, RESULT_LOAD_VIDEO);
	}

	public void playVideo(String path) {
		
		v.setVideoPath(path);
		v.start();
	}
	public void pauseVideo(){
		v.pause();
	}
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VIDEO_CAPTURE) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "Video saved to:\n" + data.getData(),
						Toast.LENGTH_LONG).show();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Video recording cancelled.",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Failed to record video",
						Toast.LENGTH_LONG).show();
			}
		}
		else 
			if (requestCode == RESULT_LOAD_VIDEO) {
			if (resultCode == RESULT_OK) {
				Uri _uri = data.getData();
				// User had pick an image.
				Cursor cursor = getContentResolver()
						.query(_uri,
								new String[] { android.provider.MediaStore.Video.VideoColumns.DATA },
								null, null, null);
				cursor.moveToFirst();
				// Link to the image
				String photoPath = cursor.getString(0);
				cursor.close();
				path=photoPath;
				v.setVideoPath(photoPath);
				v.start();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Video recording cancelled.",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Failed to record video",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private static File getOutputMediaFile(int type) {
		File mediaStorageDir = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "myvideo.mp4");
		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
						+ IMAGE_DIRECTORY_NAME + " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;

		if (type == MEDIA_TYPE_VIDEO) {
			// mediaFile = new File(mediaStorageDir.getPath() + File.separator
			// + "VID_" + timeStamp + ".mp4");
			mediaFile = new File(mediaStorageDir.getPath());
		} else {
			return null;
		}

		return mediaFile;

	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on scren orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_video, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}