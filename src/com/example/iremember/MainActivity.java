package com.example.iremember;

import java.io.File;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	public static final String FIRST_VALUE_ID = "first_value_id";
	public static final String IMAGE_KEY = "second_value_id";
	private static final int GET_VALUES_VIDEO_ID = 1;
	private static final int GET_VALUES_IMAGE_ID = 2;
	String temp;
	EditText edtTittle;
	EditText edtBody;
	Button btnCreate,btnAddVideo,btnAddAudio,btnAddLocation, btnAddPhoto;
	TextView tvTime,tvLatitude;
	private MySQLiteOpenHelper dataHelper;
	private Cursor cusor;
	private SimpleAdapter adapter;
	LocationManager locationManager;
	Location location;
	double latitude;double longitude;
	EditText edtImageName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		

		
		setContentView(R.layout.activity_main);
		

		File newVideoFolder= new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/IRemember3/Video");
		 if (newVideoFolder.exists()==false){
			Toast.makeText(getApplicationContext(), "A",Toast.LENGTH_SHORT).show();
			 if(newVideoFolder.mkdirs())
				 Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
		 }
		 File newAudioFolder= new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/IRemember3/Audio");
		 if (newAudioFolder.exists()==false){
			 if(newAudioFolder.mkdirs())
				 Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
		 }
		 File newPictureFolder= new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/IRemember3/Picture");
		 if (newPictureFolder.exists()==false){
			 if(newPictureFolder.mkdirs())
				 Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
		 }
		dataHelper = new MySQLiteOpenHelper(MainActivity.this);
		edtTittle = (EditText) findViewById(R.id.edtTittle);
		edtTittle.setText("");
		edtBody = (EditText) findViewById(R.id.edtBody);
		edtBody.setText("");
		btnAddAudio=(Button) findViewById(R.id.btnAddAudio);
		btnAddVideo=(Button) findViewById(R.id.btnAddVideo);
		btnAddPhoto= (Button) findViewById(R.id.btnAddPhoto);
		btnCreate = (Button) findViewById(R.id.btnCreate);
		
		edtImageName= (EditText) findViewById(R.id.edtImageName);
		
		tvTime = (TextView) findViewById(R.id.tvTime);
		tvLatitude=(TextView) findViewById(R.id.tvLatitude);
		btnAddLocation=(Button) findViewById(R.id.btnGetLocation);
		locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		Calendar c = Calendar.getInstance();
		String time = c.get(Calendar.HOUR) + ": " + c.get(Calendar.MINUTE)
				+ "   " + c.get(Calendar.DATE) + " - " + c.get(Calendar.MONTH)
				+ " - " + c.get(Calendar.YEAR);
		tvTime.setText(time);

		btnCreate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tittle ="";
				String body = "";
				Intent i=getIntent();
				String audioPath=i.getStringExtra("audioPath");
				btnCreate.setText(audioPath);
				tittle = edtTittle.getText().toString();
				body = edtBody.getText().toString();
				String time = tvTime.getText().toString();
				if(!tittle.equals("")){
				Record r = new Record(tittle, body, time,audioPath,"videoPath","imagePath");
				dataHelper.INSERT_RECORD(r);
				dataHelper.close();
				Toast.makeText(MainActivity.this, "created successfuly", 30000)
						.show();
				Intent mIntent = new Intent(getApplicationContext(),
						FirstScreen.class);
				startActivity(mIntent);
				}else {
					AlertDialog alertdialog= new AlertDialog.Builder(MainActivity.this).create();
					alertdialog.setTitle("iRemember");
					alertdialog.setMessage("tittle should be filled");
					alertdialog.setButton("OK", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							
						}
					});
					alertdialog.show();
				}

			}
		});
		addVideo();
		addAudio();
		addPhoto();
		getLocation();
		
	}
	public void addVideo(){
		btnAddVideo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent addVideoIntent= new Intent(getApplicationContext(),AddVideoActivity.class);
//				startActivity(addVideoIntent);
				startActivityForResult(addVideoIntent, GET_VALUES_VIDEO_ID);
			}
		});
	}
	
	public void addAudio(){
		btnAddAudio.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mIntent= new Intent(getApplicationContext(),AddAudioActivity.class);
				startActivity(mIntent);
				
			}
		});
	}
	public void addPhoto(){
		btnAddPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent addPhotoIntent= new Intent(getApplicationContext(),AddImageActivity.class);
				startActivityForResult(addPhotoIntent, GET_VALUES_IMAGE_ID);
			
			}
		});
	}

	
	public void getLocation(){
		btnAddLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//neu cho 2 toa do ben duoi fix san nay thi no chay dung roi van de la lay latitude va longitude
//				latitude = 21.0333330; 
//				longitude = 105.8500000; 
				// t bi loi o 2 cai dong ben duoi day nay no loi la NullPointer
				latitude=location.getLatitude();
				longitude=location.getLongitude();
				String label = "I'm Here!"; 
				String uriBegin = "geo:" + latitude + "," + longitude; 
				String query = latitude + "," + longitude + "(" + label + ")"; 
				String encodedQuery = Uri.encode(query); 
				String uriString = uriBegin + "?q=" + encodedQuery + "&z=16"; 
				Uri uri = Uri.parse(uriString); 
				Toast.makeText(getApplicationContext(), uriBegin, 5000);
				Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri); 
//vi may ao ko co googlemap nen t tam comment lai//startActivity(mapIntent);
				
			}

		});

	}
	 @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        switch (requestCode) {
	        case GET_VALUES_VIDEO_ID: {
	            if (Activity.RESULT_OK == resultCode) {
	              temp= data.getStringExtra(FIRST_VALUE_ID);
	                setValues();
	            }
	            break;
	        }
	        case GET_VALUES_IMAGE_ID: {
	            if (Activity.RESULT_OK == resultCode) {
	              temp= data.getStringExtra(IMAGE_KEY );
	                setValues();
	            }
	            break;
	        }
	        }
	        super.onActivityResult(requestCode, resultCode, data);
	    }


	    protected void setValues() {
	       edtImageName.setText(temp);
	    }
	
	
}
