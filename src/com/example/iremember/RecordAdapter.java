package com.example.iremember;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class RecordAdapter extends ArrayAdapter {
	int resource;
	ArrayList<Record> arrRecord;
	TextView Tittle;
	TextView Body;
	TextView Time;
	Context context;
	Record record;

	public RecordAdapter(Context context, int resource) {
		super(context, resource);

	}

	public RecordAdapter(Context context, int resource, ArrayList<Record> arr) {
		super(context, resource,arr);
		this.context = context;
		this.resource = resource;
		this.arrRecord = arr;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View friendView = convertView;
		if (friendView == null) {
			friendView = new CustomRecord(getContext());

		}
		record = arrRecord.get(position);
		if (record != null) {
			Tittle = ((CustomRecord) friendView).Tittle;
			Body = ((CustomRecord) friendView).Body;
			Time=((CustomRecord) friendView).Time;
			Tittle.setText(record.getTittle());
			Body.setText(record.getBody());
			Time.setText(record.getTime());
			
		}
		return friendView;
	}

}
