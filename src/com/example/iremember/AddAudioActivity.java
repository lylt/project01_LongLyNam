package com.example.iremember;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class AddAudioActivity extends Activity {
	Button btnRecordAudio, btnChooseAudio, btnPlay,btnBackToMainActivity;
	MediaPlayer m;
	SeekBar seekBar;
	String outputFile = null;
	Intent i;
	Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_audio);
		btnRecordAudio = (Button) findViewById(R.id.btnRecord);
		btnChooseAudio = (Button) findViewById(R.id.btnChooseAudio);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		btnPlay = (Button) findViewById(R.id.btnPlay);
		btnBackToMainActivity=(Button) findViewById(R.id.btnBackToMainActivity);
//		seekBar.setVisibility(View.INVISIBLE);
//		btnPlay.setVisibility(View.INVISIBLE);
		recordAudio();
		chooseAudio();
		play();
		backToMain();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_audio, menu);
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

	public void recordAudio() {
		btnRecordAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getApplicationContext(),
						RecordAudioActivity.class);
				startActivity(mIntent);

			}
		});
	}

	public void chooseAudio() {
		btnChooseAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent_upload = new Intent();
				intent_upload.setType("audio/3gp");
				intent_upload.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent_upload, 1);

			}
		});

	}

	// override onActivityResult
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {

			if (resultCode == RESULT_OK) {

				// the selected audio.
				Uri uri = data.getData();
				outputFile=uri.toString();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public String receiveIntent() {
		i = getIntent();
		return i.getStringExtra("path");

	}

	public void play() {
//		btnPlay.setVisibility(View.VISIBLE);
//		seekBar.setVisibility(View.VISIBLE);
		btnPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					if(outputFile==null)
					outputFile = receiveIntent();
					boolean isplaying = false;
					m = new MediaPlayer();
					if (isplaying == false) {
						m.setDataSource(outputFile);
						m.prepare();
						m.start();
						isplaying = true;
						btnPlay.setText("Stop");
						Toast.makeText(getApplicationContext(),
								"Playing audio", Toast.LENGTH_LONG).show();
						seekBar.setMax(m.getDuration());

						updateSeekBar();

					}
				} catch (Exception e) {
				}
			}
		});
	}

	private void updateSeekBar() {

		mHandler.postDelayed(getTimeTask, 1000);

	}

	private Runnable getTimeTask = new Runnable() {

		@Override
		public void run() {
			long currentPosition = m.getCurrentPosition();
			seekBar.setProgress((int) currentPosition);
			mHandler.postDelayed(this, 100);

		}
	};

	public void backToMain(){
		btnBackToMainActivity.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(getApplicationContext(),MainActivity.class);
				intent.putExtra("audioPath",outputFile);
				startActivity(intent);
				
			}
		});
	}

}
