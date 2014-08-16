package com.example.iremember;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomRecord extends LinearLayout {
	TextView Tittle;
	TextView Body;
	TextView Time;
	public CustomRecord(Context context) {
		super(context);
		LayoutInflater li = (LayoutInflater) this.getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		li.inflate(R.layout.record, this,true);
		Tittle=(TextView) findViewById(R.id.tvtittle);
		Body=(TextView) findViewById(R.id.tvBbody);
		Time=(TextView) findViewById(R.id.tvTime);
	}

}
