package com.example.iremember;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class AddImageActivity extends Activity {

	ImageView image;
	static final int REQUEST_IMAGE_CAPTURE = 200;
	private static final int RESULT_LOAD_IMAGE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	String path;
	static File mediaFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addimage);
		image = (ImageView) findViewById(R.id.imgImage);
		Button btnChooseImage = (Button) findViewById(R.id.btnChooseImage);
		Button btnTakePicture = (Button) findViewById(R.id.btnTakePicture);
		Button btnBack= (Button) findViewById(R.id.btnBack);
		btnTakePicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				takePicture();
			}
		});
		btnChooseImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				chooseImage();
			}
		});
		
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent main= new Intent();
				main.putExtra(MainActivity.IMAGE_KEY, path);
				setResult(Activity.RESULT_OK, main);
				finish();
			}
		});
	}

	// using camera to take a picture
	public void takePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,getOutputImageFile(MEDIA_TYPE_IMAGE));
		path=getOutputImageFile(MEDIA_TYPE_IMAGE).getAbsolutePath();
		startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
	}

	// pick an image from gallery.
	public void chooseImage() {
		Intent i = new Intent(Intent.ACTION_PICK,
				android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

		startActivityForResult(i, RESULT_LOAD_IMAGE);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			
			image.setImageBitmap(imageBitmap);
		}

		else if (requestCode == RESULT_LOAD_IMAGE) {
			if (resultCode == RESULT_OK) {
				Uri _uri = data.getData();
				// User had pick an image.
				Cursor cursor = getContentResolver()
						.query(_uri,
								new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
								null, null, null);
				cursor.moveToFirst();
				// Link to the image
				String photoPath = cursor.getString(0);
				cursor.close();
				path=photoPath;
				Bitmap bit = BitmapFactory.decodeFile(photoPath);
				image.setImageBitmap(bit);
				Toast.makeText(this, "OKe", Toast.LENGTH_SHORT).show();
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	private static File getOutputImageFile(int type) {

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		String videoName=timeStamp + "_";
		File mediaFileDir= new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/IRemember3/Picture/");
	
		if (type == MEDIA_TYPE_IMAGE) {
			 try {
			 mediaFile = File.createTempFile(videoName,".png",mediaFileDir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//mediaFile = new File(mediaStorageDir.getPath());
		} else {
			return null;
		}
		return mediaFile;

	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_image, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.addimage, container,
					false);
			return rootView;
		}
	}

}
