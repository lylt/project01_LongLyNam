package com.example.iremember;

import java.util.Calendar;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends Activity {
	EditText edtTittle;
	EditText edtBody;
	Button btnCreate,btnAddVideo;
	TextView tvTime;
	private MySQLiteOpenHelper dataHelper;
	private Cursor cusor;
	private SimpleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dataHelper = new MySQLiteOpenHelper(MainActivity.this);
		edtTittle = (EditText) findViewById(R.id.edtTittle);
		edtTittle.setText("");
		edtBody = (EditText) findViewById(R.id.edtBody);
		edtBody.setText("");
		btnAddVideo=(Button) findViewById(R.id.btnAddVideo);
		btnCreate = (Button) findViewById(R.id.btnCreate);
		tvTime = (TextView) findViewById(R.id.tvTime);
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
				tittle = edtTittle.getText().toString();
				body = edtBody.getText().toString();
				String time = tvTime.getText().toString();
				if(!tittle.equals("")){
				Record r = new Record(tittle, body, time);
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
	}
	public void addVideo(){
		btnAddVideo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent mIntent= new Intent(getApplicationContext(),AddVideoActivity.class);
				startActivity(mIntent);
				
			}
		});
	}

}
