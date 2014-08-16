package com.example.iremember;

import java.io.Serializable;

public class Record	implements Serializable {
	private String tittle;
	private String body;
	private String Time;
	public Record(){
		tittle="";
		body="";
		
	}
	public Record(String t,String b){
		tittle=t;
		body=b;
	}
	public String getTittle() {
		return tittle;
	}
	public void setTittle(String tittle) {
		this.tittle = tittle;
	}
	public Record(String tittle, String body, String time) {
		super();
		this.tittle = tittle;
		this.body = body;
		Time = time;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
}
