package org.orange.querysystem.util;

import java.util.ArrayList;

import org.orange.studentinformationdatabase.StudentInfDBAdapter;

import util.webpage.Course;
import android.content.Context;
import android.database.SQLException;
import android.os.AsyncTask;
import android.widget.Toast;

public class ReadDB extends AsyncTask<String,Void,ArrayList<Course>>{
	public interface OnPostExcuteListerner{
		public void onPostReadFromDB(ArrayList<Course> courses);
	}
	private Context context;
	private OnPostExcuteListerner listener;
	StudentInfDBAdapter studentInfDBAdapter = null;
	public static final String TAG = "org.orange.querysystem";

	public ReadDB(Context context, OnPostExcuteListerner listener) {
		super();
		this.context = context;
		this.listener = listener;
	}

	@Override
	protected ArrayList<Course> doInBackground(String... args) {
		ArrayList<Course> result = null;
		studentInfDBAdapter = new StudentInfDBAdapter(context);
		if(args[1].equals("this")){
			try {
				studentInfDBAdapter.open();
				result = studentInfDBAdapter.getThisTermCoursesFromDB(null, args[0]);
			} catch(SQLException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				studentInfDBAdapter.close();
			}
		}else{
			try {
				studentInfDBAdapter.open();
				result = studentInfDBAdapter.getNextTermCoursesFromDB(null, args[0]);
			} catch(SQLException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				studentInfDBAdapter.close();
			}
		}
		
		return result;
	}

	@Override
	protected void onCancelled() {
		studentInfDBAdapter.close();
	}

	@Override
	protected void onPostExecute(ArrayList<Course> courses){
		if(courses != null)
			listener.onPostReadFromDB(courses);	
		else{
			Toast.makeText(context, "数据库无数据，请刷新！", Toast.LENGTH_LONG).show();			
		}
	}	
}
