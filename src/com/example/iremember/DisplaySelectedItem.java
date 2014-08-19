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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

public class DisplaySelectedItem extends Activity {
	TextView tvTittle,tvBody;
	Button btnPlayAudio;
	MediaPlayer m;
	String audioPath,videoPath,imagePath;
	VideoView vdvVideo;
	ImageView imgImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_selected_item);
		
		tvTittle=(TextView) findViewById(R.id.tvTittle);
		tvBody=(TextView) findViewById(R.id.tvBody);
		
		btnPlayAudio=(Button) findViewById(R.id.btnPlayAudio);
		
		vdvVideo= (VideoView) findViewById(R.id.vdvVideo);
		imgImage= (ImageView) findViewById(R.id.imgImage);
		Intent i=getIntent();
		final String data[]=i.getStringArrayExtra("data");
		tvTittle.setText(data[0]);
		tvBody.setText(data[1]);
		audioPath=data[2];
		videoPath=data[3];
		imagePath=data[4];
		if(audioPath!=null)
		audio();
		if(videoPath!=null)
		disPlayVideo();
		if(imagePath!=null){
			displayImage();
		}
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
