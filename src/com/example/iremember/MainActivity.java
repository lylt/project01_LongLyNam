package com.example.iremember;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final String FIRST_VALUE_ID = "first_value_id";
	public static final String IMAGE_KEY = "second_value_id";
	public static final String AUDIO_KEY = "third_value_id";
	
	private static final int GET_VALUES_VIDEO_ID = 1;
	private static final int GET_VALUES_IMAGE_ID = 2;
	public static final int GET_VALUES_AUDIO_ID = 3;
	EditText edtTittle, edtImageName, edtBody;
	Button btnCreate, btnAddVideo, btnAddAudio, btnAddLocation, btnAddPhoto,
			btnUpdate, btnCancel;
	TextView tvTime, tvLatitude;
	private MySQLiteOpenHelper dataHelper;
	private Cursor cusor;
	private SimpleAdapter adapter;
	LocationManager locationManager;
	Location location;
	String videoPath, imagePath, audioPath;
	GPSTracker gps;
	double latitude, longitude;
	String data[] = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		createDirectory();
		initComponent();
		addVideo();
		addAudio();
		addPhoto();
		getLocation();
		CreateNew();
		update();
		cancel();
	}

	private void createDirectory() {
		File newVideoFolder = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/IRemember3/Video");
		if (newVideoFolder.exists() == false) {
			if (newVideoFolder.mkdirs())
				Toast.makeText(getApplicationContext(), "Successful",
						Toast.LENGTH_SHORT).show();
		}
		File newAudioFolder = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/IRemember3/Audio");
		if (newAudioFolder.exists() == false) {
			if (newAudioFolder.mkdirs())
				Toast.makeText(getApplicationContext(), "Successful",
						Toast.LENGTH_SHORT).show();
		}
		File newPictureFolder = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ "/IRemember3/Picture");
		if (newPictureFolder.exists() == false) {
			if (newPictureFolder.mkdirs())
				Toast.makeText(getApplicationContext(), "Successful",
						Toast.LENGTH_SHORT).show();
		}
	}

	private void initComponent() {
		dataHelper = new MySQLiteOpenHelper(MainActivity.this);
		edtTittle = (EditText) findViewById(R.id.edtTittle);
		edtTittle.setText("");
		edtBody = (EditText) findViewById(R.id.edtBody);
		edtBody.setText("");
		btnAddAudio = (Button) findViewById(R.id.btnAddAudio);
		btnAddVideo = (Button) findViewById(R.id.btnAddVideo);
		btnAddPhoto = (Button) findViewById(R.id.btnAddPhoto);
		btnCreate = (Button) findViewById(R.id.btnCreate);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		btnUpdate = (Button) findViewById(R.id.btnUpdate);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		tvTime = (TextView) findViewById(R.id.tvTime);
		btnAddLocation = (Button) findViewById(R.id.btnGetLocation);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		btnUpdate.setEnabled(false);
		setTimeText();
		edtBody.setVisibility(View.INVISIBLE);
	}

	public void setTimeText() {
		Calendar c = Calendar.getInstance();
		String time = c.get(Calendar.HOUR) + ": " + c.get(Calendar.MINUTE)
				+ "   " + c.get(Calendar.DATE) + " - " + c.get(Calendar.MONTH)
				+ " - " + c.get(Calendar.YEAR);
		tvTime.setText(time);
	}

	public void update() {
		Intent i=getIntent();
		final int id=i.getIntExtra("id", -1);
		if(id!=-1){
			btnUpdate.setEnabled(true);
			btnCreate.setEnabled(false);
			data=i.getStringArrayExtra("data");
			tvTime.setText(data[2]);
			edtTittle.setText(data[0]);
			edtBody.setText(data[1]);
		}
		
		btnUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String audioPath,videoPath,imagePath,tittle,body;
				audioPath=data[3];
				videoPath=data[4];
				imagePath=data[5];
				Intent i = getIntent();
				audioPath = i.getStringExtra("audioPath");
				btnCreate.setText(audioPath);
				tittle = edtTittle.getText().toString();
				body = edtBody.getText().toString();
				String time = tvTime.getText().toString();
				String location = "Latitude: " + latitude + " longitude: "
						+ longitude;
				if (!tittle.equals("")) {
					Record r = new Record(tittle, body, time, audioPath,
							videoPath, imagePath, location);
					dataHelper.UPDATE_Record(r,id);
					dataHelper.close();
					Toast.makeText(MainActivity.this, "created successfuly",
							30000).show();
					Intent mIntent = new Intent(getApplicationContext(),
							FirstScreen.class);
					startActivity(mIntent);
				} else {
					AlertDialog alertdialog = new AlertDialog.Builder(
							MainActivity.this).create();
					alertdialog.setTitle("iRemember");
					alertdialog.setMessage("tittle should be filled");
					alertdialog.setButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
					alertdialog.show();
				}

			}
		});
	}

	public void CreateNew() {
		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tittle = "";
				String body = "";
				Intent i = getIntent();
				tittle = edtTittle.getText().toString();
				body = edtBody.getText().toString();
				String time = tvTime.getText().toString();
				String location = "Latitude: " + latitude + " longitude: "
						+ longitude;
				if (!tittle.equals("")) {
					Record r = new Record(tittle, body, time, audioPath,
							videoPath, imagePath, location);
					dataHelper.INSERT_RECORD(r);
					dataHelper.close();
					Toast.makeText(MainActivity.this, "created successfuly",
							30000).show();
					Intent mIntent = new Intent(getApplicationContext(),
							FirstScreen.class);
					startActivity(mIntent);
				} else {
					AlertDialog alertdialog = new AlertDialog.Builder(
							MainActivity.this).create();
					alertdialog.setTitle("iRemember");
					alertdialog.setMessage("tittle should be filled");
					alertdialog.setButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
					alertdialog.show();
				}

			}
		});
	}


	public void addVideo() {
		btnAddVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent addVideoIntent = new Intent(getApplicationContext(),
						AddVideoActivity.class);
				// startActivity(addVideoIntent);
				startActivityForResult(addVideoIntent, GET_VALUES_VIDEO_ID);
			}
		});
	}

	public void addAudio() {
		btnAddAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent addAudioIntent= new Intent(getApplicationContext(),AddAudioActivity.class);
				startActivityForResult(addAudioIntent,GET_VALUES_AUDIO_ID);		
			}
		});
	}

	public void addPhoto() {
		btnAddPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent addPhotoIntent = new Intent(getApplicationContext(),
						AddImageActivity.class);
				startActivityForResult(addPhotoIntent, GET_VALUES_IMAGE_ID);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case GET_VALUES_VIDEO_ID: {
			if (Activity.RESULT_OK == resultCode) {
				videoPath = data.getStringExtra(FIRST_VALUE_ID);
			}
			break;
		}
		case GET_VALUES_IMAGE_ID: {
			if (Activity.RESULT_OK == resultCode) {
				imagePath = data.getStringExtra(IMAGE_KEY);
			}
			break;
		}
		 
        case GET_VALUES_AUDIO_ID: {
            if (Activity.RESULT_OK == resultCode) {
              audioPath= data.getStringExtra(AUDIO_KEY);
              edtBody.setText(audioPath);
            }
            break;
        }
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void getLocation() {
		btnAddLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				gps = new GPSTracker(MainActivity.this);

				// check if GPS enabled
				if (gps.canGetLocation()) {
					latitude = gps.getLatitude();
					longitude = gps.getLongitude();
					// latitude = 20.213073;
					// longitude = 105.661011;
					String label = "I'm Here!";
					String uriBegin = "geo:" + latitude + "," + longitude;
					String query = latitude + "," + longitude + "(" + label
							+ ")";
					String encodedQuery = Uri.encode(query);
					String uriString = uriBegin + "?q=" + encodedQuery
							+ "&z=16";
					Uri uri = Uri.parse(uriString);
					Toast.makeText(getApplicationContext(), uriBegin, 5000);
					Intent mapIntent = new Intent(
							android.content.Intent.ACTION_VIEW, uri);
					getAddress();
					 //startActivity(mapIntent);
				} else {
					gps.showSettingsAlert();
				}

			}
		});

	}

	public void getAddress() {
		try {
			Geocoder geocoder = new Geocoder(MainActivity.this, Locale.ENGLISH);
			double x = latitude;
			double y = longitude;
			List<Address> addresses = geocoder.getFromLocation(x, y, 1);
			StringBuilder str = new StringBuilder();
			if (geocoder.isPresent()) {
				Toast.makeText(getApplicationContext(), "geocoder present",
						Toast.LENGTH_SHORT).show();
				Address returnAddress = addresses.get(0);

				String localityString = returnAddress.getLocality();
				String city = returnAddress.getCountryName();
				String region_code = returnAddress.getCountryCode();
				String zipcode = returnAddress.getPostalCode();

				str.append(localityString + "");
				str.append(city + "" + region_code + "");
				str.append(zipcode + "");
				Toast.makeText(getApplicationContext(), city,
						Toast.LENGTH_SHORT).show();

			} else {
				Toast.makeText(getApplicationContext(), "geocoder not present",
						Toast.LENGTH_SHORT).show();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block

			Log.e("tag", e.getMessage());
		}
	}
	public void cancel(){
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(edtBody.getVisibility()==View.VISIBLE){
					edtBody.setVisibility(View.GONE);
				}else{
					edtBody.setVisibility(View.VISIBLE);
				}
				
			}
		});
	}
}
