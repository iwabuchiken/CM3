package cm3.utils;


import java.util.ArrayList;
import java.util.List;

import cm3.items.AI;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
//import android.view
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/****************************************
 * Copy & pasted from => C:\WORKS\WORKSPACES_ANDROID\ShoppingList\src\shoppinglist\main\DBUtils.java
 ****************************************/
public class DBUtils extends SQLiteOpenHelper{

	/*****************************************************************
	 * Class fields
	 *****************************************************************/
	 // DB name
	static String dbName = null;
	
	// Activity
	Activity activity;
	
	//
	Context context;

	/*********************************
	 * DB
	 *********************************/
	// Database
	SQLiteDatabase db = null;

	//
	String[] cols_with_index = 
				{android.provider.BaseColumns._ID, 
					"file_id", 		"file_path", "file_name", "date_added",
					"date_modified", "memos", "tags"};
	
	String[] col_types_with_index =
				{	"INTEGER", "TEXT", 	"TEXT",		"INTEGER",
					"INTEGER",		"TEXT",	"TEXT"};

	// Main data
	public static String[] cols = 
		{"file_id", "file_path", "file_name", 	"date_added",
		"date_modified",	"memos", "tags", 	"last_viewed_at"};
//	"date_modified", "memos", "tags"};

	public static String[] col_types =
		{"INTEGER", "TEXT", 	"TEXT",			"INTEGER",
		"INTEGER",			"TEXT",	"TEXT",		"INTEGER"};

	static String[] cols_for_insert_data = 
		{"file_id", 		"file_path", "file_name", "date_added", "date_modified"};

	// Proj
	static String[] proj = {
		MediaStore.Images.Media._ID, 
		MediaStore.Images.Media.DATA,
		MediaStore.Images.Media.DISPLAY_NAME,
		MediaStore.Images.Media.DATE_ADDED,
		MediaStore.Images.Media.DATE_MODIFIED,
		};

	static String[] proj_for_get_data = {
		MediaStore.Images.Media._ID, 
		MediaStore.Images.Media.DATA,
		MediaStore.Images.Media.DISPLAY_NAME,
		MediaStore.Images.Media.DATE_ADDED,
		MediaStore.Images.Media.DATE_MODIFIED,
		"memos",
		"tags"
		};

	static String[] cols_refresh_log = {
		"last_refreshed", "num_of_items_added"
	};
	
	static String[] col_types_refresh_log = {
		"INTEGER", 			"INTEGER"
	};

	static String[] cols_memo_patterns = {"word", "table_name"};
	static String[] col_types_memo_patterns = {"TEXT", "TEXT"};
	
	static String table_name_memo_patterns = "memo_patterns";
	
	/*****************************************************************
	 * Constructor
	 *****************************************************************/
	public DBUtils(Context context, String dbName) {
		super(context, dbName, null, 1);
		
		// Initialize activity
		this.activity = (Activity) context;
		
		this.context = context;
		
		this.dbName = dbName;
		
	}//public DBUtils(Context context)

//	public DBUtils() {
//		// TODO 自動生成されたコンストラクター・スタブ
//	}

	/*******************************************************
	 * Methods
	 *******************************************************/
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO 自動生成されたメソッド・スタブ
		
	}//public void onCreate(SQLiteDatabase db)

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO 自動生成されたメソッド・スタブ
		
	}

	/****************************************
	 * createTable_generic()
	 * 
	 * <Caller> 
	 * 1. 
	 * 
	 * <Desc> 1. <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public boolean createTable(
					SQLiteDatabase db, String tableName, String[] columns, String[] types) {
		/*----------------------------
		 * Steps
		 * 1. Table exists?
		 * 2. Build sql
		 * 3. Exec sql
			----------------------------*/
		
		//
//		if (!tableExists(db, tableName)) {
		if (tableExists(db, tableName)) {
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists => " + tableName);
			
			return false;
		}//if (!tableExists(SQLiteDatabase db, String tableName))
		
		/*----------------------------
		 * 2. Build sql
			----------------------------*/
		//
		StringBuilder sb = new StringBuilder();
		
		sb.append("CREATE TABLE " + tableName + " (");
		sb.append(android.provider.BaseColumns._ID +
							" INTEGER PRIMARY KEY AUTOINCREMENT, ");
		
		// created_at, modified_at
		sb.append("created_at INTEGER, modified_at INTEGER, ");
		
		int i = 0;
		for (i = 0; i < columns.length - 1; i++) {
			sb.append(columns[i] + " " + types[i] + ", ");
		}//for (int i = 0; i < columns.length - 1; i++)
		
		sb.append(columns[i] + " " + types[i]);
		
		sb.append(");");
		
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "sql => " + sb.toString());
		
		/*----------------------------
		 * 3. Exec sql
			----------------------------*/
		//
		try {
//			db.execSQL(sql);
			db.execSQL(sb.toString());
			
			// Log
			Log.d(this.getClass().getName() + 
					"["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table created => " + tableName);
			
			
			return true;
		} catch (SQLException e) {
			// Log
			Log.d(this.getClass().getName() + 
					"[" + Thread.currentThread().getStackTrace()[2].getLineNumber() + "]", 
					"Exception => " + e.toString());
			
			return false;
		}//try
		
	}//public boolean createTable(SQLiteDatabase db, String tableName)

	public boolean tableExists(SQLiteDatabase db, String tableName) {
		// The table exists?
		Cursor cursor = db.rawQuery(
									"SELECT * FROM sqlite_master WHERE tbl_name = '" + 
									tableName + "'", null);
		
		((Activity) context).startManagingCursor(cursor);
//		actv.startManagingCursor(cursor);
		
		// Judge
		if (cursor.getCount() > 0) {
			return true;
		} else {//if (cursor.getCount() > 0)
			return false;
		}//if (cursor.getCount() > 0)
	}//public boolean tableExists(String tableName)

	public String[] get_cols_with_index() {
		return cols_with_index;
	}
	
	public String[] get_col_types_with_index() {
		return col_types_with_index;
	}

	public String[] get_cols() {
		return cols;
	}
	
	public String[] get_col_types() {
		return col_types;
	}

	public boolean dropTable(Activity actv, SQLiteDatabase db, String tableName) {
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Starting: dropTable()");
		
		/*------------------------------
		 * The table exists?
		 *------------------------------*/
		// The table exists?
		boolean tempBool = tableExists(db, tableName);
		
		if (tempBool == true) {
		
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table exists: " + tableName);

		} else {//if (tempBool == true)
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Table doesn't exist: " + tableName);

			return false;
		}//if (tempBool == true)

		/*------------------------------
		 * Drop the table
		 *------------------------------*/
		// Define the sql
        String sql 
             = "DROP TABLE " + tableName;
        
        // Execute
        try {
			db.execSQL(sql);
			
			// Vacuum
			db.execSQL("VACUUM");
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "The table dropped => " + tableName);
			
			// Return
			return true;
			
		} catch (SQLException e) {
			// TODO 自動生成された catch ブロック
			// Log
			Log.e("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "DROP TABLE => failed (table=" + tableName + "): " + e.toString());
			
			// debug
			Toast.makeText(actv, 
						"DROP TABLE => failed(table=" + tableName, 
						3000).show();
			
			// Return
			return false;
		}//try

	}//public boolean dropTable(String tableName) 

	public boolean insertData(SQLiteDatabase db, String tableName, 
												String[] columnNames, String[] values) {
		
////		String sql = "SELECT * FROM TABLE " + DBUtils.table_name_memo_patterns;
//		String sql = "SELECT * FROM " + DBUtils.table_name_memo_patterns;
//		
//		Cursor c = db.rawQuery(sql, null);
//		
//		
//		
//		// Log
//		Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount() => " + c.getCount() + " / " +
//				"c.getColumnCount() => " + c.getColumnCount());
//		
//		c.close();
		
		
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
			db.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
			// Put values
			for (int i = 0; i < columnNames.length; i++) {
				val.put(columnNames[i], values[i]);
			}//for (int i = 0; i < columnNames.length; i++)
			
			// Insert data
			db.insert(tableName, null, val);
			
			// Set as successful
			db.setTransactionSuccessful();
			
			// End transaction
			db.endTransaction();
			
			// Log
//			Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Data inserted => " + "(" + columnNames[0] + " => " + values[0] + 
//				" / " + columnNames[3] + " => " + values[3] + ")");
			
			return true;
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
		
//		//debug
//		return false;
		
	}//public insertData(String tableName, String[] columnNames, String[] values)

	public boolean insertData(SQLiteDatabase db, String tableName, 
											String[] columnNames, long[] values) {
		/*----------------------------
		* 1. Insert data
		----------------------------*/
		try {
			// Start transaction
			db.beginTransaction();
			
			// ContentValues
			ContentValues val = new ContentValues();
			
			// Put values
			for (int i = 0; i < columnNames.length; i++) {
				val.put(columnNames[i], values[i]);
			}//for (int i = 0; i < columnNames.length; i++)
			
			// Insert data
			db.insert(tableName, null, val);
			
			// Set as successful
			db.setTransactionSuccessful();
			
			// End transaction
			db.endTransaction();
			
			// Log
			Log.d("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Data inserted => " + "(" + columnNames[0] + " => " + values[0] + "), and others");
			
			return true;
		} catch (Exception e) {
			// Log
			Log.e("DBUtils.java" + "["
			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
			+ "]", "Exception! => " + e.toString());
			
			return false;
		}//try
	}//public insertData(String tableName, String[] columnNames, String[] values)

	public boolean insertData(SQLiteDatabase db, String tableName, AI ti) {
//		/*----------------------------
//		* 1. Insert data
//		----------------------------*/
//		try {
//			// Start transaction
//			db.beginTransaction();
//			
//			// ContentValues
//			ContentValues val = new ContentValues();
//			
////			{"file_id", 		"file_path", "file_name", "date_added",
////				"date_modified", "memos", "tags"};
//			
////			// Put values
////			for (int i = 0; i < columnNames.length; i++) {
////				val.put(columnNames[i], values[i]);
////			}//for (int i = 0; i < columnNames.length; i++)
//
//			val.put("file_id", ti.getFileId());
//			
//			val.put("file_path", ti.getFile_path());
//			val.put("file_name", ti.getFile_name());
//			
//			val.put("date_added", ti.getDate_added());
//			val.put("date_modified", ti.getDate_modified());
//			
//			val.put("memos", ti.getMemo());
//			val.put("tags", ti.getTags());
//			
//			// Insert data
//			db.insert(tableName, null, val);
//			
//			// Set as successful
//			db.setTransactionSuccessful();
//			
//			// End transaction
//			db.endTransaction();
//			
//			// Log
//			Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Data inserted => " + "(file_name => " + val.getAsString("file_name") + "), and others");
//			
//			return true;
//		} catch (Exception e) {
//			// Log
//			Log.e("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "Exception! => " + e.toString());
//			
//			return false;
//		}//try
		
		return false;
		
	}//public insertData(SQLiteDatabase db, String tableName, ThumbnailItem ti)

	public AI getData(Activity actv, SQLiteDatabase rdb, String tableName, long file_id) {
//		/*----------------------------
//		 * Steps
//		 * 1. 
//			----------------------------*/
//		String sql = "SELECT * FROM " + tableName + " WHERE file_id = '" + String.valueOf(file_id) + "'";
//		
//		Cursor c = rdb.rawQuery(sql, null);
//		
//		actv.startManagingCursor(c);
//		
//		// Log
//		Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "c.getCount() => " + c.getCount());
//		
//		
//		c.moveToFirst();
//		
////		ThumbnailItem ti = new ThumbnailItem(
//		return new AI(
//				c.getLong(1),	// file_id
//				c.getString(2),	// file_path
//				c.getString(3),	// file_name
//				c.getLong(4),	// date_added
//				c.getLong(5),		// date_modified
//				c.getString(6),		// memos
//				c.getString(7)		// tags
//		);
//		
//		
////		ThumbnailItem ti = new ThumbnailItem(
////							c.getLong(1),
////							c.getString(2),
////							c.getString(3),
////							c.getLong(4),
////							c.getLong(4),
////							);
//
//		
//
		
		return null;
		
	}//public void getData(SQLiteDatabase rdb, String tableName, long file_id)

	public boolean updateData_memos(Activity actv, SQLiteDatabase wdb, 
								String tableName, AI ti) {
//		/*----------------------------
//		 * Steps
//		 * 1. 
//			----------------------------*/
//		String sql = "UPDATE " + tableName + " SET " + 
//						"file_id='" + String.valueOf(ti.getFileId()) + "', " + 
//						"file_path='" + ti.getFile_path() + "', " +
//						"file_name='" + ti.getFile_name() + "', " +
//						"date_added='" + String.valueOf(ti.getDate_added()) + "', " +
//						"date_modified='" + String.valueOf(ti.getDate_modified()) + "', " +
//						"memos='" + ti.getMemo() + "', " +
//						"tags='" + ti.getTags() + "'" +
//						
//						" WHERE file_id = '" + String.valueOf(ti.getFileId()) + "'";
//		
//						
////								"file_id", 		"file_path", "file_name", "date_added", "date_modified"
////		static String[] cols = 
////			{"file_id", 		"file_path", "file_name", "date_added",
////				"date_modified", "memos", "tags"};
//
//		
//		try {
//			
//			wdb.execSQL(sql);
//			
//			// Log
//			Log.d("DBUtils.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "sql => Done: " + sql);
//			
////			Methods.toastAndLog(actv, "Data updated", 2000);
//			
//			return true;
//			
//			
//		} catch (SQLException e) {
//			// Log
//			Log.d("DBUtils.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
//			
//			return false;
//		}
////		
////		actv.startManagingCursor(c);
////		
////		// Log
////		Log.d("DBUtils.java" + "["
////				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////				+ "]", "c.getCount() => " + c.getCount());
////		
////		
////		c.moveToFirst();
		
		return false;
		
	}//public void updateData_memos

	public static boolean updateData_TI_last_viewed_at(Activity actv, SQLiteDatabase wdb, 
			String tableName, AI ti) {
//		/*----------------------------
//		* Steps
//		* 1. 
//		----------------------------*/
//		String sql = "UPDATE " + tableName + " SET " + 
////			"file_id='" + String.valueOf(ti.getFileId()) + "', " + 
////			"last_viewed_at='" + Methods.getMillSeconds_now() + "', " +
//			"last_viewed_at='" + Methods.getMillSeconds_now() + "' " +
//			
//			" WHERE file_id = '" + String.valueOf(ti.getFileId()) + "'";
//		
//			
//		//			"file_id", 		"file_path", "file_name", "date_added", "date_modified"
//		//static String[] cols = 
//		//{"file_id", 		"file_path", "file_name", "date_added",
//		//"date_modified", "memos", "tags"};
//		
//		
//		try {
//		
//			wdb.execSQL(sql);
//			
//			// Log
//			Log.d("DBUtils.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "sql => Done: " + sql);
//			
//			//Methods.toastAndLog(actv, "Data updated", 2000);
//			
//			return true;
//			
//			
//		} catch (SQLException e) {
//			// Log
//			Log.d("DBUtils.java" + "["
//			+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//			+ "]", "Exception => " + e.toString() + " / " + "sql: " + sql);
//			
//			return false;
//		}
//		//
//		//actv.startManagingCursor(c);
//		//
//		//// Log
//		//Log.d("DBUtils.java" + "["
//		//+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//		//+ "]", "c.getCount() => " + c.getCount());
//		//
//		//
//		//c.moveToFirst();
		
		return false;
	
	}//public void updateData_memos

	public boolean deleteData(Activity actv, SQLiteDatabase db, String tableName, long file_id) {
		/*----------------------------
		 * Steps
		 * 1. Item exists in db?
		 * 2. If yes, delete it
			----------------------------*/
		/*----------------------------
		 * 1. Item exists in db?
			----------------------------*/
		boolean result = isInDB_long(db, tableName, file_id);
		
		if (result == false) {		// Result is false ==> Meaning the target data doesn't exist
											//							in db; Hence, not executing delete op
			
			// debug
			Toast.makeText(actv, 
					"Data doesn't exist in db: " + String.valueOf(file_id), 
					2000).show();
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"Data doesn't exist in db => Delete the data (file_id = " + String.valueOf(file_id) + ")");
			
			return false;
			
		} else {//if (result == false)
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", 
					"Data exists in db" + String.valueOf(file_id) + ")");
			
		}//if (result == false)
		
		
		String sql = 
						"DELETE FROM " + tableName + 
						" WHERE file_id = '" + String.valueOf(file_id) + "'";
		
		try {
			db.execSQL(sql);
			
//			// Log
//			Log.d("DBUtils.java" + "["
//					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//					+ "]", "Data deleted => file id = " + String.valueOf(file_id));
			
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Sql executed: " + sql);
			
			return true;
			
		} catch (SQLException e) {
			// Log
			Log.d("DBUtils.java" + "["
					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
					+ "]", "Exception => " + e.toString());
			
			return false;
			
		}//try
		
	}//public boolean deleteData(SQLiteDatabase db, String tableName, long file_id)

	/****************************************
	 *
	 * 
	 * <Caller> 
	 * 1. deleteData(Activity actv, SQLiteDatabase db, String tableName, long file_id)
	 * 
	 * <Desc> 
	 * 1. REF=> http://stackoverflow.com/questions/3369888/android-sqlite-insert-unique
	 * 
	 * <Params> 1.
	 * 
	 * <Return> 1.
	 * 
	 * <Steps> 1.
	 ****************************************/
	public boolean isInDB_long(SQLiteDatabase db, String tableName, long file_id) {
		
		String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE file_id = '" +
						String.valueOf(file_id) + "'";
		
		long result = DatabaseUtils.longForQuery(db, sql, null);
		
		// Log
		Log.d("DBUtils.java" + "["
				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
				+ "]", "result => " + String.valueOf(result));
		
		if (result > 0) {

			return true;
			
		} else {//if (result > 0)
			
			return false;
			
		}//if (result > 0)
		
//		return false;
		
	}//public boolean isInDB_long(SQLiteDatabase db, String tableName, long file_id)


	public List<AI> get_all_data_history(Activity actv,
			SQLiteDatabase rdb, long[] history_file_ids, String[] history_table_names) {
//		/*********************************
//		 * 1. Declare tiList
//		 * 2. Query
//		 * 
//		 * 2-1. Record exists?
//		 * 2-2. Create a TI object
//		 * 3. Add to list
//		 * 
//		 * 4. Sort list
//		 * 5. Return list
//		 *********************************/
//		List<AI> tiList = new ArrayList<AI>();
//		
////		for (String name : history_table_names) {
////			
////			// Log
////			Log.d("DBUtils.java" + "["
////					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////					+ "]", "history: name=" + name);
////			
////		}//for (String name : history_table_names)
//		
//		for (int i = 0; i < history_file_ids.length; i++) {
//			
//			/*********************************
//			 * 2. Query
//			 *********************************/
//			String sql = "SELECT * FROM " + history_table_names[i]
//						+ " WHERE file_id='" + history_file_ids[i] + "'";
//			
//			Cursor c = null;
//			
//			try {
//				
//				c = rdb.rawQuery(sql, null);
//				
////				// Log
////				Log.d("DBUtils.java"
////						+ "["
////						+ Thread.currentThread().getStackTrace()[2]
////								.getLineNumber() + "]", "Query => Done");
//				
//			} catch (Exception e) {
//				// Log
//				Log.d("Methods.java" + "["
//						+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//						+ "]", "Exception => " + e.toString()
//						+ "(i=" + i + ")");
//				
//				continue;
//				
////				rdb.close();
////				
////				return null;
//			}
//			
////			// Log
////			Log.d("Methods.java" + "["
////					+ Thread.currentThread().getStackTrace()[2].getLineNumber()
////					+ "]", "c.getCount() => " + c.getCount());
//
//			/*********************************
//			 * 2-1. Record exists?
//			 *********************************/
//			if (c.getCount() < 1) {
//				
//				// Log
//				Log.d("DBUtils.java"
//						+ "["
//						+ Thread.currentThread().getStackTrace()[2]
//								.getLineNumber() + "]", "c.getCount() < 1");
//				
//				continue;
//				
//			}
//			
//			
//			/*********************************
//			 * 2-2. Create a TI object
//			 *********************************/
//			c.moveToFirst();
//			
//			AI ti = new AI(
//					c.getLong(1),	// file_id
//					c.getString(2),	// file_path
//					c.getString(3),	// file_name
//					
//					c.getLong(4),	// date_added
////					c.getLong(5)		// date_modified
//					c.getLong(5),		// date_modified
//					
//					c.getString(6),	// memos
//					c.getString(7),	// tags
//					
//					c.getLong(8)	// last_viewed_at
//					);	
//
//			/*********************************
//			 * 3. Add to list
//			 *********************************/
//			tiList.add(ti);
//					
//			/*----------------------------
//			 * 2.2. Add to list
//				----------------------------*/
////			c.moveToNext();
//
//			
//		}//for (int i = 0; i < history_file_ids.length; i++)
//		
//		
//		// Log
//		Log.d("DBUtils.java" + "["
//				+ Thread.currentThread().getStackTrace()[2].getLineNumber()
//				+ "]", "tiList.size()=" + tiList.size());
//		
//		/*********************************
//		 * 4. Sort list
//		 *********************************/
////		Methods.sort_tiList_last_viewed_at(tiList);
//		
//		return tiList;
		
		return null;
		
	}//public List<TI> get_all_data_history()
}//public class DBUtils

