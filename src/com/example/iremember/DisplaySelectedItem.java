package com.example.iremember;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplaySelectedItem extends Activity {
	TextView tvTittle,tvBody;
	Button btnPlayAudio;
	MediaPlayer m;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_selected_item);
		tvTittle=(TextView) findViewById(R.id.tvTittle);
		tvBody=(TextView) findViewById(R.id.tvBody);
		btnPlayAudio=(Button) findViewById(R.id.btnPlayAudio);
		Intent i=getIntent();
		final String data[]=i.getStringArrayExtra("data");
		tvTittle.setText(data[0]);
		tvBody.setText(data[1]);
		btnPlayAudio.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
				m= new MediaPlayer();
				m.setDataSource(data[2]);
				m.prepare();
				m.start();}catch(Exception e){}
			}
		});
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