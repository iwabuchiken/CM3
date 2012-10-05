package cm3.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import android.os.AsyncTask;

// Apache
import org.apache.commons.lang.StringUtils;

// REF=> http://commons.apache.org/net/download_net.cgi
//REF=> http://www.searchman.info/tips/2640.html

//import org.apache.commons.net.ftp.FTPReply;

public class Methods {
	
	/****************************************
	 *	getMillSeconds_now()
	 * 
	 * <Caller> 
	 * 1. ButtonOnClickListener # case main_bt_start
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public static long getMillSeconds_now() {
		
		Calendar cal = Calendar.getInstance();
		
		return cal.getTime().getTime();
		
	}//private long getMillSeconds_now(int year, int month, int date)

	public static long getMillSeconds(int year, int month, int date) {
		// Calendar
		Calendar cal = Calendar.getInstance();
		
		// Set time
		cal.set(year, month, date);
		
		// Date
		Date d = cal.getTime();
		
		return d.getTime();
		
	}//private long getMillSeconds(int year, int month, int date)


	public static File[] get_file_list(String file_full_path) {
		/*********************************
		 * memo
		 *********************************/
		File f = new File(file_full_path);
		
		File[] f_list = f.listFiles();
		
		// Log
		Log.d("Methods.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "f_list.length=" + f_list.length);
		
		return f_list;
		
	}//public static boolean get_file_list(String file_full_path)

    public static void drop_table(Activity actv, String dbName, String tableName) {
    	// Setup db
		DBUtils dbu = new DBUtils(actv, dbName);
		
		SQLiteDatabase wdb = dbu.getWritableDatabase();
		
		boolean res = 
				dbu.dropTable(actv, wdb, tableName);
		
		if (res == true) {
			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table dropped: " + tableName);
		} else {//if (res == true)

			// Log
			Log.d("MainActv.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Drop table => Failed: " + tableName);
			
		}//if (res == true)
		

		wdb.close();
		
		
	}//private void drop_table(String tableName)

	public static boolean refresh_db(Activity actv, 
			String dbName, String tName_main,
			String f_full_path) {
		/*********************************
 		 * 1. Set up DB(writable)
 		 * 
		 * 2. Table exists?
		 * 2-1. If no, then create one
		 * 
		 * 3. Execute query for image files

		 * 4. Insert data into db
		 * 5. Update table "refresh_log"
		 * 
		 * 9. Close db
		 * 10. Return

		 *********************************/
		//
		DBUtils dbu = new DBUtils(actv, dbName);
		
		//
		SQLiteDatabase wdb = dbu.getWritableDatabase();

		/*********************************
		 * 2. Table exists?
		 *********************************/
		boolean res = Methods.refresh_db_1_setup_table(wdb, dbu, tName_main);
		
		if (res == false) {
			
			// Log
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Can't  create table");
			
			wdb.close();
			
			return false;
			
		}//if (res == false)

		
		
		wdb.close();
		
		a
//		boolean result = dbu.tableExists(wdb, tName_main);
//		
//		// If the table doesn't exist, create one
//		if (result == false) {
//
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Table doesn't exist: " + tName_main);
//			
//			result = 
//					dbu.createTable(wdb, tName_main, DBUtils.cols, DBUtils.col_types);
//			
//			if (result == false) {
//
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Can't create a table: "+ tName_main);
//				
//				return false;
//				
//			} else {//if (result == false)
//				
//				Log.d("Methods.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "Table created: "+ tName_main);
//				
//				return true;
//				
//			}//if (result == false)
//
//		} else {//if (result == false)
//			
//			Log.d("Methods.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Table exists: "+ tName_main);
//
//			return true;
//			
//		}//if (result == false)

		
		return false;
		
	}//public static boolean refresh_db

	private static boolean refresh_db_1_setup_table(SQLiteDatabase wdb,
			DBUtils dbu, String tName_main) {
		
		boolean result = dbu.tableExists(wdb, tName_main);
		
		// If the table doesn't exist, create one
		if (result == false) {

			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tName_main);
			
			result = 
					dbu.createTable(wdb, tName_main, DBUtils.cols, DBUtils.col_types);
			
			if (result == false) {

				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Can't create a table: "+ tName_main);
				
				return false;
				
			} else {//if (result == false)
				
				Log.d("Methods.java"
						+ "["
						+ Thread.currentThread().getStackTrace()[2]
								.getLineNumber() + "]", "Table created: "+ tName_main);
				
				return true;
				
			}//if (result == false)

		} else {//if (result == false)
			
			Log.d("Methods.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: "+ tName_main);

			return true;
			
		}//if (result == false)
		
	}//private static boolean refresh_db_1_setup_table

}//public class Methods
