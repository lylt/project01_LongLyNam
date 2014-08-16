package com.example.iremember;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	private static final String DB_NAME="MyDatabase.db";
	private static final int vs=1;
	private SQLiteDatabase database;
	//init table
	public static final String TB_NAME="remember";
	public static final String CL_ID="_id";
	public static final String CL_Tittle="Tittle";
	public static final String CL_body="Body";
	public static final String CL_time="Time";

	public MySQLiteOpenHelper(Context context) {
		super(context, DB_NAME,null,vs);
		database=this.getWritableDatabase();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sqlCreateDB="CREATE TABLE "+TB_NAME+"("+ CL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
		CL_Tittle+" TEXT, "+CL_body+" TEXT, "+CL_time+" TEXT)";
		Log.d("debug",sqlCreateDB);
		db.execSQL(sqlCreateDB);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TB_NAME);
		 onCreate(db);
	}
	public void INSERT_RECORD(Record r){
		ContentValues mcontentValues= new ContentValues();
		mcontentValues.put(CL_Tittle, r.getTittle());
		mcontentValues.put(CL_body, r.getBody());
		mcontentValues.put(CL_time,r.getTime());
		database.insertOrThrow(TB_NAME, null, mcontentValues);
		
	}
	public void DELETE_Record(int id){
		database.delete(TB_NAME, CL_ID + " = " + id, null);
	}
	public void UPDATE_SinhVien(Record r, int id) {
		 ContentValues values = new ContentValues();
		 values.put(CL_Tittle, r.getTittle());
		 values.put(CL_body, r.getBody());
		 database.update(TB_NAME, values, "_id = " + id, null);
		 }
		 
		// select toan bo bang TB_NAME = "SinhVien"
		 public Cursor SELECT_ALL__RECORD() {
		 return database.query(TB_NAME, null, null, null, null, null, null);
		 }
		
		 
		 public Cursor SELECTSQL(String sql) {
		 return database.rawQuery(sql, null);
		 }
		 
		 public void CloseBD() {
		 if (database != null && database.isOpen())
		 database.close();
		 }
		 public void openDB()throws SQLException{
			 String DB_Path="/data/data/com.example.iremember/databases/";
			 String myPath=DB_Path+DB_NAME;
			 database.openDatabase(myPath,null, SQLiteDatabase.OPEN_READONLY);
		 }
		 public ArrayList<Record> getData(){
			 String slQuery="SELECT *FROM "+TB_NAME;
			 SQLiteDatabase db=this.getReadableDatabase();
			 Cursor cursor=db.rawQuery(slQuery, null);
			 ArrayList<Record> arrRecord=new ArrayList<Record>();
			 if(cursor.moveToFirst()){
				 do{
				arrRecord.add(new Record(cursor.getString(1),cursor.getString(2),cursor.getString(3)));
				 }while(cursor.moveToNext());
			 }
			 db.close();
			 return arrRecord;
			 
		 }
		 public void delete(Record r){
			 try{
			 String sqlQuery="DELETE FROM "+TB_NAME +" WHERE "+CL_Tittle+" = "+"\""+ r.getTittle()+"\""+" AND "+
		 CL_body+" = "+"\""+r.getBody()+"\""+" AND "+CL_time+" = "+"\""+r.getTime()+"\"";
			 SQLiteDatabase db=this.getReadableDatabase();
			 db.execSQL(sqlQuery);
			 db.close();}catch(Exception e){}
		 }

}
