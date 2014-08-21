package com.example.iremember;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class AddAudioActivity extends Activity {
	Button btnRecordAudio, btnChooseAudio, btnPlay, btnBackToMainActivity;
	MediaPlayer m;
	SeekBar seekBar;
	String outputFile = null;
	Intent i;
	Handler mHandler = new Handler();
	MediaRecorder myAudioRecorder;

	boolean isRecording = false;

	protected static final int RESULT_LOAD_AUDIO = 50;
	private static final int AUDIO_RECORD = 101;
	public static final int MEDIA_TYPE_AUDIO = 3;

	static File mediaFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_audio);
		btnRecordAudio = (Button) findViewById(R.id.btnRecord);
		btnChooseAudio = (Button) findViewById(R.id.btnChooseAudio);
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		btnPlay = (Button) findViewById(R.id.btnPlay);
		btnBackToMainActivity = (Button) findViewById(R.id.btnBackToMainActivity);

		myAudioRecorder = new MediaRecorder();
		myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

		recordAudio();
		chooseAudio();
		play();
		backToMain();
	}

	// save in folder IRemember3/Audio
	private static File getOutputImageFile(int type) {

		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		String audioName = timeStamp + "_";
		File mediaFileDir = new File(Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/IRemember3/Audio/");

		if (type == MEDIA_TYPE_AUDIO) {
			try {
				mediaFile = File
						.createTempFile(audioName, ".3gp", mediaFileDir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// mediaFile = new File(mediaStorageDir.getPath());
		} else {
			return null;
		}
		return mediaFile;

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
				if (!isRecording) {
					outputFile = getOutputImageFile(MEDIA_TYPE_AUDIO).getPath();
					myAudioRecorder.setOutputFile(outputFile);
					btnRecordAudio.setText("Stop record Audio");
					//
					try {
						myAudioRecorder.prepare();
						myAudioRecorder.start();
						isRecording = true;
					} catch (Exception e) {
					}

					Toast.makeText(getApplicationContext(),
							"audiorecording started", 3000).show();

				} else {
					myAudioRecorder.stop();
					myAudioRecorder.release();
					myAudioRecorder = null;
					btnRecordAudio.setText("Record an audio");
					isRecording = false;
					Toast.makeText(getApplicationContext(),
							"audiorecording stopped", 3000).show();
				}

			}
		});
	}

	public void chooseAudio() {
		btnChooseAudio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intentAudio = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intentAudio, RESULT_LOAD_AUDIO);
			}
		});

	}

	// override onActivityResult
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == AUDIO_RECORD) {

			if (resultCode == RESULT_OK) {

				// the selected audio.
				Uri uri = data.getData();
				outputFile = uri.getPath();
			}
		} else if (requestCode == RESULT_LOAD_AUDIO) {
			if (resultCode == RESULT_OK) {
				Uri _uri = data.getData();
				// User had pick an image.
				Cursor cursor = getContentResolver()
						.query(_uri,
								new String[] { android.provider.MediaStore.Audio.AudioColumns.DATA },
								null, null, null);
				cursor.moveToFirst();
				// Link to the image
				outputFile = cursor.getString(0);
				cursor.close();

			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void play() {
		btnPlay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
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

	public void backToMain() {
		btnBackToMainActivity.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra(MainActivity.AUDIO_KEY, outputFile);
				setResult(Activity.RESULT_OK, intent);
				finish();
			}
		});
	}

}
