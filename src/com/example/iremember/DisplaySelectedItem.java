package com.example.iremember;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class DisplaySelectedItem extends Activity {
	TextView tvTittle,tvBody,tvTime,tvDisplayLocation;
	ImageButton btnBack,btnEdit;
	Button btnPlayAudio;
	MediaPlayer m;
	String audioPath,videoPath,imagePath,time,location;
	VideoView vdvVideo;
	ImageView imgImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.display_selected_item);
		tvDisplayLocation=(TextView) findViewById(R.id.tvDisplayLocation);
		tvTittle=(TextView) findViewById(R.id.tvTittle);
		tvBody=(TextView) findViewById(R.id.tvBody);
		tvTime=(TextView) findViewById(R.id.TVTime);
		btnPlayAudio=(Button) findViewById(R.id.btnPlayAudio);
		btnBack=(ImageButton) findViewById(R.id.btnBack);
		btnEdit=(ImageButton) findViewById(R.id.btnEdit);
		vdvVideo= (VideoView) findViewById(R.id.vdvVideo);
		imgImage= (ImageView) findViewById(R.id.imgImage);
		Intent i=getIntent();
		final String data[]=i.getStringArrayExtra("data");
		tvTittle.setText(data[0]);
		tvBody.setText(data[1]);
		tvTime.setText(data[2]);
		audioPath=data[3];
		videoPath=data[4];
		imagePath=data[5];
		location=data[6];
		if(audioPath!=null)
		audio();
		if(videoPath!=null)
		disPlayVideo();
		if(imagePath!=null){
			displayImage();
		}
		setUpdate();
		tvDisplayLocation.setText(location);
	}
	public void back(){
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent= new Intent(getApplicationContext(),FirstScreen.class);
				startActivity(intent);
				
			}
		});
	}
public void audio(){
	btnPlayAudio.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			try{
			m= new MediaPlayer();
			m.setDataSource(audioPath);
			m.prepare();
			m.start();}catch(Exception e){}
		}
	});
}
public void disPlayVideo(){
	vdvVideo.setVideoPath(videoPath);
	vdvVideo.setOnTouchListener(new OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			
			vdvVideo.start();
			return false;
		}
	});
}
public void displayImage(){
	Bitmap bit = BitmapFactory.decodeFile(imagePath);
	imgImage.setImageBitmap(bit);
}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.display_selected_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void setUpdate(){
		Intent i=getIntent();
		final int id=i.getIntExtra("id", -1);
		btnEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(getApplicationContext(),MainActivity.class);
				mIntent.putExtra("id", id);
				String data[]={tvTittle.getText().toString(),tvBody.getText().toString(),
						tvTime.getText().toString(),audioPath,videoPath,imagePath};
				mIntent.putExtra("data",data );
				startActivity(mIntent);
			}
		});
	}
}
