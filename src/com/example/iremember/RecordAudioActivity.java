package com.example.iremember;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class RecordAudioActivity extends Activity {
	Button btnStart, btnStop, btnPlay;
	String outputFile = null;
	MediaRecorder myAudioRecorder;
	SeekBar seekBar;
	Handler mHandler=new Handler();
	MediaPlayer m;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_audio);
		initComponent();
		
	}

	public void initComponent() {
		btnStart = (Button) findViewById(R.id.btnStart);
		btnStop = (Button) findViewById(R.id.btnStop);
		btnStop.setEnabled(false);
		outputFile = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/myRecording"+".3gp";
		myAudioRecorder = new MediaRecorder();
		myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
		myAudioRecorder.setOutputFile(outputFile);
		Start();
		stop();
	
	}
	
	public void Start() {
btnStart.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					myAudioRecorder.prepare();
					myAudioRecorder.start();
				} catch (Exception e) {
				}
				btnStart.setEnabled(true);
				btnStop.setEnabled(true);
				Toast.makeText(getApplicationContext(), "audiorecording started", 3000)
						.show();
				
			}
		});

	}

	public void stop() {
btnStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				myAudioRecorder.stop();
				myAudioRecorder.release();
				myAudioRecorder = null;
				btnStop.setEnabled(false);

				Toast.makeText(getApplicationContext(),
						"audiorecoder saved successfully", 3000).show();
				Intent mIntent=new Intent(getApplicationContext(),AddAudioActivity.class);
				mIntent.putExtra("path", outputFile);
				startActivity(mIntent);
			}
		});
	}


}
