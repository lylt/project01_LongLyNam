package com.example.iremember;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.os.Build;

public class FirstScreen extends Activity {
	Button btnAdd;
	EditText edtFilter;
	MySQLiteOpenHelper dataHelper;
	ArrayList<Record> arrRecord;
	ListView listview;
	RecordAdapter arrAdapter;
	ArrayAdapter<String> adapter;
	public void addRecord(Record r) {
		arrRecord.add(0, r);
		arrAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_screen);
		btnAdd = (Button) findViewById(R.id.btnAdd);
		edtFilter = (EditText) findViewById(R.id.edtFilter);
		arrRecord = new ArrayList<Record>();
		loadData();
		onClick();
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(mIntent);

			}
		});
		displaySelectedItem();

	}

	// load data from SQLite when the program begin
	public void loadData() {
		dataHelper = new MySQLiteOpenHelper(FirstScreen.this);
		dataHelper.openDB();
		arrAdapter = new RecordAdapter(this, R.layout.first_screen, arrRecord);
		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(arrAdapter);
		ArrayList<Record> data=dataHelper.getData();
		for(int i=0;i<data.size();i++){
			this.addRecord(data.get(i));
		}
		Filter();
		
	}

	public void onClick() {
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final Record r=arrRecord.get(position);
				final int p = position;
				AlertDialog alertDialog = new AlertDialog.Builder(
						FirstScreen.this).create();
				alertDialog.setTitle("Delete?");
				alertDialog.setIcon(R.drawable.al);
				alertDialog
						.setMessage("are you sure want to delete this memory? click OK to delete or Cancel");
				alertDialog.setButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								arrRecord.remove(p);
								arrAdapter.notifyDataSetChanged();
								dataHelper.delete(r);
							}
						});
				alertDialog.setButton2("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();
							}
						});
				alertDialog.show();
				return false;
			}
		});
	}
	public void displaySelectedItem(){
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent iIntent=new Intent(getApplicationContext(),DisplaySelectedItem.class);
				Record r= arrRecord.get(position);
				String data[]={r.getTittle(),r.getBody(),r.getAudioPath()};
				iIntent.putExtra("data", data);
				startActivity(iIntent);
			}
			
		});
	}
	public void Filter(){
		edtFilter.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int textlength=s.length();
				ArrayList<Record> arrResult=new ArrayList<Record>();
				for(Record r:arrRecord){
					if(textlength<=r.getTittle().length()){
						if(r.getTittle().toLowerCase().contains(s.toString().toLowerCase())){
							arrResult.add(r);
						}
					}
				}
				RecordAdapter mAdapter=new RecordAdapter(getApplicationContext(), R.layout.first_screen,arrResult);
				listview.setAdapter(mAdapter);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
